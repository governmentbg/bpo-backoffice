--liquibase formatted sql

--changeset dveizov:180.1 splitStatements:false
DROP PROCEDURE IPASPROD.deleteProcessAction;

--changeset dveizov:180.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[deleteProcessAction] @procTyp [varchar](4),
                                                  @procNbr [varchar](8),
                                                  @actionNbr [numeric](10),
                                                  @deleteUserId [numeric](10),
                                                  @deleteReason text,
                                                  @executionStatus BIT OUTPUT,
                                                  @errorMessage NVARCHAR(4000) OUTPUT

AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            DECLARE @actionCount int;
            SET @actionCount = (
                SELECT count(*) FROM IPASPROD.IP_ACTION a where a.PROC_TYP = @procTyp AND a.PROC_NBR = @procNbr AND a.ACTION_NBR = @actionNbr
            );
            IF @actionCount <> 1 -- Check if action exists
                BEGIN
                    THROW 51000, 'Cannot find action', 1;
                END

            DELETE FROM IPASPROD.IP_ACTION_OUTPUT_FIELDS where PROC_TYP = @procTyp AND PROC_NBR = @procNbr AND ACTION_NBR = @actionNbr

            DECLARE
                @indChangeStatus      varchar(1),
                @priorStatus          varchar(4),
                @priorStatusDate      datetime,
                @priorDueDate         datetime,
                @priorResponsibleUser numeric(5),
                @actionDate           datetime,
                @maxActionNumber      numeric(10),
                @offidocOri           varchar(4),
                @offidocSer           numeric(4),
                @offidocNbr           numeric(7),
                @actionTyp            varchar(8),
                @journalCode          varchar(8),
                @actionNotes          varchar(4000),
                @notes1               varchar(4000),
                @notes2               varchar(4000),
                @notes3               varchar(4000),
                @notes4               varchar(4000),
                @notes5               varchar(4000),
                @captureUser          numeric(5),
                @newStatus            varchar(4);

            SELECT @indChangeStatus = a.IND_CHANGES_STATUS,
                   @priorStatus = a.PRIOR_STATUS_CODE,
                   @priorStatusDate = a.PRIOR_STATUS_DATE,
                   @priorDueDate = a.PRIOR_DUE_DATE,
                   @priorResponsibleUser = a.PRIOR_RESPONSIBLE_USER_ID,
                   @newStatus = a.NEW_STATUS_CODE,
                   @actionDate = a.ACTION_DATE,
                   @actionTyp = a.ACTION_TYP,
                   @journalCode = a.JOURNAL_CODE,
                   @actionNotes = a.ACTION_NOTES,
                   @notes1 = a.NOTES1,
                   @notes2 = a.NOTES2,
                   @notes3 = a.NOTES3,
                   @notes4 = a.NOTES4,
                   @notes5 = a.NOTES5,
                   @captureUser = a.CAPTURE_USER_ID,
                   @offidocOri = a.OFFIDOC_ORI,
                   @offidocSer = a.OFFIDOC_SER,
                   @offidocNbr = a.OFFIDOC_NBR
            FROM IPASPROD.IP_ACTION a
            where a.PROC_TYP = @procTyp AND a.PROC_NBR = @procNbr AND a.ACTION_NBR = @actionNbr

            IF @indChangeStatus = 'S' -- Check if current actions is normal
                BEGIN -- Delete normal action
                IF @journalCode IS NOT NULL -- Check if action has journal
                    BEGIN
                        DECLARE @indClosed varchar(1);
                        SET @indClosed = (
                            SELECT j.IND_CLOSED FROM IP_JOURNAL j where j.JOURNAL_CODE = @journalCode
                        );

                        IF @indClosed = 'S' -- Check if journal is closed
                            BEGIN
                                THROW 51000, 'Cannot delete published action !', 1;
                            END
                    END

                SELECT @maxActionNumber = MAX(a.ACTION_NBR) FROM IPASPROD.IP_ACTION a where a.PROC_TYP = @procTyp AND a.PROC_NBR = @procNbr
                IF @maxActionNumber <> @actionNbr --Check if action is last
                    BEGIN
                        THROW 51000, 'Only last action of process can be deleted', 1;
                    END
                ELSE
                    BEGIN
                        DECLARE @invalidationActionsCount int;
                        SET @invalidationActionsCount = (
                            SELECT count(*) FROM EXT_CORE.IP_FILE_RECORDAL a where a.INVALIDATION_PROC_TYP = @procTyp AND a.INVALIDATION_PROC_NBR = @procNbr AND a.INVALIDATION_ACTION_NBR = @actionNbr
                        );

                        IF @invalidationActionsCount > 0 -- Check if invalidation actions exists
                            BEGIN -- Delete invalidation data

                            DECLARE
                                @recordalUserdocOri varchar(4),
                                @recordalUserdocLog varchar(1),
                                @recordalUserdocSer numeric(4),
                                @recordalUserdocNbr numeric(15)

                            SELECT @recordalUserdocOri = a.DOC_ORI,
                                   @recordalUserdocLog = a.DOC_LOG,
                                   @recordalUserdocSer = a.DOC_SER,
                                   @recordalUserdocNbr = a.DOC_NBR
                            FROM EXT_CORE.IP_FILE_RECORDAL a
                            where a.INVALIDATION_PROC_TYP = @procTyp AND a.INVALIDATION_PROC_NBR = @procNbr AND a.INVALIDATION_ACTION_NBR = @actionNbr

                            UPDATE EXT_CORE.IP_FILE_RECORDAL
                            SET INVALIDATION_PROC_TYP   = NULL,
                                INVALIDATION_PROC_NBR   = NULL,
                                INVALIDATION_ACTION_NBR = NULL,
                                INVALIDATION_DATE       = NULL,
                                INVALIDATION_DOC_ORI    = NULL,
                                INVALIDATION_DOC_LOG    = NULL,
                                INVALIDATION_DOC_SER    = NULL,
                                INVALIDATION_DOC_NBR    = NULL
                            WHERE INVALIDATION_PROC_TYP = @procTyp
                              AND INVALIDATION_PROC_NBR = @procNbr
                              AND INVALIDATION_ACTION_NBR = @actionNbr;

                            UPDATE IPASPROD.IP_DOC SET DATA_DATE5 = NULL -- Remove invalidation date of recordal userdoc
                            WHERE DOC_ORI = @recordalUserdocOri AND DOC_LOG = @recordalUserdocLog AND DOC_SER = @recordalUserdocSer AND DOC_NBR = @recordalUserdocNbr;

                            END


                        DECLARE @recordalActionsCount int;
                        SET @recordalActionsCount = (
                            SELECT count(*) FROM EXT_CORE.IP_FILE_RECORDAL a where a.PROC_TYP = @procTyp AND a.PROC_NBR = @procNbr AND a.ACTION_NBR = @actionNbr
                        );

                        IF @recordalActionsCount > 0 -- Check if recordal actions exists
                            BEGIN -- Delete recordal data

                            DECLARE
                                @invalidationDate       datetime,
                                @invalidationUserdocOri varchar(4),
                                @invalidationUserdocLog varchar(1),
                                @invalidationUserdocSer numeric(4),
                                @invalidationUserdocNbr numeric(15),
                                @recordalFileSeq        varchar(2),
                                @recordalFileTyp        varchar(1),
                                @recordalFileSer        numeric(4),
                                @recordalFileNbr        numeric(10);

                            SELECT @invalidationDate       = a.INVALIDATION_DATE,
                                   @invalidationUserdocOri = a.INVALIDATION_DOC_ORI,
                                   @invalidationUserdocLog = a.INVALIDATION_DOC_LOG,
                                   @invalidationUserdocSer = a.INVALIDATION_DOC_SER,
                                   @invalidationUserdocNbr = a.INVALIDATION_DOC_NBR,
                                   @recordalFileSeq        = a.FILE_SEQ,
                                   @recordalFileTyp        = a.FILE_TYP,
                                   @recordalFileSer        = a.FILE_SER,
                                   @recordalFileNbr        = a.FILE_NBR
                            FROM EXT_CORE.IP_FILE_RECORDAL a where a.PROC_TYP = @procTyp AND a.PROC_NBR = @procNbr AND a.ACTION_NBR = @actionNbr

                            IF @invalidationDate IS NOT NULL OR  -- Check if invalidation data exists
                               @invalidationUserdocOri IS NOT NULL OR
                               @invalidationUserdocLog IS NOT NULL OR
                               @invalidationUserdocSer IS NOT NULL OR
                               @invalidationUserdocNbr IS NOT NULL
                                BEGIN
                                    THROW 51000, 'Cannot delete action, because recordal invalidation exists in EXT_CORE.IP_FILE_RECORDAL', 1;
                                END

                            DELETE FROM EXT_CORE.IP_FILE_RECORDAL where PROC_TYP = @procTyp AND PROC_NBR = @procNbr AND ACTION_NBR = @actionNbr;
                            EXEC increaseMarkOrPatentRowVersion @fileSeq = @recordalFileSeq, @fileTyp = @recordalFileTyp, @fileSer = @recordalFileSer, @fileNbr = @recordalFileNbr
                            END


                        DELETE FROM IPASPROD.IP_ACTION where PROC_TYP = @procTyp AND PROC_NBR = @procNbr AND ACTION_NBR = @actionNbr

                        IF @offidocOri IS NOT NULL AND @offidocSer IS NOT NULL AND @offidocNbr IS NOT NULL -- Check if action contain office document
                            BEGIN --Delete office document
                            DELETE FROM IPASPROD.IP_OFFIDOC where OFFIDOC_ORI = @offidocOri AND OFFIDOC_SER = @offidocSer AND OFFIDOC_NBR = @offidocNbr AND PROC_NBR = @procNbr AND PROC_TYP = @procTyp

                            DECLARE @offidocProcTyp varchar(4);
                            DECLARE @offidocProcNumber varchar(8);

                            SELECT @offidocProcTyp = p.PROC_TYP, @offidocProcNumber = p.PROC_NBR FROM IPASPROD.IP_PROC p where p.OFFIDOC_ORI = @offidocOri AND p.OFFIDOC_SER = @offidocSer AND p.OFFIDOC_NBR = @offidocNbr

                            IF @offidocProcTyp IS NULL OR @offidocProcNumber IS NULL -- Check existing of office document process
                                BEGIN
                                    THROW 51000, 'Cannot find offidoc process', 1;
                                END

                            DECLARE @offidocProcessUppers int;
                            SET @offidocProcessUppers = (
                                SELECT count(*)
                                FROM IPASPROD.IP_PROC a
                                where a.UPPER_PROC_TYP = @offidocProcTyp
                                  AND a.UPPER_PROC_NBR = @offidocProcNumber
                            );

                            IF @offidocProcessUppers > 0 -- Check existing of processes with upper process = office document process
                                BEGIN
                                    THROW 51000, 'There are upper processes of offidoc process. You must delete them first !' , 1;
                                END

                            DECLARE @offidocActionsOffidocs int;
                            SET @offidocActionsOffidocs = (
                                SELECT count(*)
                                FROM IPASPROD.IP_ACTION a
                                where a.PROC_NBR = @offidocProcNumber
                                  and a.PROC_TYP = @offidocProcTyp
                                  AND a.OFFIDOC_NBR is not null
                                  and a.OFFIDOC_SER is not null
                                  and a.OFFIDOC_ORI is not null
                            );

                            IF @offidocActionsOffidocs > 0 -- Check existing of sub offidocs
                                BEGIN
                                    THROW 51000, 'There are offidoc actions with offidocs. You must delete them first !', 1;
                                END


                            DELETE FROM IPASPROD.IP_ACTION where PROC_NBR = @offidocProcNumber AND PROC_TYP = @offidocProcTyp -- Delete offidoc actions
                            DELETE FROM IPASPROD.IP_PROC where PROC_NBR = @offidocProcNumber AND PROC_TYP = @offidocProcTyp -- Delete offidoc process
                            END

                        UPDATE IPASPROD.IP_PROC -- Update main process
                        SET STATUS_CODE         = @priorStatus,
                            STATUS_DATE         = @priorStatusDate,
                            EXPIRATION_DATE     = @priorDueDate,
                            RESPONSIBLE_USER_ID = @priorResponsibleUser
                        WHERE PROC_TYP = @procTyp
                          AND PROC_NBR = @procNbr;
                    END

                END
            ELSE
                BEGIN -- Delete note action
                DELETE FROM IPASPROD.IP_ACTION where PROC_TYP = @procTyp AND PROC_NBR = @procNbr AND ACTION_NBR = @actionNbr
                END

            DECLARE @fileSeq varchar(2);
            DECLARE @fileTyp varchar(1);
            DECLARE @fileSer numeric(4);
            DECLARE @fileNbr numeric(10);
            DECLARE @docOri varchar(4);
            DECLARE @docLog varchar(1);
            DECLARE @docSer numeric(4);
            DECLARE @docNbr numeric(15);
            DECLARE @procOffidocOri varchar(4);
            DECLARE @procOffidocSer numeric(4);
            DECLARE @procOffidocNbr numeric(7);
            DECLARE @topProcFileSeq varchar(2);
            DECLARE @topProcFileTyp varchar(1);
            DECLARE @topProcFileSer numeric(4);
            DECLARE @topProcFileNbr numeric(10);

            SELECT @docOri = p.DOC_ORI,
                   @docLog = p.DOC_LOG,
                   @docSer = p.DOC_SER,
                   @docNbr = p.DOC_NBR,
                   @fileSeq = p.FILE_SEQ,
                   @fileTyp = p.FILE_TYP,
                   @fileSer = p.FILE_SER,
                   @fileNbr = p.FILE_NBR,
                   @procOffidocOri = p.OFFIDOC_ORI,
                   @procOffidocSer = p.OFFIDOC_SER,
                   @procOffidocNbr = p.OFFIDOC_NBR,
                   @topProcFileSeq = fp.FILE_SEQ,
                   @topProcFileTyp = fp.FILE_TYP,
                   @topProcFileSer = fp.FILE_SER,
                   @topProcFileNbr = fp.FILE_NBR
            FROM IPASPROD.IP_PROC p
                     JOIN IPASPROD.IP_PROC fp on p.FILE_PROC_TYP = fp.PROC_TYP and p.FILE_PROC_NBR = fp.PROC_NBR
            WHERE p.PROC_TYP = @procTyp AND p.PROC_NBR = @procNbr

            IF @fileSeq IS NOT NULL AND @fileTyp IS NOT NULL AND @fileSer IS NOT NULL AND @fileNbr IS NOT NULL -- Increase row version of mark or patent object
                BEGIN
                    EXEC increaseMarkOrPatentRowVersion @fileSeq = @fileSeq, @fileTyp = @fileTyp, @fileSer = @fileSer, @fileNbr = @fileNbr
                END

            IF @docOri IS NOT NULL AND @docLog IS NOT NULL AND @docSer IS NOT NULL AND @docNbr IS NOT NULL -- Increase row version of userdoc object
                BEGIN
                    EXEC increaseUserdocRowVersion @docOri = @docOri, @docLog = @docLog, @docSer = @docSer, @docNbr = @docNbr
                END

            DECLARE @logActionNumber numeric(5);
            SELECT @logActionNumber = MAX(LOG_ACTION_NBR) FROM IPASPROD.IP_ACTION_LOG_DELETES l where l.PROC_NBR = @procNbr and l.PROC_TYP = @procTyp

            IF @logActionNumber IS NULL
                BEGIN
                    SET @logActionNumber = 1
                END
            ELSE
                BEGIN
                    SET @logActionNumber = @logActionNumber +1;
                END

            DECLARE @notesPrompt1 varchar(255);
            DECLARE @notesPrompt2 varchar(255);
            DECLARE @notesPrompt3 varchar(255);
            DECLARE @notesPrompt4 varchar(255);
            DECLARE @notesPrompt5 varchar(255);

            SELECT @notesPrompt1 = NOTES1_PROMPT,
                   @notesPrompt2 = NOTES2_PROMPT,
                   @notesPrompt3 = NOTES3_PROMPT,
                   @notesPrompt4 = NOTES4_PROMPT,
                   @notesPrompt5 = NOTES5_PROMPT
            FROM IPASPROD.CF_ACTION_TYPE t
            where t.ACTION_TYP = @actionTyp

            DECLARE @logNotes varchar(4000);
            SET @logNotes = @actionNotes;
            IF @notesPrompt1 IS NOT NULL
                BEGIN
                    IF @logNotes IS NULL
                        BEGIN
                            SET @logNotes = @notes1
                        END
                    ELSE
                        BEGIN
                            IF @notes1 IS NOT NULL
                                BEGIN
                                    SET @logNotes = @logNotes + '; ' + @notes1
                                END
                        END
                END
            IF @notesPrompt2 IS NOT NULL
                BEGIN
                    IF @logNotes IS NULL
                        BEGIN
                            SET @logNotes = @notes2
                        END
                    ELSE
                        BEGIN
                            IF @notes2 IS NOT NULL
                                BEGIN
                                    SET @logNotes = @logNotes + '; ' + @notes2
                                END
                        END
                END
            IF @notesPrompt3 IS NOT NULL
                BEGIN
                    IF @logNotes IS NULL
                        BEGIN
                            SET @logNotes = @notes3
                        END
                    ELSE
                        BEGIN
                            IF @notes3 IS NOT NULL
                                BEGIN
                                    SET @logNotes = @logNotes + '; ' + @notes3
                                END
                        END
                END
            IF @notesPrompt4 IS NOT NULL
                BEGIN
                    IF @logNotes IS NULL
                        BEGIN
                            SET @logNotes = @notes4
                        END
                    ELSE
                        BEGIN
                            IF @notes4 IS NOT NULL
                                BEGIN
                                    SET @logNotes = @logNotes + '; ' + @notes4
                                END
                        END
                END
            IF @notesPrompt5 IS NOT NULL
                BEGIN
                    IF @logNotes IS NULL
                        BEGIN
                            SET @logNotes = @notes5
                        END
                    ELSE
                        BEGIN
                            IF @notes5 IS NOT NULL
                                BEGIN
                                    SET @logNotes = @logNotes + '; ' + @notes5
                                END
                        END
                END
            INSERT INTO IPASPROD.IP_ACTION_LOG_DELETES (ROW_VERSION,
                                                        PROC_TYP, PROC_NBR,
                                                        LOG_ACTION_NBR,
                                                        ACTION_DATE,
                                                        ACTION_TYP,
                                                        CAPTURE_USER_ID,
                                                        NOTES,
                                                        XML_DESIGNER,
                                                        DELETING_DATE,
                                                        DELETING_USER_ID,
                                                        ACTION_NBR,
                                                        PRIOR_STATUS_CODE,
                                                        NEW_STATUS_CODE,
                                                        PRIOR_DUE_DATE,
                                                        PRIOR_RESPONSIBLE_USER_ID,
                                                        DELETE_REASON)
            VALUES (1, @procTyp, @procNbr, @logActionNumber, @actionDate, @actionTyp, @captureUser, LTRIM(RTRIM(@logNotes)), null, GETDATE(),
                    @deleteUserId, @actionNbr,
                    @priorStatus, @newStatus, @priorDueDate, @priorResponsibleUser, @deleteReason);

        SET @executionStatus = 0;
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        DECLARE
            @errorSeverity INT,
            @errorNumber   INT,
            @errorState    INT,
            @ErrorLine     INT,
            @ErrorProc     NVARCHAR(200)
        SET @errorSeverity = ERROR_SEVERITY()
        SET @errorNumber = ERROR_NUMBER()
        SET @errorMessage = ERROR_MESSAGE()
        SET @errorState = ERROR_STATE()

        -- Not all errors generate an error state, to set to 1 if it's zero
        IF @errorState = 0
            SET @errorState = 1
        -- If the error renders the transaction as uncommittable or we have open transactions, we may want to rollback
        IF @@TRANCOUNT > 0
            BEGIN
                PRINT 'ROLLBACK'
                ROLLBACK TRANSACTION
            END

        SET @executionStatus = 1;
        RAISERROR (@errorMessage , @errorSeverity, @errorState, @errorNumber)
    END CATCH
    RETURN @@ERROR
END
;