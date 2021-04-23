package com.epam.training.ticketservice.core.room.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class RoomDto {

    private final String name;
    private final int rows;
    private final int columns;

    @Override
    public String toString() {
        return String.format("Room %s with %d seats, %d rows and %d columns", name, rows * columns, rows, columns);
    }
}
