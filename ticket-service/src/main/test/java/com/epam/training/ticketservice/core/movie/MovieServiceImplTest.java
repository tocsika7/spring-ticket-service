package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesntExistException;
import com.epam.training.ticketservice.core.movie.exception.MovieExistsException;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

public class MovieServiceImplTest {

    private static final Movie shrek = new Movie("Shrek", "comedy", 90);
    private static final Movie shrek2 = new Movie("Shrek 2", "comedy", 93);
    private static final MovieDto shrekDto = MovieDto.builder()
            .title("Shrek")
            .genre("comedy")
            .screeningTime(90)
            .build();
    private static final MovieDto shrek2Dto = MovieDto.builder()
            .title("Shrek 2")
            .genre("comedy")
            .screeningTime(93)
            .build();


    private MovieServiceImpl underTest;

    private MovieRepository movieRepository;

    @BeforeEach
    public void init() {
        movieRepository = Mockito.mock(MovieRepository.class);
        underTest = new MovieServiceImpl(movieRepository);
    }

    @Test
    public void testGetMovieListShouldCallMovieRepositoryAndReturnADtoList() {
        // Given
        Mockito.when(movieRepository.findAll()).thenReturn(List.of(shrek, shrek2));
        List<MovieDto> expected = List.of(shrekDto, shrek2Dto);

        // When
        List<MovieDto> actual = underTest.getMovieList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(movieRepository).findAll();
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testDeleteMovieShouldCallRepositoryAndDeleteTheMovie() throws MovieDoesntExistException {

        // When
        underTest.deleteMovie("Shrek");

        // Then
        Mockito.verify(movieRepository).deleteById("Shrek");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test()
    public void testDeleteMovieShouldThrowMovieDoesntExistException() {
        // Given
        doThrow(EmptyResultDataAccessException.class).when(movieRepository).deleteById(anyString());

        // When
        Assertions.assertThrows(MovieDoesntExistException.class, () -> {
            underTest.deleteMovie("Shrek 3");
        });

        // Then
        Mockito.verify(movieRepository).deleteById("Shrek 3");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieDtoIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createMovie(null));

        //Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenTitleIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title(null)
                .genre("comedy")
                .screeningTime(120)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createMovie(movieDto));

        //Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenGenreIsNull() {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("Shrek 3")
                .genre(null)
                .screeningTime(93)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createMovie(movieDto));

        //Then
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void testCreateMovieShouldThrowMovieExistsExceptionWhenTryingToCreateAlreadyExistingMovie() {
        // Given
        Mockito.when(movieRepository.findById(anyString())).thenReturn(java.util.Optional.of(shrek));

        // When
        Assertions.assertThrows(MovieExistsException.class, () -> underTest.createMovie(shrekDto));

        // Then
        Mockito.verify(movieRepository, times(0)).save(any());
    }

    @Test
    public void testCreateMovieShouldCallTheRepositoryAndThenSaveTheMovie() throws MovieExistsException {
        // Given
        Mockito.when(movieRepository.save(shrek)).thenReturn(shrek);
        Mockito.when(movieRepository.findById("Shrek")).thenReturn(Optional.empty());

        // When
        underTest.createMovie(shrekDto);

        // Then
        Mockito.verify(movieRepository, times(1)).save(shrek);
    }

    @Test
    public void testUpdateMovieShouldThrowMovieDoesntExistException() {
        // Given
        Mockito.when(movieRepository.findById(shrekDto.getTitle())).thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(MovieDoesntExistException.class, ()-> underTest.updateMovie(shrekDto));

        // Then
        Mockito.verify(movieRepository, times(0)).save(any());

    }

    @Test
    public void testUpdateMovieShouldSaveUpdatedMovieWithValidMovieDto() throws MovieDoesntExistException {
        // Given
        MovieDto movieDto = MovieDto.builder()
                .title("Shrek")
                .genre("drama")
                .screeningTime(200)
                .build();
        Movie movie = new Movie("Shrek", "drama", 200);
        Mockito.when(movieRepository.findById(movieDto.getTitle())).thenReturn(Optional.of(shrek));

        // When
        underTest.updateMovie(movieDto);

        // Then
        Mockito.verify(movieRepository).save(movie);
    }


}
