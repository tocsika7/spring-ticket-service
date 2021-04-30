package com.epam.training.ticketservice.core.movie.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Generated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Generated
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @Column(unique = true, name = "movie_title")
    private String title;

    @Column(name = "genre")
    private String genre;

    @Column(name = "screening_time")
    private int screeningTime;


}
