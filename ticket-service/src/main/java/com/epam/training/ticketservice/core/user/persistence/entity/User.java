package com.epam.training.ticketservice.core.user.persistence.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public static enum Role {
        ADMIN, USER
    }
}
