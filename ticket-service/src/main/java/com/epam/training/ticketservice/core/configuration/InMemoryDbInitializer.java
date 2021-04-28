package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class InMemoryDbInitializer {

    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public InMemoryDbInitializer(MovieRepository movieRepository, RoomRepository roomRepository, UserRepository userRepository){
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Movie theGodfather = new Movie("The Godfather", "crime", 175);
        Movie lotr = new Movie("The Lord of the Rings: The Return of the King ", "adventure", 201);
        movieRepository.saveAll(List.of(lotr, theGodfather));

        Room room = new Room("VROOM", 10, 5);
        roomRepository.save(room);

        User admin = new User("admin", "admin", User.Role.ADMIN);
        User user = new User("user", "pw", User.Role.USER);
        userRepository.saveAll(List.of(user, admin));
    }
}
