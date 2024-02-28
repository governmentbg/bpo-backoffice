--liquibase formatted sql

--changeset dveizov:154
CREATE PROCEDURE [IPASPROD].[selectNextIpPersonNumber] @newIpPersonNumber numeric(15) OUTPUT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            INSERT INTO IPASPROD.SYS_SEC_PERSONA values ('')
            SET @newIpPersonNumber = (
                SELECT @@IDENTITY
            )
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        DECLARE
            @ErrorSeverity INT,
            @ErrorNumber   INT,
            @ErrorMessage  NVARCHAR(4000),
            @ErrorState    INT,
            @ErrorLine     INT,
            @ErrorProc     NVARCHAR(200)
        SET @ErrorSeverity = ERROR_SEVERITY()
        SET @ErrorNumber = ERROR_NUMBER()
        SET @ErrorMessage = ERROR_MESSAGE()
        SET @ErrorState = ERROR_STATE()

        -- Not all errors generate an error state, to set to 1 if it's zero
        IF @ErrorState = 0
            SET @ErrorState = 1
        -- If the error renders the transaction as uncommittable or we have open transactions, we may want to rollback
        IF @@TRANCOUNT > 0
            BEGIN
                PRINT 'ROLLBACK'
                ROLLBACK TRANSACTION
            END
        RAISERROR (@ErrorMessage , @ErrorSeverity, @ErrorState, @ErrorNumber)
    END CATCH
    RETURN @@ERROR
END
