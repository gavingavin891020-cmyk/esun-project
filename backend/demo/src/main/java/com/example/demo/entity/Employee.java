package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Employee")
@Data
public class Employee {
    @Id
    @Column(name = "EMP_ID", length = 5)
    private String empId;

    @Column(name = "EMP_NAME")
    private String empName;

    // 這裡記錄該員工目前坐哪個位置的序號 (對應 SeatingChart 的 FLOOR_SEAT_SEQ)
    @Column(name = "FLOOR_SEAT_SEQ")
    private Integer floorSeatSeq;
}   