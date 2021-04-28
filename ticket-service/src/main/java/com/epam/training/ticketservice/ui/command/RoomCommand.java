package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.exception.RoomAlreadyExistsException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesntExistException;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.AuthenticationService;
import com.epam.training.ticketservice.core.user.exception.UserNotFoundException;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class RoomCommand {

    private final RoomService roomService;
    private final AuthenticationService authenticationService;

    public RoomCommand(RoomService roomService, AuthenticationService authenticationService) {
        this.roomService = roomService;
        this.authenticationService = authenticationService;
    }

    @ShellMethod(value = "List All Available Rooms", key = "list rooms")
    public List<RoomDto> listRooms() {
        return roomService.getRoomList();
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(value = "Create new Room", key = "create room")
    public void createRoom(String name, int rows, int columns) {
        RoomDto roomDto = RoomDto.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        try {
            roomService.createRoom(roomDto);
            System.out.println("Room Created: " + roomDto);
        } catch (RoomAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(value = "Update room", key = "update room")
    public void updateRoom(String name, int rows, int columns) {
        RoomDto roomDto = RoomDto.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        try {
            roomService.updateRoom(roomDto);
            System.out.println("Room Updated: " + roomDto);
        } catch (RoomDoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(value = "Delete room by name", key = "delete room")
    public void deleteRoom(String name) {
        try {
            roomService.deleteRoom(name);
            System.out.printf("Room %s deleted%n", name);
        } catch (RoomDoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    private Availability isAvailable() {
        try {
            UserDto userDto = authenticationService.getLoggedUser();
            if (userDto.getRole() == User.Role.ADMIN) {
                return Availability.available();
            } else {
                return Availability.unavailable("You are not an admin user");
            }
        } catch (UserNotFoundException e) {
            return Availability.unavailable(e.getMessage());
        }
    }
}
