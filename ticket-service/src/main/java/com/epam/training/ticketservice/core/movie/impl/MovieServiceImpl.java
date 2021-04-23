package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.movie.exception.MovieExistsException;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
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
        List<MovieDto> movies = movieRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
        if(movies.isEmpty()) {
            System.out.println("There are no movies at the moment");
        }
        return movies;
    }

    @Override
    public void deleteMovie(String title) throws MovieDoesntExistException {
        try {
        movieRepository.deleteById(title);
    } catch(EmptyResultDataAccessException e) {
            throw new MovieDoesntExistException(String.format("Movie: %s doesn't exist.", title));
        }
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

    @Override
    public void updateMovie(MovieDto movieDto) throws MovieDoesntExistException {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie title cannot be null");
        Objects.requireNonNull(movieDto.getGenre(), "Genre cannot be null");
        Optional<Movie> movieToUpdate = movieRepository.findById(movieDto.getTitle());
        if(movieToUpdate.isEmpty()) {
            throw new MovieDoesntExistException(String.format("Movie: %s doesn't exist.", movieDto.getTitle()));
        }
        movieRepository.save(new Movie(movieDto.getTitle(), movieDto.getGenre(), movieDto.getScreeningTime()));
    }

    private MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .screeningTime(movie.getScreeningTime())
                .build();
    }
}
