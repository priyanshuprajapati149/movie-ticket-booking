package com.example.booking.dto;

import com.example.booking.entity.Cinema;

public class CinemaDistanceDTO {
    private Cinema cinema;
    private double distanceKm;

    public CinemaDistanceDTO(Cinema cinema, double distanceKm) {
        this.cinema = cinema;
        this.distanceKm = distanceKm;
    }

    public Cinema getCinema() { return cinema; }
    public double getDistanceKm() { return distanceKm; }
}
