CREATE PROCEDURE [IPASPROD].[changeUserDocumentType] @docOri varchar(4),
                                                     @docLog varchar(1),
                                                     @docSer numeric(4),
                                                     @docNbr numeric(15),
                                                     @newUserdocType [varchar](7)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION

            DECLARE @newUserdocTypeCount int;
            SET @newUserdocTypeCount = (
                SELECT count(*) FROM IPASPROD.CF_USERDOC_TYPE a where a.USERDOC_TYP = @newUserdocType
            );
            IF @newUserdocTypeCount <> 1 -- Check if userdoc type exists
                BEGIN
                    THROW 51000, 'Cannot find new userdoc type in CF_USERDOC_TYPE !', 1;
                END

            DECLARE @userdocProcessCount int;
            SET @userdocProcessCount = (
                SELECT count(*) FROM IPASPROD.IP_PROC a where a.DOC_ORI = @docOri AND a.DOC_LOG = @docLog AND a.DOC_SER = @docSer AND a.DOC_NBR = @docNbr
            );
            IF @userdocProcessCount <> 1 -- Check if userdoc process exists
                BEGIN
                    THROW 51000, 'Cannot find userdoc process in IP_PROC !', 1;
                END

            DECLARE @userdocObjectCount int;
            SET @userdocObjectCount = (
                SELECT count(*) FROM IPASPROD.IP_USERDOC a where a.DOC_ORI = @docOri AND a.DOC_LOG = @docLog AND a.DOC_SER = @docSer AND a.DOC_NBR = @docNbr
            );
            IF @userdocObjectCount <> 1 -- Check if userdoc object exists
                BEGIN
                    THROW 51000, 'Cannot find userdoc object in IP_USERDOC !', 1;
                END

            DECLARE @procTyp varchar(4);
            DECLARE @procNbr numeric(8);
            DECLARE @fileProcTyp varchar(4);
            DECLARE @fileProcNbr numeric(8);
            DECLARE @fileSeq varchar(2);
            DECLARE @fileTyp varchar(1);
            DECLARE @fileSer numeric(4);
            DECLARE @fileNbr numeric(10);
            DECLARE @userdocType varchar(7);
            DECLARE @originalUpperProcTyp varchar(4);
            DECLARE @originalUpperProcNbr numeric(8);

            SELECT @userdocType = p.USERDOC_TYP,
                   @procTyp = p.PROC_TYP,
                   @procNbr = p.PROC_NBR,
                   @fileProcTyp = p.FILE_PROC_TYP,
                   @fileProcNbr = p.FILE_PROC_NBR,
                   @fileSeq = fp.FILE_SEQ,
                   @fileTyp = fp.FILE_TYP,
                   @fileSer = fp.FILE_SER,
                   @fileNbr = fp.FILE_NBR,
                   @originalUpperProcTyp = p.UPPER_PROC_TYP,
                   @originalUpperProcNbr = p.UPPER_PROC_NBR
            FROM IPASPROD.IP_PROC p
                     JOIN IPASPROD.IP_PROC fp on p.FILE_PROC_TYP = fp.PROC_TYP and p.FILE_PROC_NBR = fp.PROC_NBR
            WHERE p.DOC_ORI = @docOri AND p.DOC_LOG = @docLog AND p.DOC_SER = @docSer AND p.DOC_NBR = @docNbr

            IF @userdocType = @newUserdocType -- Check if userdoc types are equals
                BEGIN
                    THROW 51000, 'Cannot change userdoc type, because old and new userdoc type are equals !', 1;
                END

            DECLARE @userdocTypeProcessType varchar(7);
            SET @userdocTypeProcessType = (
                SELECT a.GENERATE_PROC_TYP FROM IPASPROD.CF_USERDOC_TYPE a where a.USERDOC_TYP = @userdocType
            );

            DECLARE @newUserdocTypeProcessType varchar(7);
            SET @newUserdocTypeProcessType = (
                SELECT a.GENERATE_PROC_TYP FROM IPASPROD.CF_USERDOC_TYPE a where a.USERDOC_TYP = @newUserdocType
            );

            IF (@userdocTypeProcessType <> @newUserdocTypeProcessType) -- Check if new and old userdoc types have same process type
                BEGIN
                    DECLARE @actionsCount int;
                    SET @actionsCount = (
                        SELECT count(*) FROM IPASPROD.IP_ACTION a where a.PROC_TYP = @procTyp and PROC_NBR = @procNbr
                    );
                    IF @actionsCount <> 0 -- Check number of process actions
                        BEGIN
                            THROW 51000, 'Delete process actions before userdoc type change !', 1;
                        END

                    DECLARE @procNbrConflict int;
                    SET @procNbrConflict = (
                        SELECT count(*) FROM IPASPROD.IP_PROC a where a.PROC_TYP = @newUserdocTypeProcessType and PROC_NBR = @procNbr
                    );
                    IF @procNbrConflict <> 0 -- Check for process type conflict
                        BEGIN
                            THROW 51000, 'Change userdoc type changes process type too. Conflict exists in IP_PROC table for selected PROC_TYP and PROC_NBR', 1;
                        END

                    DECLARE @newStatusCode varchar(4);
                    SET @newStatusCode = (
                        SELECT a.PRIMARY_INI_STATUS_CODE FROM IPASPROD.CF_PROCESS_TYPE a where a.PROC_TYP = @newUserdocTypeProcessType
                    );
                    IF @newStatusCode IS NULL -- Check if status exists
                        BEGIN
                            THROW 51000, 'Cannot find new status code from CF_PROCESS_TYPE', 1;
                        END

                    -- Insert new IP_PROC record
                    INSERT INTO IPASPROD.IP_PROC(ROW_VERSION, PROC_TYP, PROC_NBR, CREATION_DATE, STATUS_CODE,
                                                 STATUS_DATE, EXPIRATION_DATE, RESPONSIBLE_USER_ID, UPPER_PROC_TYP,
                                                 UPPER_PROC_NBR, IND_FREEZING_JUST_ENDED, drop1, drop2,
                                                 MANUAL_PROC_DESCRIPTION, MANUAL_PROC_REF, FILE_SEQ, FILE_TYP, FILE_SER,
                                                 FILE_NBR, APPL_TYP, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, USERDOC_TYP,
                                                 USERDOC_FILE_SEQ, USERDOC_FILE_TYP, USERDOC_FILE_SER, USERDOC_FILE_NBR,
                                                 OFFIDOC_ORI, OFFIDOC_SER, OFFIDOC_NBR, OFFIDOC_TYP, FILE_PROC_TYP,
                                                 FILE_PROC_NBR, END_FREEZE_FLAG, SUB_STATUS_CODE, IND_SIGNATURE_PENDING)
                    SELECT p.ROW_VERSION, @newUserdocTypeProcessType, p.PROC_NBR, p.CREATION_DATE, @newStatusCode,
                           GETDATE(), p.EXPIRATION_DATE, p.RESPONSIBLE_USER_ID, p.UPPER_PROC_TYP,
                           p.UPPER_PROC_NBR, p.IND_FREEZING_JUST_ENDED, p.drop1, p.drop2,
                           p.MANUAL_PROC_DESCRIPTION, p.MANUAL_PROC_REF, p.FILE_SEQ, p.FILE_TYP, p.FILE_SER,
                           p.FILE_NBR, p.APPL_TYP, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, p.DOC_NBR, @newUserdocType,
                           p.USERDOC_FILE_SEQ, p.USERDOC_FILE_TYP, p.USERDOC_FILE_SER, p.USERDOC_FILE_NBR,
                           p.OFFIDOC_ORI, p.OFFIDOC_SER, p.OFFIDOC_NBR, p.OFFIDOC_TYP, p.FILE_PROC_TYP,
                           p.FILE_PROC_NBR, p.END_FREEZE_FLAG, p.SUB_STATUS_CODE, p.IND_SIGNATURE_PENDING
                    FROM IP_PROC p where p.PROC_TYP = @procTyp AND p.PROC_NBR = @procNbr ;

                    -- Insert new IP_USERDOC_PROCS record
                    INSERT INTO IPASPROD.IP_USERDOC_PROCS (ROW_VERSION, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, USERDOC_TYP, USERDOC_FILE_SEQ, USERDOC_FILE_TYP, USERDOC_FILE_SER, USERDOC_FILE_NBR, PROC_TYP, PROC_NBR)
                        SELECT p.ROW_VERSION, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, p.DOC_NBR, @newUserdocType, p.USERDOC_FILE_SEQ, p.USERDOC_FILE_TYP, p.USERDOC_FILE_SER, p.USERDOC_FILE_NBR, @newUserdocTypeProcessType, p.PROC_NBR
                        FROM IPASPROD.IP_USERDOC_PROCS p where p.PROC_TYP = @procTyp AND p.PROC_NBR = @procNbr;

                    -- Insert new IP_PROC_FREEZES record FREEZING
                    INSERT INTO IPASPROD.IP_PROC_FREEZES (ROW_VERSION, FROZEN_PROC_TYP, FROZEN_PROC_NBR, FREEZING_PROC_TYP, FREEZING_PROC_NBR, IND_FREEZE_NO_OFFIDOC, IND_FREEZE_CONTINUE_WHEN_END)
                        SELECT p.ROW_VERSION, p.FROZEN_PROC_TYP, p.FROZEN_PROC_NBR, @newUserdocTypeProcessType, p.FREEZING_PROC_NBR, p.IND_FREEZE_NO_OFFIDOC, p.IND_FREEZE_CONTINUE_WHEN_END
                        FROM IPASPROD.IP_PROC_FREEZES p where p.FREEZING_PROC_TYP = @procTyp AND p.FREEZING_PROC_NBR = @procNbr;

                    -- Insert new IP_PROC_FREEZES record FROZEN
                    INSERT INTO IPASPROD.IP_PROC_FREEZES (ROW_VERSION, FROZEN_PROC_TYP, FROZEN_PROC_NBR, FREEZING_PROC_TYP, FREEZING_PROC_NBR, IND_FREEZE_NO_OFFIDOC, IND_FREEZE_CONTINUE_WHEN_END)
                        SELECT p.ROW_VERSION, @newUserdocTypeProcessType, p.FROZEN_PROC_NBR, p.FREEZING_PROC_TYP, p.FREEZING_PROC_NBR, p.IND_FREEZE_NO_OFFIDOC, p.IND_FREEZE_CONTINUE_WHEN_END
                        FROM IPASPROD.IP_PROC_FREEZES p where p.FROZEN_PROC_TYP = @procTyp AND p.FROZEN_PROC_NBR = @procNbr;

                    -- Insert new IP_ACTION_LOG_DELETES record FROZEN
                    INSERT INTO IPASPROD.IP_ACTION_LOG_DELETES (ROW_VERSION, PROC_TYP, PROC_NBR, LOG_ACTION_NBR, ACTION_DATE, ACTION_TYP, CAPTURE_USER_ID, NOTES, XML_DESIGNER, DELETING_DATE, DELETING_USER_ID, ACTION_NBR, PRIOR_STATUS_CODE, NEW_STATUS_CODE, PRIOR_DUE_DATE, PRIOR_RESPONSIBLE_USER_ID, DELETE_REASON)
                    select p.ROW_VERSION, @newUserdocTypeProcessType, @procNbr,p.LOG_ACTION_NBR,p.ACTION_DATE, p.ACTION_TYP, p.CAPTURE_USER_ID, p.NOTES, p.XML_DESIGNER, p.DELETING_DATE, p.DELETING_USER_ID, p.ACTION_NBR, p.PRIOR_STATUS_CODE, p.NEW_STATUS_CODE, p.PRIOR_DUE_DATE, p.PRIOR_RESPONSIBLE_USER_ID, p.DELETE_REASON
                    FROM IPASPROD.IP_ACTION_LOG_DELETES p where p.PROC_TYP = @procTyp AND p.PROC_NBR = @procNbr;

                    -- Update upper process types of child records
                    UPDATE IPASPROD.IP_PROC SET UPPER_PROC_TYP = @newUserdocTypeProcessType WHERE UPPER_PROC_TYP = @procTyp AND UPPER_PROC_NBR = @procNbr;

                    -- Delete old IP_USERDOC_TYPES record and insert new
                    DELETE FROM IPASPROD.IP_USERDOC_TYPES
                    where DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr and USERDOC_TYP = @userdocType;

                    INSERT INTO IPASPROD.IP_USERDOC_TYPES(ROW_VERSION, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, USERDOC_TYP, PROC_TYP, PROC_NBR, IND_PROCESS_FIRST)
                    VALUES (1, @docOri, @docLog, @docSer, @docNbr, @newUserdocType, NULL, NULL, NULL);

                    -- Delete old IP_USERDOC_PROCS record and insert new
                    DELETE FROM IPASPROD.IP_USERDOC_PROCS
                    where DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr and USERDOC_TYP = @userdocType AND PROC_TYP = @procTyp AND PROC_NBR = @procNbr;

                    -- Delete IP_PROC_FREEZE records
                    DELETE FROM IPASPROD.IP_PROC_FREEZES
                    where (FROZEN_PROC_TYP = @procTyp AND FROZEN_PROC_NBR = @procNbr) OR (FREEZING_PROC_TYP = @procTyp AND FREEZING_PROC_NBR = @procNbr);

                    -- Delete IP_ACTION_LOG_DELETES records
                    DELETE FROM IPASPROD.IP_ACTION_LOG_DELETES
                    where PROC_TYP = @procTyp AND PROC_NBR = @procNbr;

                    DELETE FROM IPASPROD.IP_PROC
                    where DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr and USERDOC_TYP = @userdocType AND PROC_TYP = @procTyp AND PROC_NBR = @procNbr;
                END
            ELSE
                BEGIN
                    -- Update IP_PROC.USERDOC_TYP
                    UPDATE IP_PROC SET USERDOC_TYP = @newUserdocType WHERE PROC_TYP = @procTyp AND PROC_NBR = @procNbr;

                    -- Delete old IP_USERDOC_PROCS record and insert new
                    DELETE FROM IPASPROD.IP_USERDOC_PROCS
                    where DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr and USERDOC_TYP = @userdocType
                      and USERDOC_FILE_SEQ = @fileSeq and USERDOC_FILE_TYP = @fileTyp and USERDOC_FILE_SER = @fileSer and USERDOC_FILE_NBR = @fileNbr;

                    INSERT INTO IPASPROD.IP_USERDOC_PROCS(ROW_VERSION, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, USERDOC_TYP,
                                                          USERDOC_FILE_SEQ, USERDOC_FILE_TYP, USERDOC_FILE_SER, USERDOC_FILE_NBR, PROC_TYP, PROC_NBR)
                    VALUES (1, @docOri, @docLog, @docSer, @docNbr, @newUserdocType, @fileSeq, @fileTyp, @fileSer, @fileNbr, @procTyp, @procNbr);

                    -- Delete old IP_USERDOC_TYPES record and insert new
                    DELETE FROM IPASPROD.IP_USERDOC_TYPES
                    where DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr and USERDOC_TYP = @userdocType;

                    INSERT INTO IPASPROD.IP_USERDOC_TYPES(ROW_VERSION, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, USERDOC_TYP, PROC_TYP, PROC_NBR, IND_PROCESS_FIRST)
                    VALUES (1, @docOri, @docLog, @docSer, @docNbr, @newUserdocType, NULL, NULL, NULL);
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
;

