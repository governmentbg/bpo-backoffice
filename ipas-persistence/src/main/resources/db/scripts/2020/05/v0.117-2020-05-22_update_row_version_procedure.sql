--liquibase formatted sql

--changeset dveizov:117.1 splitStatements:false
DROP PROCEDURE IPASPROD.[increaseMarkOrPatentRowVersion]
;
--changeset dveizov:117.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[increaseMarkOrPatentRowVersion] @fileSeq [varchar](2),
                                                             @fileTyp [varchar](1),
                                                             @fileSer [numeric](4),
                                                             @fileNbr [numeric](10)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            DECLARE @tableName varchar(30);
            SET @tableName = (
                SELECT DISTINCT a.TABLE_NAME FROM IPASPROD.CF_APPLICATION_TYPE a where a.FILE_TYP = @fileTyp
            );

            IF @tableName = 'MARCA'
                BEGIN
                    DECLARE @markRowVersion numeric(9);
                    SET @markRowVersion = (
                        SELECT ROW_VERSION FROM IPASPROD.IP_MARK where FILE_SEQ = @fileSeq AND FILE_TYP = @fileTyp AND FILE_SER = @fileSer AND FILE_NBR = @fileNbr
                    );
                    UPDATE IPASPROD.IP_MARK SET ROW_VERSION = (@markRowVersion + 1) where FILE_SEQ = @fileSeq AND FILE_TYP = @fileTyp AND FILE_SER = @fileSer AND FILE_NBR = @fileNbr
                END

            IF @tableName = 'SOLICITUD'
                BEGIN
                    DECLARE @patentRowVersion numeric(9);
                    SET @patentRowVersion = (
                        SELECT ROW_VERSION FROM IPASPROD.IP_PATENT where FILE_SEQ = @fileSeq AND FILE_TYP = @fileTyp AND FILE_SER = @fileSer AND FILE_NBR = @fileNbr
                    );
                    UPDATE IPASPROD.IP_PATENT SET ROW_VERSION = (@patentRowVersion + 1) where FILE_SEQ = @fileSeq AND FILE_TYP = @fileTyp AND FILE_SER = @fileSer AND FILE_NBR = @fileNbr
                END

            DECLARE @fileRowVersion numeric(9);
            SET @fileRowVersion = (
                SELECT ROW_VERSION FROM IPASPROD.IP_FILE where FILE_SEQ = @fileSeq AND FILE_TYP = @fileTyp AND FILE_SER = @fileSer AND FILE_NBR = @fileNbr
            );
            UPDATE IPASPROD.IP_FILE SET ROW_VERSION = (@fileRowVersion + 1) where FILE_SEQ = @fileSeq AND FILE_TYP = @fileTyp AND FILE_SER = @fileSer AND FILE_NBR = @fileNbr

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
;



