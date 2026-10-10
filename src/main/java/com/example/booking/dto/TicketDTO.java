package com.example.booking.dto;

import com.example.booking.entity.Booking;
import java.time.LocalDateTime;

public class TicketDTO {
    private Long bookingId;
    private String movieTitle;
    private String cinemaName;
    private String location;
    private String seatNumber;
    private LocalDateTime showTime;
    private LocalDateTime bookingTime;

    public TicketDTO(Booking booking) {
        this.bookingId = booking.getId();
        this.movieTitle = booking.getSeat().getShow().getMovie().getTitle();
        this.cinemaName = booking.getSeat().getShow().getCinema().getName();
        this.location = booking.getSeat().getShow().getCinema().getLocation();
        this.seatNumber = booking.getSeat().getSeatNumber();
        this.showTime = booking.getSeat().getShow().getStartTime();
        this.bookingTime = booking.getBookingTime();
    }

    public Long getBookingId() { return bookingId; }
    public String getMovieTitle() { return movieTitle; }
    public String getCinemaName() { return cinemaName; }
    public String getLocation() { return location; }
    public String getSeatNumber() { return seatNumber; }
    public LocalDateTime getShowTime() { return showTime; }
    public LocalDateTime getBookingTime() { return bookingTime; }
}
