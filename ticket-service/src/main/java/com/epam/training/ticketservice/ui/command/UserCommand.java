package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.AuthenticationService;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UserCommand {

    private final AuthenticationService authenticationService;

    public UserCommand(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ShellMethod(value = "Login as admin", key = "sign in privileged")
    public String loginUser(String username, String password) {
        try {
            authenticationService.login(username, password);
            UserDto user = authenticationService.getLoggedUser();
            return "Signed in with " + user;
        } catch (UserNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Describe currently logged in user", key = "describe account")
    public String describeUser() {
        try {
            UserDto userDto = authenticationService.getLoggedUser();
            return "Signed in with " + userDto;
        } catch (UserNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Log out from current account", key = "sign out")
    public String logout() {
        return authenticationService.logout();
    }
}
