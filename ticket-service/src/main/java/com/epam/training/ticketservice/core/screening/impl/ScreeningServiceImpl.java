package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public ScreeningServiceImpl(ScreeningRepository screeningRepository, MovieRepository movieRepository, RoomRepository roomRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }


    @Override
    public void createScreening(ScreeningDto screeningDto) throws MovieDoesntExistException, RoomDoesntExistException {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovieTitle(), "Movie title cannot be null");
        Objects.requireNonNull(screeningDto.getRoomName(), "Room name cannot be null");
        Objects.requireNonNull(screeningDto.getStartDate(), "Start date cannot be null");
        Screening screening = Screening.builder()
                .movie(queryMovie(screeningDto.getMovieTitle()))
                .room(queryRoom(screeningDto.getRoomName()))
                .startDate(screeningDto.getStartDate())
                .build();
        screeningRepository.save(screening);
    }

    protected Movie queryMovie(String title) throws MovieDoesntExistException {
        Optional<Movie> movie = movieRepository.findById(title);
        if(movie.isEmpty()) {
            throw new MovieDoesntExistException("Movie: " + title + " doesn't exist.");
        }
        return movie.get();
    }

    protected Room queryRoom(String name) throws RoomDoesntExistException {
        Optional<Room> room = roomRepository.findById(name);
        if(room.isEmpty()) {
            throw new RoomDoesntExistException("Room: " + name + " doesn't exist");
        }
        return room.get();
    }
}
