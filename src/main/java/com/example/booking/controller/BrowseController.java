package com.example.booking.controller;

import com.example.booking.entity.Movie;
import com.example.booking.entity.Show;
import com.example.booking.repository.MovieRepository;
import com.example.booking.repository.ShowRepository;
import com.example.booking.repository.SeatRepository;
import com.example.booking.entity.Seat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/browse")
public class BrowseController {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    public BrowseController(MovieRepository movieRepository, ShowRepository showRepository, SeatRepository seatRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
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
}
