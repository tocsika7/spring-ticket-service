package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ScreeningCommand {

    private final ScreeningService screeningService;

    public ScreeningCommand(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

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
        } catch (RoomDoesntExistException | MovieDoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }
}
