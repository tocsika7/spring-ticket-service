package com.epam.training.ticketservice.core.screening.exception;

public class ScreeningDoesntExistException extends Exception {

    public ScreeningDoesntExistException(String message) {
        super(message);
    }
}
