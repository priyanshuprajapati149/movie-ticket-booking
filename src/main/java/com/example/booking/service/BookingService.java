package com.example.booking.service;

import com.example.booking.entity.Booking;
import com.example.booking.entity.Seat;
import com.example.booking.entity.User;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.SeatRepository;
import com.example.booking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BookingService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingService(SeatRepository seatRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    /**
     * Books a seat for a user.
     * Uses pessimistic database locking to prevent race conditions.
     */
    @Transactional
    public Booking bookSeat(Long seatId, String username) {
        // Find user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find seat WITH PESSIMISTIC LOCK
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
    }
}
