package com.example.booking.controller;

import com.example.booking.entity.Booking;
import com.example.booking.service.BookingService;
import com.example.booking.repository.BookingRepository;
import com.example.booking.dto.TicketDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    @PostMapping("/seat/{seatId}")
    public ResponseEntity<String> bookSeat(@PathVariable Long seatId, Authentication authentication) {
        try {
            String username = authentication.getName();
            Booking booking = bookingService.bookSeat(seatId, username);
            return ResponseEntity.ok("Successfully booked seat " + booking.getSeat().getSeatNumber() + " for user " + username);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mytickets")
    @Transactional(readOnly = true)
    public ResponseEntity<List<TicketDTO>> getMyTickets(Authentication authentication) {
        String username = authentication.getName();
        List<Booking> bookings = bookingRepository.findByUserUsername(username);
        
        List<TicketDTO> tickets = bookings.stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(tickets);
    }
}
