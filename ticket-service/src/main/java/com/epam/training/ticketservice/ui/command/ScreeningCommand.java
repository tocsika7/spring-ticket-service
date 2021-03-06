package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.exception.OverlappingScreeningException;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesntExistException;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningListDto;
import com.epam.training.ticketservice.core.user.AuthenticationService;
import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.text.ParseException;
import java.util.List;

@ShellComponent
public class ScreeningCommand {

    private final ScreeningService screeningService;
    private final AuthenticationService authenticationService;

    public ScreeningCommand(ScreeningService screeningService, AuthenticationService authenticationService) {
        this.screeningService = screeningService;
        this.authenticationService = authenticationService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(value = "Create a new screening", key = "create screening")
    public void createScreening(String movieTitle, String roomName, String startDate) {
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDate(startDate)
                .build();
        try {
            screeningService.createScreening(screeningDto);
            System.out.println("Screening created: " + screeningDto);
        } catch (RoomDoesntExistException | MovieDoesntExistException
                | ParseException | OverlappingScreeningException e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod(value = "Listing all available screenings", key = "list screenings")
    public List<ScreeningListDto> listScreenings() {
        return screeningService.listScreenings();
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(value = "Delete screening by all attributes", key = "delete screening")
    public void deleteScreening(String movieTitle, String roomName, String startDate) {
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle(movieTitle)
                .startDate(startDate)
                .roomName(roomName)
                .build();
        try {
            screeningService.deleteScreening(screeningDto);
            System.out.println("Screening deleted: " + screeningDto);
        } catch (RoomDoesntExistException | MovieDoesntExistException
                | ScreeningDoesntExistException | ParseException e) {
            System.out.println(e.getMessage());
        }
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
