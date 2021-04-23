package com.epam.training.ticketservice.core.movie.persistence.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Movie {

    @Id
    @Column(unique = true)
    private String title;
    private String genre;
    private int screeningTime;


}
