package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.exception.MovieExistsException;
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

    @ShellMethod(value = "Delete a movie by title", key = "delete movie")
    public void deleteMovie(String title) {movieService.deleteMovie(title);}

    @ShellMethod(value = "Create new movie", key = "create movie")
    public String createMovie(String title, String genre, int screeningTime) {
        MovieDto movieDto = MovieDto.builder()
                .title(title)
                .genre(genre)
                .screeningTime(screeningTime)
                .build();
        try {
            movieService.createMovie(movieDto);
        } catch (MovieExistsException e) {
            return e.getMessage();
        }
        return "Movie created: " + movieDto;
    }
}
