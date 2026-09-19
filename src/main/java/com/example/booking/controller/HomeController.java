package com.example.booking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "<h1>Welcome to the Movie Ticket Booking API (BookMyShow Edition)!</h1>" +
               "<h3>Browse (Public):</h3>" +
               "<ul>" +
               "<li><a href='/api/browse/movies'>/api/browse/movies</a> - View all movies</li>" +
               "<li><a href='/api/browse/movies/1/shows'>/api/browse/movies/1/shows</a> - View shows for Movie ID 1</li>" +
               "</ul>" +
               "<h3>Book (Requires Login):</h3>" +
               "<p>To test the race condition logic, you need to send an authenticated POST request to:</p>" +
               "<code>/api/bookings/seat/{seatId}</code>" +
               "<p><b>Test Users:</b> alice / password</p>";
    }
}
