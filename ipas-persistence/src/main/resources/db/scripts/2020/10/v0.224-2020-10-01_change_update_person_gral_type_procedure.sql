--liquibase formatted sql

--changeset dveizov:224.1
DROP PROCEDURE [IPASPROD].[updateIpPersonGralIdTypeStartingByLastVersion];

--changeset dveizov:224.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[updateIpPersonGralIdTypeStartingByLastVersion] @personNumber numeric(8)
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

            DECLARE @newerVersion numeric(15), @currentGralIdType varchar (10);
            SELECT @newerVersion = p.GRAL_PERSON_ID_NBR,
                   @currentGralIdType = p.GRAL_PERSON_ID_TYP
            FROM IPASPROD.IP_PERSON p where p.PERSON_NBR = @personNumber


            DECLARE @olderVersion numeric(15);
            DECLARE curson_child CURSOR LOCAL FOR SELECT p.PERSON_NBR as olderVersion FROM IPASPROD.IP_PERSON p where p.GRAL_PERSON_ID_NBR = @personNumber;
            OPEN curson_child;

            FETCH NEXT FROM curson_child INTO @olderVersion;
            IF @@FETCH_STATUS = -1 AND @newerVersion IS NULL
                BEGIN
                    IF @currentGralIdType IS NOT NULL
                        BEGIN
                            UPDATE IPASPROD.IP_PERSON SET GRAL_PERSON_ID_TYP = NULL WHERE PERSON_NBR = @personNumber
                        END
                END
            ELSE
                BEGIN
                    IF @newerVersion IS NULL
                        BEGIN
                            IF 'LAST' <> @currentGralIdType OR @currentGralIdType IS NULL
                                BEGIN
                                    UPDATE IPASPROD.IP_PERSON SET GRAL_PERSON_ID_TYP = 'LAST' WHERE PERSON_NBR = @personNumber
                                END
                        END
                    ELSE
                        BEGIN
                            IF 'OLD' <> @currentGralIdType OR @currentGralIdType IS NULL
                                BEGIN
                                    UPDATE IPASPROD.IP_PERSON SET GRAL_PERSON_ID_TYP = 'OLD' WHERE PERSON_NBR = @personNumber
                                END
                        END
                END

            WHILE @@FETCH_STATUS = 0
                BEGIN
                    PRINT @olderVersion
                    PRINT 'Processing person record.... '+ CAST(@olderVersion AS varchar);
                    EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @olderVersion;
                    FETCH NEXT FROM curson_child INTO @olderVersion;
                END;
            CLOSE curson_child;
            DEALLOCATE curson_child;
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
