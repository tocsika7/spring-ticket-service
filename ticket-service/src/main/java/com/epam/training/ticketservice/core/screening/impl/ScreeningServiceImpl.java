package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.date.DateConverterService;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.exception.OverlappingScreeningException;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesntExistException;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningListDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final DateConverterService dateConverterService;

    public ScreeningServiceImpl(ScreeningRepository screeningRepository, MovieRepository movieRepository,
                                RoomRepository roomRepository, DateConverterService dateConverterService) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.dateConverterService = dateConverterService;
    }


    @Override
    public List<ScreeningListDto> listScreenings() {
        List<ScreeningListDto> screeningListDto = screeningRepository.findAll().stream().map(screening ->
                ScreeningListDto.builder()
                .room(screening.getRoom().getName())
                .movie(screening.getMovie())
                .startDate(dateConverterService.convertDateToString(screening.getStartDate()))
                .build()
                ).collect(Collectors.toList());
        if (screeningListDto.isEmpty()) {
            System.out.println("There are no screenings");
        }
        return screeningListDto;
    }

    @Override
    public void createScreening(ScreeningDto screeningDto) throws MovieDoesntExistException, RoomDoesntExistException,
            ParseException, OverlappingScreeningException {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovieTitle(), "Movie title cannot be null");
        Objects.requireNonNull(screeningDto.getRoomName(), "Room name cannot be null");
        Objects.requireNonNull(screeningDto.getStartDate(), "Start date cannot be null");
        Screening screening = Screening.builder()
                .movie(queryMovie(screeningDto.getMovieTitle()))
                .room(queryRoom(screeningDto.getRoomName()))
                .startDate(dateConverterService.convertStringToDate(screeningDto.getStartDate()))
                .build();
        checkOverlappingScreening(screening);
        screeningRepository.save(screening);
    }

    @Override
    public void deleteScreening(ScreeningDto screeningDto) throws MovieDoesntExistException, RoomDoesntExistException,
            ScreeningDoesntExistException, ParseException {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovieTitle(), "Movie title cannot be null");
        Objects.requireNonNull(screeningDto.getRoomName(), "Room name cannot be null");
        Objects.requireNonNull(screeningDto.getStartDate(), "Start date cannot be null");
        Screening screening = Screening.builder()
                .movie(queryMovie(screeningDto.getMovieTitle()))
                .room(queryRoom(screeningDto.getRoomName()))
                .startDate(dateConverterService.convertStringToDate(screeningDto.getStartDate()))
                .build();
        Optional<Screening> screeningToDelete = screeningRepository
                .findFirstByMovieAndRoomAndStartDate(screening.getMovie(), screening.getRoom(),
                        screening.getStartDate());
        if (screeningToDelete.isEmpty()) {
            throw new ScreeningDoesntExistException(screeningDto + " doesn't exist");
        }
        screeningRepository.delete(screeningToDelete.get());
    }

    protected Movie queryMovie(String title) throws MovieDoesntExistException {
        Optional<Movie> movie = movieRepository.findById(title);
        if (movie.isEmpty()) {
            throw new MovieDoesntExistException("Movie: " + title + " doesn't exist.");
        }
        return movie.get();
    }

    protected Room queryRoom(String name) throws RoomDoesntExistException {
        Optional<Room> room = roomRepository.findById(name);
        if (room.isEmpty()) {
            throw new RoomDoesntExistException("Room: " + name + " doesn't exist");
        }
        return room.get();
    }

    protected void checkOverlappingScreening(Screening screeningToAdd) throws OverlappingScreeningException {
        Date screeningToAddStart = screeningToAdd.getStartDate();
        Date screeningToAddEnd = DateUtils.addMinutes(screeningToAddStart,
                screeningToAdd.getMovie().getScreeningTime());
        List<Screening> screenings = screeningRepository.findAllByRoom(screeningToAdd.getRoom());
        if (!screenings.isEmpty()) {
            for (Screening screening : screenings) {
                Date screeningStart = screening.getStartDate();
                Date screeningEnd = DateUtils.addMinutes(screeningStart,
                        screening.getMovie().getScreeningTime());
                if ((screeningToAddStart.before(screeningEnd))
                        && (screeningToAddEnd.after(screeningStart))) {
                    throw new OverlappingScreeningException("There is an overlapping screening");
                }
                if ((screeningToAddStart.before(DateUtils.addMinutes(screeningEnd, 11)))
                        && (screeningToAddEnd.after(screeningStart))) {
                    throw new OverlappingScreeningException(
                            "This would start in the break period after another screening in this room");
                }
            }
        }
    }

}
