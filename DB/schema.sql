CREATE OR ALTER PROCEDURE UpdateEmployeeSeat
    @EmpID VARCHAR(5),
    @NewSeatSeq INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;
        
        -- 清空該員工原本的座位
        UPDATE Employee SET FLOOR_SEAT_SEQ = NULL WHERE EMP_ID = @EmpID;
        
        -- 換到新座位
        UPDATE Employee SET FLOOR_SEAT_SEQ = @NewSeatSeq WHERE EMP_ID = @EmpID;
        
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END