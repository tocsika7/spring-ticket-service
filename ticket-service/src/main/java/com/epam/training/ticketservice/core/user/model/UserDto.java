package com.epam.training.ticketservice.core.user.model;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Generated;

@Getter
@Generated
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {

    private final String username;
    private final User.Role role;

    @Override
    public String toString() {
        if (role == User.Role.ADMIN) {
            return String.format("privileged account '%s'", username);
        } else  {
            return String.format("account '%s'", username);
        }
    }
}
