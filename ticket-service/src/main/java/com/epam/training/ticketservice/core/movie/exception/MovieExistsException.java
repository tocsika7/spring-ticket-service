package com.epam.training.ticketservice.core.movie.exception;

public class MovieExistsException extends Exception {

    public MovieExistsException(String message) {
        super(message);
    }
}
