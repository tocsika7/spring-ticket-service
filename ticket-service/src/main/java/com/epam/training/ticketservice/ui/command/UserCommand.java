package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.AuthenticationService;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.model.UserDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UserCommand {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserCommand(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @ShellMethod(value = "Login as admin", key = "sign in privileged")
    public String loginUser(String username, String password) {
        try {
            authenticationService.login(username, password);
            return "Login successful";
        } catch (UserNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Describe currently logged in user", key = "describe user")
    public String describeUser() {
        try {
            UserDto userDto = authenticationService.getLoggedUser();
            return "Signed in with " + userDto;
        } catch (UserNotFoundException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Log out from current account", key = "logout")
    public String logout() {
        return authenticationService.logout();
    }
}
