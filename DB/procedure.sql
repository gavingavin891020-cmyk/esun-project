-- ============================================================================
-- 員工座位系統 - Stored Procedures
-- ============================================================================

-- 座位更新存儲程序
-- 功能: 清空員工原本座位並指派新座位
-- 參數: @EmpID 員工編號(5碼), @NewSeatSeq 座位序號(NULL表示清除座位)
-- 事務: 原子性操作，保證資料一致性
CREATE OR ALTER PROCEDURE UpdateEmployeeSeat
    @EmpID VARCHAR(5),
    @NewSeatSeq INT = NULL
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;
        
        -- 清空該員工原本的座位
        UPDATE Employee SET FLOOR_SEAT_SEQ = NULL WHERE EMP_ID = @EmpID;
        
        -- 若新座位序號不為 NULL，則指派新座位
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
END;

-- 查詢座位使用情況
-- 功能: 查詢所有座位及其使用狀態
CREATE OR ALTER PROCEDURE GetSeatingInfo
AS
BEGIN
    SELECT 
        sc.FLOOR_SEAT_SEQ,
        sc.FLOOR_NO,
        sc.SEAT_NO,
        e.EMP_ID,
        e.EMP_NAME
    FROM SeatingChart sc
    LEFT JOIN Employee e ON sc.FLOOR_SEAT_SEQ = e.FLOOR_SEAT_SEQ
    ORDER BY sc.FLOOR_NO, sc.SEAT_NO;
END;

-- 查詢所有員工
-- 功能: 查詢員工列表及其座位資訊
CREATE OR ALTER PROCEDURE GetEmployeeList
AS
BEGIN
    SELECT 
        e.EMP_ID,
        e.EMP_NAME,
        e.FLOOR_SEAT_SEQ,
        CASE WHEN e.FLOOR_SEAT_SEQ IS NOT NULL THEN 
            'Floor ' + CAST(sc.FLOOR_NO AS VARCHAR) + ' Seat ' + CAST(sc.SEAT_NO AS VARCHAR)
        ELSE 
            'Unassigned'
        END AS SEAT_INFO
    FROM Employee e
    LEFT JOIN SeatingChart sc ON e.FLOOR_SEAT_SEQ = sc.FLOOR_SEAT_SEQ
    ORDER BY e.EMP_ID;
END;