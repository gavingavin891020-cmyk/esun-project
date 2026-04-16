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
}