package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
@Generated
public class ScreeningListDto {

    private final Movie movie;
    private final String room;
    private final String startDate;

    @Override
    public String toString() {
        return String.format("%s (%s, %d minutes), screened in room %s, at %s",
                movie.getTitle(),
                movie.getGenre(),
                movie.getScreeningTime(),
                room,
                startDate
        );
    }
}
