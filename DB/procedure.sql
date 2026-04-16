-- 新增 1 到 4 樓的座位 (每層 4 個)
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (1, 1), (1, 2), (1, 3), (1, 4);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (2, 1), (2, 2), (2, 3), (2, 4);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (3, 1), (3, 2), (3, 3), (3, 4);
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES (4, 1), (4, 2), (4, 3), (4, 4);

-- 新增員工名單
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10001', '王小明');
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10002', '李小華');
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10003', '陳小春');
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10004', '林大明');
INSERT INTO Employee (EMP_ID, EMP_NAME) VALUES ('10005', '吳美麗');