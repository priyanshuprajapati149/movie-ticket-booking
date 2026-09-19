package com.example.booking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "<h1>Welcome to the Movie Ticket Booking API!</h1>" +
               "<p>To test the race condition logic, you need to send an authenticated POST request to:</p>" +
               "<code>/api/bookings/seat/{seatId}</code>" +
               "<p><b>Test Users:</b> alice / password</p>";
    }
}
