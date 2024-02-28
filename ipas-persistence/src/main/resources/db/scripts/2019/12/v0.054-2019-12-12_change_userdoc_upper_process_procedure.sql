CREATE PROCEDURE [IPASPROD].[changeUserDocumentUpperProcess] @userdocProcTyp [varchar](4),
                                                             @userdocProcNbr numeric(8),
                                                             @newUpperProcTyp [varchar](4),
                                                             @newUpperProcNbr numeric(8)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            DECLARE @docOri varchar(4);
            DECLARE @docLog varchar(1);
            DECLARE @docSer numeric(4);
            DECLARE @docNbr numeric(15);
            DECLARE @fileProcTyp varchar(4);
            DECLARE @fileProcNbr numeric(8);
            DECLARE @fileSeq varchar(2);
            DECLARE @fileTyp varchar(1);
            DECLARE @fileSer numeric(4);
            DECLARE @fileNbr numeric(10);
            DECLARE @userdocType varchar(7);
            DECLARE @originalUpperProcTyp varchar(4);
            DECLARE @originalUpperProcNbr numeric(8);

            SELECT @docOri = p.DOC_ORI,
                   @docLog = p.DOC_LOG,
                   @docSer = p.DOC_SER,
                   @docNbr = p.DOC_NBR,
                   @userdocType = p.USERDOC_TYP,
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
            WHERE p.PROC_TYP = @userdocProcTyp AND p.PROC_NBR = @userdocProcNbr

            IF @docOri IS NULL OR @docLog IS NULL OR @docSer IS NULL OR @docNbr IS NULL
                BEGIN
                    THROW 51000, 'Userdoc id is empty !', 1;
                END
            IF @fileProcTyp IS NULL OR @fileProcNbr IS NULL
                BEGIN
                    THROW 51000, 'File process id is empty !', 1;
                END
            IF @fileSeq IS NULL OR @fileTyp IS NULL OR @fileSer IS NULL OR @fileNbr IS NULL
                BEGIN
                    THROW 51000, 'Userdoc file id is empty !', 1;
                END
            IF @userdocType IS NULL
                BEGIN
                    THROW 51000, 'Userdoc type is empty', 1;
                END
            IF @originalUpperProcTyp IS NULL OR @originalUpperProcNbr IS NULL
                BEGIN
                    THROW 51000, 'Original upper process is empty !', 1;
                END
            IF (@originalUpperProcTyp = @newUpperProcTyp) AND (@originalUpperProcNbr = @newUpperProcNbr)
                BEGIN
                    THROW 51000, 'Original upper process and new upper process are same !', 1;
                END

            DECLARE @newUpperProcessFileProcTyp varchar(4);
            DECLARE @newUpperProcessFileProcNbr numeric(8);

            DECLARE @newUpperProcessDocOri varchar(4);
            DECLARE @newUpperProcessDocLog varchar(1);
            DECLARE @newUpperProcessDocSer numeric(4);
            DECLARE @newUpperProcessDocNbr numeric(15);

            DECLARE @newUpperProcessFileSeq varchar(2);
            DECLARE @newUpperProcessFileTyp varchar(1);
            DECLARE @newUpperProcessFileSer numeric(4);
            DECLARE @newUpperProcessFileNbr numeric(10);

            SELECT @newUpperProcessFileProcTyp = p.FILE_PROC_TYP,
                   @newUpperProcessFileProcNbr = p.FILE_PROC_NBR,
                   @newUpperProcessDocOri = p.DOC_ORI,
                   @newUpperProcessDocLog = p.DOC_LOG,
                   @newUpperProcessDocSer = p.DOC_SER,
                   @newUpperProcessDocNbr = p.DOC_NBR,
                   @newUpperProcessFileSeq = p.FILE_SEQ,
                   @newUpperProcessFileTyp = p.FILE_TYP,
                   @newUpperProcessFileSer = p.FILE_SER,
                   @newUpperProcessFileNbr = p.FILE_NBR
            FROM IPASPROD.IP_PROC p where p.PROC_TYP = @newUpperProcTyp AND p.PROC_NBR = @newUpperProcNbr

            IF @newUpperProcessFileProcTyp IS NULL OR @newUpperProcessFileProcNbr IS NULL
                BEGIN
                    THROW 51000, 'New upper process file process id is empty !', 1;
                END

            -- Update upper process
            UPDATE IPASPROD.IP_PROC  SET UPPER_PROC_TYP = @newUpperProcTyp,   UPPER_PROC_NBR = @newUpperProcNbr where PROC_TYP = @userdocProcTyp AND PROC_NBR = @userdocProcNbr

            IF @newUpperProcessDocOri IS NOT NULL
                AND @newUpperProcessDocLog IS NOT NULL
                AND @newUpperProcessDocSer IS NOT NULL
                AND @newUpperProcessDocNbr IS NOT NULL
                BEGIN
                    -- Update IP_USERDOC. Set affected document id.
                    UPDATE IPASPROD.IP_USERDOC SET AFFECTED_DOC_ORI = @newUpperProcessDocOri,
                                                   AFFECTED_DOC_LOG = @newUpperProcessDocLog,
                                                   AFFECTED_DOC_SER = @newUpperProcessDocSer,
                                                   AFFECTED_DOC_NBR = @newUpperProcessDocNbr
                    where DOC_ORI = @docOri AND DOC_LOG = @docLog AND DOC_SER = @docSer AND DOC_NBR = @docNbr
                END
            ELSE
                BEGIN
                    IF @newUpperProcessFileSeq IS NOT NULL
                        AND @newUpperProcessFileTyp IS NOT NULL
                        AND @newUpperProcessFileSer IS NOT NULL
                        AND @newUpperProcessFileNbr IS NOT NULL
                        BEGIN
                            -- Update IP_USERDOC. Set affected document id.
                            UPDATE IPASPROD.IP_USERDOC
                            SET AFFECTED_DOC_ORI = NULL,
                                AFFECTED_DOC_LOG = NULL,
                                AFFECTED_DOC_SER = NULL,
                                AFFECTED_DOC_NBR = NULL
                            where DOC_ORI = @docOri AND DOC_LOG = @docLog AND DOC_SER = @docSer AND DOC_NBR = @docNbr
                        END
                    ELSE
                        BEGIN
                            THROW 51000, 'Upper process file id and userdoc id are empty !', 1;
                        END
                END

            IF (@fileProcTyp <> @newUpperProcessFileProcTyp) OR (@fileProcNbr <> @newUpperProcessFileProcNbr)
                BEGIN
                    EXEC changeTopFileProcessId @procType= @userdocProcTyp, @procNumber = @userdocProcNbr, @newTopFileProcTyp = @newUpperProcessFileProcTyp, @newTopFileProcNbr = @newUpperProcessFileProcNbr;
                END

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
;


