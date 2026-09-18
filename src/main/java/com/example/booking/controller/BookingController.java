package com.example.booking.controller;

import com.example.booking.entity.Booking;
import com.example.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/seat/{seatId}")
    public ResponseEntity<?> bookSeat(@PathVariable Long seatId, Authentication authentication) {
        try {
            String username = authentication.getName();
            Booking booking = bookingService.bookSeat(seatId, username);
            return ResponseEntity.ok("Successfully booked seat " + booking.getSeat().getSeatNumber() + " for user " + username);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
