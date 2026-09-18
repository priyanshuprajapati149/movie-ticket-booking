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
     * Uses a Redis Distributed Lock to prevent race conditions across multiple application instances.
     */
    @Transactional
    public Booking bookSeat(Long seatId, String username) {
        // 1. Define the distributed lock key based on the seat ID
        String lockKey = "seat_lock_" + seatId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 2. Try to acquire the lock. Wait up to 5 seconds, hold for 10 seconds.
            boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
            
            if (!isLocked) {
                throw new RuntimeException("Could not acquire lock for seat " + seatId + ". System is busy.");
            }

            // --- Critical Section Start ---
            
            // Find user
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Find seat (We still use the DB lock as a fallback/belt-and-suspenders, but Redis handles the primary lock)
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
            // --- Critical Section End ---

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            // 3. Always release the lock in a finally block!
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
