package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;

import java.util.Date;

@Generated
@Builder
@EqualsAndHashCode
@Getter
public class ScreeningDto {

    private final String roomName;
    private final String movieTitle;
    private final String startDate;

    @Override
    public String toString() {
        return String.format("Screening: %s in %s at %s", movieTitle, roomName, startDate);
    }
}
