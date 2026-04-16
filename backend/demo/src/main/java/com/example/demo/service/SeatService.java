package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate; // 用來執行 Stored Procedure 的工具

    // 呼叫資料庫裡的 SP 來更新座位
    public void updateSeat(String empId, Integer seatSeq) {
        // 執行我們之前在 SQL Server 建的那個 PROCEDURE
        jdbcTemplate.update("EXEC UpdateEmployeeSeat ?, ?", empId, seatSeq);
    }
}