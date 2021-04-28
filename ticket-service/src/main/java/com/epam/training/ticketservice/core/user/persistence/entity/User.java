package com.epam.training.ticketservice.core.user.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
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

    public User(String username, String password, User.Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
