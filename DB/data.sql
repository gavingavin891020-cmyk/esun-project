-- ============================================================================
-- 員工座位系統 - DML (Data Manipulation Language)
-- 初始資料
-- ============================================================================

-- 清空現有資料
DELETE FROM Employee;
DELETE FROM SeatingChart;
DBCC CHECKIDENT('SeatingChart', RESEED, 0);

-- 插入座位資訊 (4 樓 × 4 座位/樓 = 16 個座位)
-- 第 1 樓
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (1, 1);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (1, 2);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (1, 3);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (1, 4);

-- 第 2 樓
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (2, 1);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (2, 2);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (2, 3);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (2, 4);

-- 第 3 樓
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (3, 1);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (3, 2);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (3, 3);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (3, 4);

-- 第 4 樓
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (4, 1);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (4, 2);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (4, 3);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (4, 4);

-- 插入員工資訊
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10001', '王小明');
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10002', '李小華');
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10003', '陳小春');
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10004', '林大明');
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10005', '吳美麗');

-- 分配初始座位
UPDATE Employee SET FLOOR_SEAT_SEQ = 1 WHERE EMP_ID = '10001';
UPDATE Employee SET FLOOR_SEAT_SEQ = 2 WHERE EMP_ID = '10002';
UPDATE Employee SET FLOOR_SEAT_SEQ = 6 WHERE EMP_ID = '10003';