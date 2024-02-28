--liquibase formatted sql

--changeset dveizov:223.1
DROP PROCEDURE [IPASPROD].[selectIpPersonLastVersion];

--changeset dveizov:223.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[selectIpPersonLastVersion] @personNumber numeric(8), @ipPersonLastVersion numeric(8) OUTPUT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            DECLARE @personCount int;
            SET @personCount = (
                SELECT COUNT(*) FROM IPASPROD.IP_PERSON p where p.PERSON_NBR = @personNumber
            );
            IF @personCount <> 1 -- Check if person exists
                BEGIN
                    THROW 51000, 'Cannot find person', 1;
                END

            DECLARE @newerVersion numeric(15);
            SET @newerVersion = (
                SELECT p.GRAL_PERSON_ID_NBR FROM IPASPROD.IP_PERSON p where p.PERSON_NBR = @personNumber
            );

            IF @newerVersion IS NOT NULL
                BEGIN
                    EXEC IPASPROD.selectIpPersonLastVersion @personNumber = @newerVersion, @ipPersonLastVersion = @ipPersonLastVersion OUTPUT
                END
            ELSE
                BEGIN
                    SET @ipPersonLastVersion = @personNumber
                END
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        DECLARE
            @ErrorSeverity INT,
            @ErrorNumber INT,
            @ErrorMessage NVARCHAR(4000),
            @ErrorState INT,
            @ErrorLine INT,
            @ErrorProc NVARCHAR(200)
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