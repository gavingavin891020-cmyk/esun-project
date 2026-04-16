# 員工座位安排系統 - 系統架構說明

## 📋 項目概述
玉山銀行員工座位安排系統，提供人資部門管理員工座位配置的 Web 應用。

## 🏗️ 系統架構

### 三層式架構
```
┌─────────────────────────────────────────┐
│      表現層 (Web Browser / Vue.js)      │
│    - 顯示座位圖、員工選單、操作界面     │
└──────────────────┬──────────────────────┘
                   │ HTTP/REST API
┌──────────────────▼──────────────────────┐
│    應用層 (Tomcat / Spring Boot)        │
│  - Controller: 請求處理、數據驗證       │
│  - Service: 業務邏輯、事務管理          │
│  - Repository: JPA 數據存取層           │
└──────────────────┬──────────────────────┘
                   │ JDBC
┌──────────────────▼──────────────────────┐
│      資料層 (SQL Server 2019)           │
│  - Employee 表、SeatingChart 表         │
│  - UpdateEmployeeSeat 存儲程序          │
└─────────────────────────────────────────┘
```

## 📁 項目結構

```
esun-project/
├── backend/demo/                    # Spring Boot 後端
│   ├── src/main/java/
│   │   └── com/example/demo/
│   │       ├── controller/          # 表現層
│   │       │   └── SeatController   # 座位管理 API
│   │       ├── service/             # 業務層
│   │       │   └── SeatService      # 座位業務邏輯
│   │       ├── repository/          # 資料層
│   │       │   ├── EmployeeRepository
│   │       │   └── SeatingChartRepository
│   │       ├── entity/              # 共用層
│   │       │   ├── Employee
│   │       │   └── SeatingChart
│   │       ├── config/              # 配置
│   │       │   └── DataInitializer  # 數據初始化
│   │       └── DemoApplication      # 主程序
│   ├── resources/
│   │   ├── application.properties   # Spring 配置
│   │   ├── schema.sql              # 表結構
│   │   └── data.sql                # 初始數據
│   └── pom.xml                     # Maven 配置
├── frontend/                        # Vue.js 前端
│   ├── src/
│   │   ├── components/
│   │   ├── views/
│   │   ├── App.vue
│   │   └── main.js
│   └── package.json
└── DB/                              # 資料庫腳本
    ├── schema.sql                  # 表結構
    ├── data.sql                    # 初始數據
    └── procedure.sql               # 存儲程序
```

## 🔐 安全性實現

### 1. SQL Injection 防護 ✅
```java
// ❌ 不安全的方式
String sql = "SELECT * FROM Employee WHERE EMP_ID = '" + empId + "'";

// ✅ 安全的方式 (PreparedStatement)
jdbcTemplate.update("EXEC UpdateEmployeeSeat @EmpID = ?, @NewSeatSeq = ?", 
    empId, seatSeq);
```

**實現位置：**
- `SeatService.updateSeat()` - 使用參數化查詢
- `SeatController.updateSeat1()` - 輸入驗證

### 2. XSS 攻擊防護 ✅
```java
// ✅ 輸入驗證和清理
String empId = ((String) empIdObj).trim();

// ✅ 格式檢查 (只允許字母和數字)
if (!empId.matches("^[A-Za-z0-9]*$")) {
    throw new IllegalArgumentException("格式無效");
}

// ✅ 長度限制
if (empId.length() > 5) {
    throw new IllegalArgumentException("長度無效");
}
```

**實現位置：**
- `SeatController.updateSeat1()` - 後端驗證
- Vue.js - 前端自動 HTML escape

### 3. Transaction 事務管理 ✅
```java
@Transactional
public void updateSeat(String empId, Integer seatSeq) {
    // 多個 SQL 操作在一個事務內執行
    // 若任何操作失敗，全部回滾
}
```

