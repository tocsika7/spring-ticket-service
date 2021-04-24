package com.epam.training.ticketservice.core.room.exception;

public class RoomDoesntExistException extends Exception{

    public RoomDoesntExistException(String message) {
        super(message);
    }
}
