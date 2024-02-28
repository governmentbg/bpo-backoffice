CREATE PROCEDURE [IPASPROD].[changeTopFileProcessId] @procType [varchar](4),
                                                     @procNumber numeric(8),
                                                     @newTopFileProcTyp [varchar](4),
                                                     @newTopFileProcNbr numeric(8)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            DECLARE @newFileSeq varchar(2);
            DECLARE @newFileTyp varchar(1);
            DECLARE @newFileSer numeric(4);
            DECLARE @newFileNbr numeric(10);

            SELECT @newFileSeq = p.FILE_SEQ,
                   @newFileTyp = p.FILE_TYP,
                   @newFileSer = p.FILE_SER,
                   @newFileNbr = p.FILE_NBR
            FROM IPASPROD.IP_PROC p where p.PROC_TYP = @newTopFileProcTyp AND p.PROC_NBR = @newTopFileProcNbr

            PRINT 'New File: '+@newFileSeq +'/'+@newFileTyp+ '/'+ CAST(@newFileSer AS varchar)+'/'+ CAST(@newFileNbr AS varchar);

            -- Check if new file id is empty
            IF @newFileSeq IS NULL OR @newFileTyp IS NULL OR @newFileSer IS NULL OR @newFileNbr IS NULL
                BEGIN
                    THROW 51000, 'File id of new top process is empty !', 1;
                END

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
            WHERE p.PROC_TYP = @procType AND p.PROC_NBR = @procNumber

            PRINT 'Old File: '+@fileSeq +'/'+@fileTyp+ '/'+ CAST(@fileSer AS varchar)+'/'+ CAST(@fileNbr AS varchar);

            -- Update file process
            UPDATE IPASPROD.IP_PROC SET FILE_PROC_TYP = @newTopFileProcTyp, FILE_PROC_NBR = @newTopFileProcNbr where PROC_TYP = @procType AND PROC_NBR = @procNumber

            -- Update userdoc data
            IF @docOri IS NOT NULL AND @docLog IS NOT NULL AND @docSer IS NOT NULL AND @docNbr IS NOT NULL
                BEGIN
                    PRINT 'Userdoc ID: '+@docOri +'/'+@docLog+ '/'+ CAST(@docSer AS varchar)+'/'+ CAST(@docNbr AS varchar);

                    -- Insert new record in IP_DOC_FILES for userdoc with new IP_FILE id
                    DELETE FROM IPASPROD.IP_DOC_FILES where DOC_ORI = @docOri AND DOC_LOG = @docLog AND DOC_SER = @docSer AND DOC_NBR = @docNbr AND FILE_SEQ = @fileSeq AND FILE_TYP = @fileTyp AND FILE_SER = @fileSer AND FILE_NBR = @fileNbr;
                    INSERT INTO IPASPROD.IP_DOC_FILES (ROW_VERSION, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PRIOR_EXPIRATION_DATE)
                    VALUES (1, @docOri, @docLog, @docSer, @docNbr,@newFileSeq, @newFileTyp, @newFileSer, @newFileNbr, null);
                    PRINT 'Updated IP_DOC_FILES successfully !';

                    -- Insert new record in IP_USERDOC_PROCS for userdoc with new IP_FILE id
                    DELETE FROM IPASPROD.IP_USERDOC_PROCS where DOC_ORI = @docOri AND DOC_LOG = @docLog AND DOC_SER = @docSer AND DOC_NBR = @docNbr;
                    INSERT INTO IPASPROD.IP_USERDOC_PROCS (ROW_VERSION, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, USERDOC_TYP, USERDOC_FILE_SEQ, USERDOC_FILE_TYP, USERDOC_FILE_SER, USERDOC_FILE_NBR, PROC_TYP, PROC_NBR)
                    VALUES (1, @docOri, @docLog, @docSer, @docNbr, @userdocType, @newFileSeq, @newFileTyp, @newFileSer, @newFileNbr, @procType, @procNumber);
                    PRINT 'Updated IP_USERDOC_PROCS successfully !';

                    -- Update userdoc file id
                    UPDATE IPASPROD.IP_PROC SET USERDOC_FILE_SEQ = @newFileSeq, USERDOC_FILE_TYP = @newFileTyp, USERDOC_FILE_SER = @newFileSer,USERDOC_FILE_NBR = @newFileNbr where PROC_TYP = @procType AND PROC_NBR = @procNumber;
                    PRINT 'Updated IP_PROC.USERDOC_FILE_ID successfully !';
                END

            DECLARE @childProcTyp varchar(4);
            DECLARE @childProcNbr numeric(8);
            DECLARE curson_child CURSOR LOCAL FOR SELECT p.PROC_TYP as childProcTyp, p.PROC_NBR as childProcNbr FROM IPASPROD.IP_PROC p where UPPER_PROC_TYP = @procType AND UPPER_PROC_NBR = @procNumber;
            OPEN curson_child;
            FETCH NEXT FROM curson_child INTO
                @childProcTyp,
                @childProcNbr;

            WHILE @@FETCH_STATUS = 0
                BEGIN
                    PRINT 'Processing record.... '+@childProcTyp +' '+ CAST(@childProcNbr AS varchar);
                    exec changeTopFileProcessId @procType= @childProcTyp, @procNumber = @childProcNbr, @newTopFileProcTyp = @newTopFileProcTyp, @newTopFileProcNbr = @newTopFileProcNbr;
                    FETCH NEXT FROM curson_child INTO @childProcTyp, @childProcNbr;
                END;
            CLOSE curson_child;
            DEALLOCATE curson_child;
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




