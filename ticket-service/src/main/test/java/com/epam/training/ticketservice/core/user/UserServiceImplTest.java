package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class UserServiceImplTest {

    private static final UserDto userDto = new UserDto("admin", User.Role.ADMIN);
    private static final User user = new User("admin", "admin", User.Role.ADMIN);

    private UserServiceImpl underTest;

    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        underTest = new UserServiceImpl(userRepository);
    }

    @Test
    public void testGetUserByUserNameAndPasswordShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.getUserByUsernameAndPassword(null, "admin"));

        // Then
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetUserByUserNameAndPasswordShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.getUserByUsernameAndPassword("admin", null));

        // Then
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetUserByUserNameAndPasswordShouldThrowUserNotFoundExceptionWhenGivenInvalidCredentials() {
        // Given
        Mockito.when(userRepository.findByUsernameAndPassword("user", "pw")).thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(UserNotFoundException.class, () -> underTest.getUserByUsernameAndPassword("user", "pw"));

        // Then
        Mockito.verify(userRepository).findByUsernameAndPassword("user", "pw");
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetUserByUserNameAndPasswordShouldReturnCorrectDtoWhenGivenValidCredentials() throws UserNotFoundException {
        // Given
        Mockito.when(userRepository.findByUsernameAndPassword("admin", "admin")).thenReturn(Optional.of(user));

        // When
        UserDto actual = underTest.getUserByUsernameAndPassword("admin", "admin");

        // Then
        Assertions.assertEquals(actual, userDto);
        Mockito.verify(userRepository).findByUsernameAndPassword("admin", "admin");
        Mockito.verifyNoMoreInteractions(userRepository);

    }


}
