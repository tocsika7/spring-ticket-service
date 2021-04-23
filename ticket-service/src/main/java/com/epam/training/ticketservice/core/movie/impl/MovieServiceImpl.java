package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.exception.MovieExistsException;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteMovie(String title) {
        movieRepository.deleteById(title);
    }

    @Override
    public void createMovie(MovieDto movie) throws MovieExistsException {
        Objects.requireNonNull(movie, "Movie cannot be null");
        Objects.requireNonNull(movie.getTitle(), "Movie title cannot be null");
        Objects.requireNonNull(movie.getGenre(), "Genre cannot be null");
        Optional<Movie> movieToCreate = movieRepository.findById(movie.getTitle());
        if(movieToCreate.isPresent()) {
            throw new MovieExistsException("The movie " + movie.getTitle() + " already exists");
        }
        Movie newMovie = new Movie(movie.getTitle(), movie.getGenre(), movie.getScreeningTime());
        movieRepository.save(newMovie);
    }

    private MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .screeningTime(movie.getScreeningTime())
                .build();
    }
}
