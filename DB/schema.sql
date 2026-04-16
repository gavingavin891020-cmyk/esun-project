-- ============================================================================
-- 員工座位系統 - DDL (Data Definition Language)
-- 表結構定義
-- ============================================================================

-- 座位資訊表
CREATE TABLE SeatingChart (
    FLOOR_SEAT_SEQ INT IDENTITY(1,1) PRIMARY KEY,
    FLOOR_NO INT NOT NULL,
    SEAT_NO INT NOT NULL,
    CREATED_AT DATETIME DEFAULT GETDATE()
);

-- 員工資訊表
CREATE TABLE Employee (
    EMP_ID VARCHAR(5) PRIMARY KEY,
    EMP_NAME NVARCHAR(50) NOT NULL,
    FLOOR_SEAT_SEQ INT NULL,
    CREATED_AT DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (FLOOR_SEAT_SEQ) REFERENCES SeatingChart(FLOOR_SEAT_SEQ)
);

-- 建立索引以提高查詢效能
CREATE INDEX idx_Employee_Seat ON Employee(FLOOR_SEAT_SEQ);
CREATE INDEX idx_SeatingChart_Floor ON SeatingChart(FLOOR_NO);