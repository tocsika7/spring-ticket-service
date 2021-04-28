package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.movie.exception.MovieExistsException;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.AuthenticationService;
import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class MovieCommand {

    private final MovieService movieService;
    private final AuthenticationService authenticationService;

    public MovieCommand(MovieService movieService, AuthenticationService authenticationService) {
        this.movieService = movieService;
        this.authenticationService = authenticationService;
    }

    @ShellMethod(value = "List All Available Movies", key = "list movies")
    public List<MovieDto> listMovies() {
        return movieService.getMovieList();
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(value = "Delete a movie by title", key = "delete movie")
    public void deleteMovie(String title) {
        try {
            movieService.deleteMovie(title);
        } catch (MovieDoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethodAvailability("isAvailable")
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

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(value = "Create new movie", key = "update movie")
    public String updateMovie(String title, String genre, int screeningTime) {
        MovieDto movieDto = MovieDto.builder()
                .title(title)
                .genre(genre)
                .screeningTime(screeningTime)
                .build();
        try {
            movieService.updateMovie(movieDto);
        } catch (MovieDoesntExistException e) {
            return e.getMessage();
        }
        return "Movie updated: " + movieDto;
    }

    private Availability isAvailable() {
        try {
            UserDto userDto = authenticationService.getLoggedUser();
            if (userDto.getRole() == User.Role.ADMIN) {
                return Availability.available();
            } else {
                return Availability.unavailable("You are not an admin user");
            }
        } catch (UserNotFoundException e) {
            return Availability.unavailable(e.getMessage());
        }
    }



}
