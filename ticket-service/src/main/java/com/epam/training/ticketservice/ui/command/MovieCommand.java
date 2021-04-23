package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
public class MovieCommand {

    private final MovieService movieService;

    public MovieCommand(MovieService movieService) {
        this.movieService = movieService;
    }

    @ShellMethod(value = "List All Available Movies", key = "list movies")
    public List<MovieDto> listMovies() {
        return movieService.getMovieList();
    }
}
