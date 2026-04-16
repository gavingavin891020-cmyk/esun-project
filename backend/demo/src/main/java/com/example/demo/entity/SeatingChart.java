package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SeatingChart")
@Data
public class SeatingChart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FLOOR_SEAT_SEQ")
    private Integer floorSeatSeq;

    @Column(name = "FLOOR_NO", nullable = false)
    private Integer floorNo;

    @Column(name = "SEAT_NO", nullable = false)
    private Integer seatNo;
}