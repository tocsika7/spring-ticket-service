package com.epam.training.ticketservice.core.user.persistence.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

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
