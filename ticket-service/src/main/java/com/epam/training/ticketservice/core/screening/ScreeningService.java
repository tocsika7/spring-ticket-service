package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

public interface ScreeningService {

    void createScreening(ScreeningDto screeningDto) throws MovieDoesntExistException, RoomDoesntExistException;
}
