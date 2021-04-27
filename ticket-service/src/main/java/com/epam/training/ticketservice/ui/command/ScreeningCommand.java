package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesntExistException;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningListDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Date;
import java.util.List;

@ShellComponent
public class ScreeningCommand {

    private final ScreeningService screeningService;

    public ScreeningCommand(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @ShellMethod(value = "Create a new screening", key = "create screening")
    public void createScreening(String movieTitle, String roomName, Date startDate) {
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDate(startDate)
                .build();
        try {
            screeningService.createScreening(screeningDto);
            System.out.println("Screening created: " + screeningDto);
        } catch (RoomDoesntExistException | MovieDoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod(value = "Listing all available screenings", key = "list screenings")
    public List<ScreeningListDto> listScreenings() {
        return screeningService.listScreenings();
    }

    @ShellMethod(value = "Delete screening by all attributes", key = "delete screening")
    public void deleteScreening(String movieTitle, String roomName, Date startDate) {
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle(movieTitle)
                .startDate(startDate)
                .roomName(roomName)
                .build();
        try {
            screeningService.deleteScreening(screeningDto);
            System.out.println("Screening deleted: " + screeningDto);
        } catch (RoomDoesntExistException | MovieDoesntExistException | ScreeningDoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }
}
