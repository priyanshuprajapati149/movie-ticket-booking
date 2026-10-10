package com.example.booking.service;

import com.example.booking.entity.Booking;
import com.example.booking.entity.Seat;
import com.example.booking.entity.User;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.SeatRepository;
import com.example.booking.repository.UserRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RedissonClient redissonClient;

    public BookingService(SeatRepository seatRepository, BookingRepository bookingRepository, UserRepository userRepository, RedissonClient redissonClient) {
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.redissonClient = redissonClient;
    }

    /**
     * Books a seat for a user.
     * Uses Redis Distributed Locking + pessimistic database locking fallback to prevent race conditions.
     */
    @Transactional
    public Booking bookSeat(Long seatId, String username) {
        // 1. Acquire Redis Distributed Lock
        String lockKey = "seatLock:" + seatId;
        RLock lock = redissonClient.getLock(lockKey);
        
        try {
            // Try to acquire lock, wait up to 10 seconds, lock expires after 10 seconds
            boolean isLocked = lock.tryLock(10, 10, TimeUnit.SECONDS);
            
            if (!isLocked) {
                throw new RuntimeException("System is busy. Please try again.");
            }

            // Find user
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Find seat WITH PESSIMISTIC LOCK (Fallback)
            Seat seat = seatRepository.findByIdWithLock(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            if (seat.getStatus() == Seat.SeatStatus.BOOKED) {
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " is already booked.");
            }

            // Mark seat as booked
            seat.setStatus(Seat.SeatStatus.BOOKED);
            seatRepository.save(seat);

            // Create booking record
            Booking booking = Booking.builder()
                    .user(user)
                    .seat(seat)
                    .bookingTime(LocalDateTime.now())
                    .build();

            return bookingRepository.save(booking);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Booking interrupted by server.");
        } finally {
            // Ensure lock is always released
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
