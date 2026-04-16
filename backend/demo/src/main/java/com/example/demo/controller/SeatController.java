package com.example.demo.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Employee;
import com.example.demo.entity.SeatingChart;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.SeatingChartRepository;
import com.example.demo.service.SeatService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 允許前端跨域呼叫 (這對 Vue 開發很重要)
public class SeatController {

    @Autowired
    private EmployeeRepository empRepo;
    
    @Autowired
    private SeatingChartRepository seatRepo;

    @Autowired
    private SeatService seatService;

    // 1. 取得所有員工 (供下拉選單用)
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return empRepo.findAll();
    }

    // 2. 取得所有座位圖 (供畫出 1~4 樓格子用)
    @GetMapping("/seats")
    public List<SeatingChart> getSeats() {
        return seatRepo.findAll();
    }

    // 3. 執行換座位動作
    @PostMapping("/update-seat")
    public String update(@RequestBody Map<String, Object> payload) {
        String empId = (String) payload.get("empId");
        Integer seatSeq = (Integer) payload.get("seatSeq");
        
        seatService.updateSeat(empId, seatSeq);
        return "更新成功";
    }
    
    // 3.1 執行換座位動作 (前端版本) - 帶輸入驗證
    @PostMapping("/update-seat1")
    public Map<String, String> updateSeat1(@RequestBody Map<String, Object> payload) {
        try {
            // 輸入驗證 - 防止 XSS 和 SQL Injection
            Object empIdObj = payload.get("empId");
            Object seatSeqObj = payload.get("seatSeq");
            
            if (empIdObj == null || !(empIdObj instanceof String)) {
                return Map.of("status", "error", "message", "員工編號格式錯誤");
            }
            
            if (seatSeqObj == null || !(seatSeqObj instanceof Number)) {
                return Map.of("status", "error", "message", "座位編號格式錯誤");
            }
            
            String empId = ((String) empIdObj).trim();
            Integer seatSeq = ((Number) seatSeqObj).intValue();
            
            // 驗證長度
            if (empId.isEmpty() || empId.length() > 5) {
                return Map.of("status", "error", "message", "員工編號長度無效（最多5碼）");
            }
            
            // 驗證只包含數字和字母
            if (!empId.matches("^[A-Za-z0-9]*$")) {
                return Map.of("status", "error", "message", "員工編號只能包含字母和數字");
            }
            
            if (seatSeq <= 0) {
                return Map.of("status", "error", "message", "座位編號無效");
            }
            
            // 調用服務更新座位
            seatService.updateSeat(empId, seatSeq);
            return Map.of("status", "success", "message", "座位更新成功");
        } catch (IllegalArgumentException e) {
            return Map.of("status", "error", "message", "輸入驗證失敗: " + e.getMessage());
        } catch (Exception e) {
            return Map.of("status", "error", "message", "座位更新失敗: " + e.getMessage());
        }
    }
}