**存儲程序內實現：**
```sql
BEGIN TRANSACTION;
  UPDATE Employee SET FLOOR_SEAT_SEQ = NULL WHERE EMP_ID = @EmpID;
  UPDATE Employee SET FLOOR_SEAT_SEQ = @NewSeatSeq WHERE EMP_ID = @EmpID;
COMMIT TRANSACTION;
```

## 🗄️ 資料庫設計

### Employee 表
| 欄位名 | 型別 | 說明 |
|--------|------|------|
| EMP_ID | VARCHAR(5) | 員工編號 (Primary Key) |
| EMP_NAME | NVARCHAR(50) | 員工名稱 |
| FLOOR_SEAT_SEQ | INT | 座位序號 (Foreign Key) |

### SeatingChart 表
| 欄位名 | 型別 | 說明 |
|--------|------|------|
| FLOOR_SEAT_SEQ | INT | 座位序號 (Primary Key) |
| FLOOR_NO | INT | 樓層號 (1-4) |
| SEAT_NO | INT | 座位號 (1-4) |

### UpdateEmployeeSeat 存儲程序
```sql
CREATE PROCEDURE UpdateEmployeeSeat
    @EmpID VARCHAR(5),
    @NewSeatSeq INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;
        
        -- 清空該員工原本的座位
        UPDATE Employee SET FLOOR_SEAT_SEQ = NULL WHERE EMP_ID = @EmpID;
        
        -- 若新座位序號不為 NULL，指派新座位
        IF @NewSeatSeq IS NOT NULL AND @NewSeatSeq > 0
        BEGIN
            UPDATE Employee SET FLOOR_SEAT_SEQ = @NewSeatSeq WHERE EMP_ID = @EmpID;
        END
        
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END
```

## 📡 API 設計

### 1. 取得所有員工
- **方法：** GET
- **端點：** `/api/employees`
- **回應：** 員工列表

### 2. 取得所有座位
- **方法：** GET
- **端點：** `/api/seats`
- **回應：** 座位列表

### 3. 更新座位
- **方法：** POST
- **端點：** `/api/update-seat1`
- **請求體：**
```json
{
    "empId": "10001",
    "seatSeq": 5
}
```
- **回應：**
```json
{
    "status": "success",
    "message": "座位更新成功"
}
```

## 🛠️ 技術棧

| 層級 | 技術 | 版本 |
|------|------|------|
| 前端 | Vue.js | 3.5.31 |
| 前端框架 | Vite | 6.0.0 |
| 後端 | Spring Boot | 3.5.14 |
| ORM | Spring Data JPA | Included |
| 構建工具 | Maven | 3.x |
| 資料庫 | SQL Server | 2019 |
| Java | OpenJDK | 21.0.10 |

## ✅ 需求符合度

| 需求 | 實現狀況 | 說明 |
|------|---------|------|
| 顯示各樓層座位 | ✅ 完成 | GET /api/seats |
| 調整座位 | ✅ 完成 | POST /api/update-seat1 |
| 三層式架構 | ✅ 完成 | Web + App + DB |
| Vue.js | ✅ 完成 | 3.5.31 |
| Spring Boot | ✅ 完成 | 3.5.14 |
| RESTful API | ✅ 完成 | JSON 格式 |
| Maven | ✅ 完成 | pom.xml |
| Stored Procedure | ✅ 完成 | UpdateEmployeeSeat |
| Transaction | ✅ 完成 | @Transactional + SP |
| 防 SQL Injection | ✅ 完成 | PreparedStatement |
| 防 XSS 攻擊 | ✅ 完成 | 輸入驗證 + Vue escape |
| DB 文件 | ✅ 完成 | /DB 資料夾 |

## 🚀 運行方式

### 後端
```bash
cd backend/demo
mvn spring-boot:run
```

### 前端
```bash
cd frontend
npm install
npm run dev
```

## 📝 備註
- 所有 SQL 操作使用 PreparedStatement，防止 SQL Injection
- 所有 API 輸入經過驗證，防止 XSS 攻擊
- 座位更新使用事務確保數據一致性
- 存儲程序自動在應用啟動時創建
