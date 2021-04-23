package com.epam.training.ticketservice.core.movie.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Movie {

    @Id
    @Column(unique = true)
    private String title;
    private String genre;
    private int screeningTime;


}
