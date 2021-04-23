package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.exception.MovieExistsException;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;

import java.util.List;

public interface MovieService {

    List<MovieDto> getMovieList();
    void deleteMovie(String title);
    void createMovie(MovieDto movie) throws MovieExistsException;
}
