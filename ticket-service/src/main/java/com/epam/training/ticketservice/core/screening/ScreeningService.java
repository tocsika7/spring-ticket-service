package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningListDto;

import java.util.List;

public interface ScreeningService {

    List<ScreeningListDto> listScreenings();
    void createScreening(ScreeningDto screeningDto) throws MovieDoesntExistException, RoomDoesntExistException;
}
