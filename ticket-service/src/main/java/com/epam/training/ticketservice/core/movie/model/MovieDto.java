package com.epam.training.ticketservice.core.movie.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
public class MovieDto {

    private final String title;
    private final String genre;
    private final int screeningTime;

    @Override
    public String toString() {
        return String.format("%s (%s, %d)",title,genre,screeningTime);
    }
}
