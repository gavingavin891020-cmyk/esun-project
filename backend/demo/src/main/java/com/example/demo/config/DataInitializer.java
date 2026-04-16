package com.example.demo.config;

import com.example.demo.entity.Employee;
import com.example.demo.entity.SeatingChart;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.SeatingChartRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final SeatingChartRepository seatingChartRepository;
    private final EmployeeRepository employeeRepository;
    private final JdbcTemplate jdbcTemplate;
    
    public DataInitializer(SeatingChartRepository seatingChartRepository, 
                          EmployeeRepository employeeRepository,
                          JdbcTemplate jdbcTemplate) {
        this.seatingChartRepository = seatingChartRepository;
        this.employeeRepository = employeeRepository;
        this.jdbcTemplate = jdbcTemplate;   
    }
    
    @Override
    public void run(String... args) throws Exception {
        // 1. 首先確保存儲程序存在（無論數據是否存在）
        createStoredProcedure();
        
        // 2. 檢查是否已有數據，避免重複插入
        if (seatingChartRepository.count() > 0) {
            System.out.println("✓ 數據已存在，跳過數據初始化");
            return;
        }
        
        System.out.println("開始初始化座位數據...");
        
        // 新增 1 到 4 樓的座位 (每層 4 個)
        for (int floor = 1; floor <= 4; floor++) {
            for (int seat = 1; seat <= 4; seat++) {
                SeatingChart chart = new SeatingChart();
                chart.setFloorNo(floor);
                chart.setSeatNo(seat);
                seatingChartRepository.save(chart);
            }
        }
        
        System.out.println("✓ 座位數據初始化完成");
        System.out.println("開始初始化員工數據...");
        
        // 新增員工名單
        String[][] employees = {
            {"10001", "王小明"},
            {"10002", "李小華"},
            {"10003", "陳小春"},
            {"10004", "林大明"},
            {"10005", "吳美麗"}
        };
        
        for (String[] empData : employees) {
            Employee employee = new Employee();
            employee.setEmpId(empData[0]);
            employee.setEmpName(empData[1]);
            employeeRepository.save(employee);
        }
        
        System.out.println("✓ 員工數據初始化完成");
    }
    
    private void createStoredProcedure() {
        System.out.println("準備創建/更新存儲程序...");
        try {
            // 先刪除舊程序如果存在
            try {
                jdbcTemplate.execute("DROP PROCEDURE UpdateEmployeeSeat");
                System.out.println("已刪除舊的存儲程序");
            } catch (Exception e) {
                // 程序不存在，忽略
            }
            
            // 創建存儲程序
            String spSql = "CREATE PROCEDURE UpdateEmployeeSeat " +
                    "@EmpID VARCHAR(5), " +
                    "@NewSeatSeq INT " +
                    "AS " +
                    "BEGIN " +
                    "    SET NOCOUNT ON " +
                    "    BEGIN TRY " +
                    "        BEGIN TRANSACTION " +
                    "        UPDATE Employee SET FLOOR_SEAT_SEQ = NULL WHERE EMP_ID = @EmpID " +
                    "        IF @NewSeatSeq IS NOT NULL AND @NewSeatSeq > 0 " +
                    "        BEGIN " +
                    "            UPDATE Employee SET FLOOR_SEAT_SEQ = @NewSeatSeq WHERE EMP_ID = @EmpID " +
                    "        END " +
                    "        COMMIT TRANSACTION " +
                    "    END TRY " +
                    "    BEGIN CATCH " +
                    "        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION " +
                    "        THROW " +
                    "    END CATCH " +
                    "END";
            jdbcTemplate.execute(spSql);
            System.out.println("✓ 存儲程序 UpdateEmployeeSeat 創建成功");
        } catch (Exception e) {
            System.out.println("✗ 存儲程序創建失敗: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
