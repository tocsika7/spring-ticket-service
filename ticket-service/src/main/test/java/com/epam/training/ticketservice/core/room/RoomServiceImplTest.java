package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

public class RoomServiceImplTest {

    private static final Room room = new Room("Room 1", 10, 10);
    private static final Room room2 = new Room("Room 2", 12, 12);
    private static final RoomDto roomDto = RoomDto.builder()
            .name("Room 1")
            .rows(10)
            .columns(10)
            .build();
    private static final RoomDto roomDto2 = RoomDto.builder()
            .name("Room 2")
            .rows(12)
            .columns(12)
            .build();

    private RoomServiceImpl underTest;

    private RoomRepository roomRepository;

    @BeforeEach
    public void init() {
        roomRepository = Mockito.mock(RoomRepository.class);
        underTest = new RoomServiceImpl(roomRepository);
    }

    @Test
    public void testGetRoomListShouldCallMovieRoomRepositoryAndReturnADtoList() {
        // Given
        Mockito.when(roomRepository.findAll()).thenReturn(List.of(room, room2));
        List<RoomDto> expected = List.of(roomDto, roomDto2);

        // When
        List<RoomDto> actual  = underTest.getRoomList();

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(roomRepository).findAll();
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenDtoIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createRoom(null));

        /// Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenRoomNameIsNull() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name(null)
                .columns(10)
                .rows(10)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createRoom(roomDto));

        /// Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testCreateRoomShouldThrowRoomExistsExceptionWhenCreatingRoomThatAlreadyExists() {
        // Given
        Mockito.when(roomRepository.findById(roomDto.getName())).thenReturn(java.util.Optional.of(room));

        // When
        Assertions.assertThrows(RoomAlreadyExistsException.class, () -> underTest.createRoom(roomDto));

        // Then
        Mockito.verify(roomRepository, times(0)).save(room);
    }

    @Test
    public void testCreateRoomShouldSaveRoomWithValidRoomDto() throws RoomAlreadyExistsException {
        // Given
        Mockito.when(roomRepository.findById(roomDto.getName())).thenReturn(Optional.empty());

        // When
        underTest.createRoom(roomDto);

        // Then
        Mockito.verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testUpdateRoomShouldThrowNullPointerExceptionWhenDtoIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.updateRoom(null));

        /// Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testUpdateRoomShouldThrowNullPointerExceptionWhenRoomNameIsNull() {
        // Given
        RoomDto roomDto = RoomDto.builder()
                .name(null)
                .rows(10)
                .columns(10)
                .build();

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.updateRoom(roomDto));

        // Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testUpdateRoomShouldThrowRoomDoesntExistExceptionWhenTryingToUpdateANonExistentRoom() {
        // Given
        Mockito.when(roomRepository.findById(roomDto.getName())).thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(RoomDoesntExistException.class, () -> underTest.updateRoom(roomDto));

        // Then
        Mockito.verify(roomRepository, times(0)).save(room);
    }

    @Test
    public void testUpdateShouldUpdateRoomWhenDtoIsValid() throws RoomDoesntExistException {
        // Given
        Mockito.when(roomRepository.findById(roomDto.getName())).thenReturn(Optional.of(room));

        // When
        underTest.updateRoom(roomDto);

        // Then
        Mockito.verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void testDeleteRoomShouldThrowNullPointerExceptionWhenRoomNameIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.deleteRoom(null));

        // Then
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testDeleteRoomShouldReturnRoomDoesntExistExceptionWhenRoomDoesntExist() {
        // Given
        doThrow(EmptyResultDataAccessException.class).when(roomRepository).deleteById(roomDto.getName());

        // When
        Assertions.assertThrows(RoomDoesntExistException.class, () -> underTest.deleteRoom(roomDto.getName()));

        // Then
        Mockito.verify(roomRepository, times(1)).deleteById(roomDto.getName());
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    public void testDeleteRoomShouldDeleteRoomWhenRoomNameIsValid() throws RoomDoesntExistException {
        // Given

        // When
        underTest.deleteRoom(roomDto.getName());

        // Then
        Mockito.verify(roomRepository).deleteById(roomDto.getName());
        Mockito.verifyNoMoreInteractions(roomRepository);
    }


}
