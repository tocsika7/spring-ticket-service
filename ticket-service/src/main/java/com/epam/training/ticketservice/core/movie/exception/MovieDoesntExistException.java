package com.epam.training.ticketservice.core.movie.exception;

public class MovieDoesntExistException extends Exception {

    public MovieDoesntExistException(String message) {
        super(message);
    }
}
