CREATE PROCEDURE [IPASPROD].[deleteProcessAction] @procTyp [varchar](4),
                                                  @procNbr [varchar](8),
                                                  @actionNbr [numeric](10),
                                                  @deleteUserId [numeric](10),
                                                  @deleteReason text

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

            DECLARE
                @indChangeStatus      varchar(1),
                @priorStatus          varchar(4),
                @priorStatusDate      datetime,
                @priorDueDate         datetime,
                @priorResponsibleUser numeric(5),
                @actionDate           datetime,
                @maxActionDate        datetime,
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

                SELECT @maxActionDate = MAX(a.ACTION_DATE) FROM IPASPROD.IP_ACTION a where a.PROC_TYP = @procTyp AND a.PROC_NBR = @procNbr
                IF @maxActionDate <> @actionDate --Check if action is last
                    BEGIN
                        THROW 51000, 'Only last action of process can be deleted', 1;
                    END
                ELSE
                    BEGIN
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
END;


