package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.impl.AuthenticationServiceImpl;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AuthenticationServiceImplTest {

    private static final UserDto userDto = new UserDto("admin", User.Role.USER);
    private static final User user = new User("admin", "admin", User.Role.ADMIN);

    private AuthenticationServiceImpl underTestLoggedIn;

    private AuthenticationServiceImpl underTestLoggedOut;

    private UserService userService;

    @BeforeEach
    public void init() {
        userService = Mockito.mock(UserService.class);
        underTestLoggedIn = new AuthenticationServiceImpl(userService, userDto);
        underTestLoggedOut = new AuthenticationServiceImpl(userService);
    }

    @Test
    public void testLoginShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTestLoggedOut.login(null, "admin"));

        // Then
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testLoginShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTestLoggedOut.login("admin", null));

        // Then
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testLoginShouldReturnLoggedInUserDtoWhenGivenValidCredentials() throws UserNotFoundException {
        // Given
        Mockito.when(userService.getUserByUsernameAndPassword("admin", "admin")).thenReturn(userDto);

        // When
        UserDto expected = underTestLoggedOut.login("admin", "admin");

        // Then
        Assertions.assertEquals(expected, userDto);
        Mockito.verify(userService).getUserByUsernameAndPassword("admin", "admin");
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testLoginShouldThrowUserNotFoundExceptionWhenGivenWrongCredentials() throws UserNotFoundException {
        // Given
        Mockito.doThrow(UserNotFoundException.class).when(userService).getUserByUsernameAndPassword("admin", "admin");

        // When
        Assertions.assertThrows(UserNotFoundException.class, () -> underTestLoggedOut.login("admin", "admin"));

        // Then
        Mockito.verify(userService).getUserByUsernameAndPassword("admin", "admin");
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetLoggedUserShouldReturnValidUseDtoWhenUserIsLoggedIn() throws UserNotFoundException {
        // Given

        // When
        UserDto actual = underTestLoggedIn.getLoggedUser();

        // Then
        Assertions.assertEquals(userDto, actual);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetLoggedUserShouldThrowUserNotFoundExceptionWhenUserIsNotLoggedIn() {
        // Given

        // When
        Assertions.assertThrows(UserNotFoundException.class, () -> underTestLoggedOut.getLoggedUser());

        // Then
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void logoutShouldReturnLogoutSuccessfulWhenUserIsLoggedIn() throws UserNotFoundException {
        // Given
        String expected = "Logout successful";

        // When
        String actual = underTestLoggedIn.logout();

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void logoutShouldReturnYouAreNotSignedInWhenUserIsNotLoggedIn() {
        // Given
        String expected = "You are not signed in";

        // When
        String actual = underTestLoggedOut.logout();

        // Then
        Assertions.assertEquals(expected,actual);
    }
}
