package com.epam.training.ticketservice.core.screening.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int screeningId;

    @ManyToOne
    @JoinColumn(name = "room_name")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "movie_title")
    private Movie movie;

    @Column(unique = true, name = "start_date")
    private Date startDate;

}
