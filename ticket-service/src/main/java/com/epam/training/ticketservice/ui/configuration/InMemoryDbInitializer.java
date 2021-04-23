package com.epam.training.ticketservice.ui.configuration;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class InMemoryDbInitializer {

    private MovieRepository movieRepository;

    public InMemoryDbInitializer(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    public void init() {
        Movie theGodfather = new Movie("The Godfather", "crime", 175);
        Movie lotr = new Movie("The Lord of the Rings: The Return of the King ", "adventure", 201);
        movieRepository.saveAll(List.of(lotr, theGodfather));
    }
}
