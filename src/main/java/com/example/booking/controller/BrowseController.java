package com.example.booking.controller;

import com.example.booking.entity.Movie;
import com.example.booking.entity.Show;
import com.example.booking.repository.MovieRepository;
import com.example.booking.repository.ShowRepository;
import com.example.booking.repository.SeatRepository;
import com.example.booking.repository.CinemaRepository;
import com.example.booking.entity.Seat;
import com.example.booking.entity.Cinema;
import com.example.booking.dto.CinemaDistanceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@RestController
@RequestMapping("/api/browse")
public class BrowseController {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final CinemaRepository cinemaRepository;

    public BrowseController(MovieRepository movieRepository, ShowRepository showRepository, SeatRepository seatRepository, CinemaRepository cinemaRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieRepository.findAll());
    }

    @GetMapping("/movies/{movieId}/shows")
    public ResponseEntity<List<Show>> getShowsForMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showRepository.findByMovieId(movieId));
    }

    @GetMapping("/shows/{showId}/seats")
    public ResponseEntity<List<Seat>> getSeatsForShow(@PathVariable Long showId) {
        return ResponseEntity.ok(seatRepository.findByShowId(showId));
    }

    @GetMapping("/cinemas/nearby")
    public ResponseEntity<List<CinemaDistanceDTO>> getNearbyCinemas(@RequestParam double lat, @RequestParam double lon) {
        List<Cinema> cinemas = cinemaRepository.findAll();
        
        List<CinemaDistanceDTO> sortedCinemas = cinemas.stream()
            .map(cinema -> new CinemaDistanceDTO(cinema, calculateHaversineDistance(lat, lon, cinema.getLatitude(), cinema.getLongitude())))
            .sorted(Comparator.comparingDouble(CinemaDistanceDTO::getDistanceKm))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(sortedCinemas);
    }

    // Haversine formula to calculate great-circle distance between two points
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round((R * c) * 10.0) / 10.0; // Round to 1 decimal place
    }
}
