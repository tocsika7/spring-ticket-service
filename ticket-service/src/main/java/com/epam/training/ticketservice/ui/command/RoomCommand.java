package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
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
}
