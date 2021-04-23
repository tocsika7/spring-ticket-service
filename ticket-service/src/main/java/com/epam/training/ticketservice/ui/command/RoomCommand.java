package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
public class RoomCommand {

    private final RoomService roomService;

    public RoomCommand(RoomService roomService) {
        this.roomService = roomService;
    }

    @ShellMethod(value = "List All Available Rooms", key = "list rooms")
    public List<RoomDto> listRooms() {return roomService.getRoomList();}

    @ShellMethod(value = "Create new Room", key = "create room")
    public void createRoom(String name, int rows, int columns) {
        RoomDto roomDto = RoomDto.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        try {
            roomService.createRoom(roomDto);
            System.out.println(roomDto);
        } catch (RoomAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }
}