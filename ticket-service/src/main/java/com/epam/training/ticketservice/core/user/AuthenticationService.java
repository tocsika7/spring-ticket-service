package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.model.UserDto;

import java.util.Optional;

public interface AuthenticationService {

    UserDto login(String username, String password) throws UserNotFoundException;

    UserDto getLoggedUser() throws UserNotFoundException;

    String logout();
}
