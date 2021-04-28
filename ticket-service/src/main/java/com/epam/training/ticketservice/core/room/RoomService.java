package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.room.model.RoomDto;

import java.util.List;

public interface RoomService {

    List<RoomDto> getRoomList();

    void createRoom(RoomDto roomDto) throws RoomAlreadyExistsException;

    void updateRoom(RoomDto roomDto) throws RoomDoesntExistException;

    void deleteRoom(String name) throws RoomDoesntExistException;
}
