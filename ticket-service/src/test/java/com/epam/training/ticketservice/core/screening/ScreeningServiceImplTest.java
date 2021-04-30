package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.date.DateConverterService;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.exception.OverlappingScreeningException;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningListDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;


public class ScreeningServiceImplTest {

    @Deprecated
    private final static Date date = new Date(2021, 03, 14);

    private static final Screening screening = Screening.builder()
            .room(new Room("Room", 10,10))
            .movie(new Movie("Shrek", "comedy", 60))
            .startDate(date)
            .build();

    private static final Movie movie = new Movie("Shrek", "comedy", 90);

    private static final ScreeningListDto screeningListDto = ScreeningListDto.builder()
            .movie(new Movie("Shrek", "comedy", 60))
            .room("Room")
            .startDate("2021-03-15 16:00")
            .build();

    private ScreeningServiceImpl underTest;

    private ScreeningRepository screeningRepository;

    private MovieRepository movieRepository;

    private RoomRepository roomRepository;

    private DateConverterService dateConverterService;

    @BeforeEach
    public void init() {
        screeningRepository = Mockito.mock(ScreeningRepository.class);
        movieRepository = Mockito.mock(MovieRepository.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        underTest = new ScreeningServiceImpl(screeningRepository, movieRepository, roomRepository, dateConverterService);
    }

    @Test
    public void testCreateScreeningShouldThrowNullPointerExceptionWhenDtoIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createScreening(null));

        // Then
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowNullPointerExceptionWhenMovieTitle() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle(null)
                .roomName("Room")
                .startDate("2021-03-15 16:00")
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));

        // Then
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowNullPointerExceptionWhenRoomIsNull() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("Shrek")
                .roomName(null)
                .startDate("2021-03-15 16:00")
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));

        // Then
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowNullPointerExceptionWhenDateIsNull() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("Shrek")
                .roomName("Room")
                .startDate(null)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));

        // Then
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowMovieDoesntExistExceptionWhenGivenMovieThatDoesntExist() throws OverlappingScreeningException, RoomDoesntExistException, MovieDoesntExistException, ParseException {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("Shrek")
                .roomName("Room")
                .startDate("2021-03-15 16:00")
                .build();
        Mockito.when(movieRepository.findById("Shrek")).thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(MovieDoesntExistException.class, () -> underTest.createScreening(screeningDto));

        // Then
        Mockito.verifyNoMoreInteractions(screeningRepository);
        Mockito.verify(movieRepository).findById("Shrek");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowRoomDoesntExistExceptionWhenGivenRoomThatDoesntExist() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .movieTitle("Shrek")
                .roomName("Room")
                .startDate("2021-03-15 16:00")
                .build();
        Mockito.when(roomRepository.findById("Room")).thenReturn(Optional.empty());
        Mockito.when(movieRepository.findById("Shrek")).thenReturn(Optional.of(movie));

        // When
        Assertions.assertThrows(RoomDoesntExistException.class, () -> underTest.createScreening(screeningDto));

        // Then
        Mockito.verifyNoMoreInteractions(screeningRepository);
        Mockito.verify(roomRepository).findById("Room");
        Mockito.verifyNoMoreInteractions(roomRepository);
        Mockito.verify(movieRepository).findById("Shrek");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }



}
