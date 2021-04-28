package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserByUsernameAndPassword(String username, String password) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Login failed due to incorrect credentials");
        }
        return convertEntityToDto(user.get());
    }

    protected UserDto convertEntityToDto(User user) {
        return new UserDto(user.getUsername(), user.getRole());
    }
}
