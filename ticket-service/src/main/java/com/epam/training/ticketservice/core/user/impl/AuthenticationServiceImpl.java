package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.user.AuthenticationService;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private UserDto loggedInUser = null;

    @Autowired
    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public AuthenticationServiceImpl(UserService userService, UserDto loggedInUser) {
        this.userService = userService;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public UserDto login(String username, String password) throws UserNotFoundException {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        loggedInUser = userService.getUserByUsernameAndPassword(username, password);
        return loggedInUser;
    }

    @Override
    public UserDto getLoggedUser() throws UserNotFoundException {
        if (loggedInUser == null) {
            throw new UserNotFoundException("You are not signed in");
        } else {
            return loggedInUser;
        }
    }

    @Override
    public String logout() {
        if (loggedInUser == null) {
            return "You are not signed in";
        } else {
            loggedInUser = null;
            return "Logout successful";
        }
    }
}
