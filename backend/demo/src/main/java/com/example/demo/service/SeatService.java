package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 使用 Stored Procedure 更新座位 - 支持事務
    @Transactional
    public void updateSeat(String empId, Integer seatSeq) {
        // 輸入驗證 - 防止 SQL Injection
        if (empId == null || empId.trim().isEmpty() || empId.length() > 5) {
            throw new IllegalArgumentException("員工編號無效");
        }
        if (seatSeq == null || seatSeq <= 0) {
            throw new IllegalArgumentException("座位編號無效");
        }
        
        // 調用存儲程序 UpdateEmployeeSeat
        jdbcTemplate.update(
            "EXEC UpdateEmployeeSeat @EmpID = ?, @NewSeatSeq = ?",
            empId.trim(),
            seatSeq
        );
    }
}