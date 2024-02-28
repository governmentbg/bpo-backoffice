--liquibase formatted sql

--changeset mmurlev:273.1
drop procedure [IPASPROD].[deletePatentDetails];

--changeset mmurlev:273.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[deletePatentDetails]
    @fileSeq [varchar](2),
    @fileTyp [varchar](1),
    @fileSer [numeric](4, 0),
    @fileNbr [numeric](10, 0)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            --1. Delete userdocs
            --reception tables
            delete c
            from ext_reception.USERDOC_CORRESPONDENT c
                     join EXT_RECEPTION.RECEPTION_USERDOC_REQUEST rr on rr.ID = c.RECEPTION_USERDOC_REQUEST_ID
                     JOIN IP_DOC_FILES df on rr.DOC_LOG = df.DOC_LOG AND rr.DOC_ORI = df.DOC_ORI and rr.DOC_SER = df.DOC_SER and rr.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete rr
            FROM EXT_RECEPTION.RECEPTION_USERDOC_REQUEST rr
                     JOIN IP_DOC_FILES df on rr.DOC_LOG = df.DOC_LOG AND rr.DOC_ORI = df.DOC_ORI and rr.DOC_SER = df.DOC_SER and rr.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            --ext tables
            delete e
            FROM EXT_CORE.IP_USERDOC_APPROVED_DATA e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_APPROVED_NICE_CLASSES e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_COURT_APPEAL e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_EFILING_DATA e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_GROUND_NICE_CLASSES e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_NICE_CLASSES e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_PERSON e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_REGISTRATION_NUMBER_CHANGE_LOG e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_REVIEWERS e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_ROOT_GROUNDS e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_SINGLE_DESIGN e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_SINGLE_DESIGN_LOCARNO_CLASSES e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            delete e
            FROM EXT_CORE.IP_USERDOC_SUB_GROUNDS e
                     JOIN IP_DOC_FILES df on e.DOC_LOG = df.DOC_LOG AND e.DOC_ORI = df.DOC_ORI and e.DOC_SER = df.DOC_SER and e.DOC_NBR = df.DOC_NBR
            where df.FILE_SEQ = @fileSeq and df.FILE_TYP = @fileTyp and df.FILE_SER = @fileSer and df.FILE_NBR = @fileNbr;

            -- IP_USERDOC_TYPES
            delete d
            from IP_DOC_FILES df
                     join IP_USERDOC_TYPES d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr;

            -- actions for all the userdocs associated with a patent (only if a workflow has been executed for any of the user documents)
            delete a
            from IP_DOC_FILES df
                     join IP_DOC d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI and d.FILE_NBR is null
                     join IP_PROC p on d.DOC_LOG=p.DOC_LOG and d.DOC_ORI=p.DOC_ORI and d.DOC_SER=p.DOC_SER and d.DOC_NBR=p.DOC_NBR
                     join IP_ACTION a on a.PROC_NBR=p.PROC_NBR and a.PROC_TYP=p.PROC_TYP
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr

            -- IP_USERDOC
            delete d
            from IP_DOC_FILES df
                     join IP_USERDOC d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr;

            -- IP_DOC
            delete d
            from IP_DOC_FILES df
                     join IP_DOC d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI and d.FILE_NBR is null
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr;

            --process responsible users log changes for all the userdocs associated with a patent
            delete ruc
            from IP_DOC_FILES df
             join IP_DOC d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI and d.FILE_NBR is null
             join IP_PROC p on d.DOC_LOG=p.DOC_LOG and d.DOC_ORI=p.DOC_ORI and d.DOC_SER=p.DOC_SER and d.DOC_NBR=p.DOC_NBR
             JOIN EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES ruc on ruc.PROC_NBR = p.PROC_NBR and ruc.PROC_TYP = p.PROC_TYP
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr

            -- processes for all the userdocs associated with a patent
            delete p
            from IP_DOC_FILES df
                     join IP_DOC d on d.DOC_LOG=df.DOC_LOG and d.DOC_SER=df.DOC_SER and d.DOC_NBR=df.DOC_NBR and d.DOC_ORI=df.DOC_ORI and d.FILE_NBR is null
                     join IP_PROC p on d.DOC_LOG=p.DOC_LOG and d.DOC_ORI=p.DOC_ORI and d.DOC_SER=p.DOC_SER and d.DOC_NBR=p.DOC_NBR
            where df.FILE_SEQ=@fileSeq and df.FILE_TYP=@fileTyp and df.file_ser=@fileSer and df.FILE_NBR=@fileNbr

            --2. Delete patent itself ---------------------------------------------------------

            delete c
            from EXT_RECEPTION.CORRESPONDENT c
                     join EXT_RECEPTION.RECEPTION_REQUEST rr on rr.ID = c.RECEPTION_REQUEST_ID
            where rr.FILE_SEQ = @fileSeq and rr.FILE_TYP = @fileTyp and rr.FILE_SER = @fileSer and rr.FILE_NBR = @fileNbr;

            delete from EXT_RECEPTION.RECEPTION_REQUEST where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete
            FROM [IP_PATENT_WORDS]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_PATENT_REPRS]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_PATENT_OWNERS]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_PATENT_INVENTORS]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_PATENT_NOTES]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete FROM EXT_CORE.IP_PATENT_ATTACHMENT
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete FROM EXT_CORE.IP_PATENT_DETAILS
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete from EXT_CORE.IP_FILE_RECORDAL
            where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete from EXT_CORE.IP_FILE_RELATIONSHIP_EXTENDED
            where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete from EXT_CORE.IP_OBJECT_EFILING_DATA
            where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete from EXT_CORE.PLANT
            where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete from EXT_CORE.SINGLE_DESIGN_EXTENDED
            where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete from EXT_CORE.SPC_EXTENDED
            where FILE_SEQ = @fileSeq and FILE_TYP = @fileTyp and FILE_SER = @fileSer and FILE_NBR = @fileNbr;

            delete from IP_FILE_RELATIONSHIP
            where FILE_SEQ1=@fileSeq and FILE_TYP1=@fileTyp and FILE_NBR1=@fileNbr and FILE_SER1=@fileSer;

            delete from IP_FILE_RELATIONSHIP
            where FILE_SEQ2=@fileSeq and FILE_TYP2=@fileTyp and FILE_NBR2=@fileNbr and FILE_SER2=@fileSer;

            delete
            FROM [IP_PATENT]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_FILE]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;


            -- IP_ACTION
            delete a
            FROM [IP_ACTION] a
                     join [IP_PROC] p on a.PROC_NBR=p.PROC_NBR and a.PROC_TYP=p.PROC_TYP
                     join IP_DOC pt on pt.FILE_NBR=p.FILE_NBR and pt.FILE_SEQ=p.FILE_SEQ and pt.FILE_SER=p.FILE_SER and pt.FILE_TYP=p.FILE_TYP
            where pt.FILE_SEQ=@fileSeq and pt.FILE_TYP=@fileTyp and pt.FILE_NBR=@fileNbr and pt.FILE_SER=@fileSer;

            --3. delete office docs
            delete a
            from IP_OFFIDOC od
                     join IP_PROC odp on od.OFFIDOC_PROC_NBR=odp.PROC_NBR and od.OFFIDOC_PROC_TYP=odp.PROC_TYP
                     join IP_ACTION a on a.PROC_NBR=odp.PROC_NBR and a.PROC_TYP=odp.PROC_TYP
                     join IP_PROC p on od.PROC_NBR=p.PROC_NBR and od.PROC_TYP=p.PROC_TYP
                     join IP_DOC pt on pt.FILE_NBR=p.FILE_NBR and pt.FILE_SEQ=p.FILE_SEQ and pt.FILE_SER=p.FILE_SER and pt.FILE_TYP=p.FILE_TYP
            where pt.FILE_SEQ=@fileSeq and pt.FILE_TYP=@fileTyp and pt.FILE_NBR=@fileNbr and pt.FILE_SER=@fileSer;


            delete od
            from IP_OFFIDOC od
                     join IP_PROC p on od.PROC_NBR=p.PROC_NBR and od.PROC_TYP=p.PROC_TYP
                     join IP_DOC pt on pt.FILE_NBR=p.FILE_NBR and pt.FILE_SEQ=p.FILE_SEQ and pt.FILE_SER=p.FILE_SER and pt.FILE_TYP=p.FILE_TYP
            where pt.FILE_SEQ=@fileSeq and pt.FILE_TYP=@fileTyp and pt.FILE_NBR=@fileNbr and pt.FILE_SER=@fileSer;

            delete odp
            from IP_PROC odp
                     join IP_PROC p on odp.UPPER_PROC_NBR=p.PROC_NBR and odp.UPPER_PROC_TYP=p.PROC_TYP
                     join IP_DOC pt on pt.FILE_NBR=p.FILE_NBR and pt.FILE_SEQ=p.FILE_SEQ and pt.FILE_SER=p.FILE_SER and pt.FILE_TYP=p.FILE_TYP
            where pt.FILE_SEQ=@fileSeq and pt.FILE_TYP=@fileTyp and pt.FILE_NBR=@fileNbr and pt.FILE_SER=@fileSer;

            --4. Delete remaining records
            -- main patent responsible user changes
            delete ruc
            FROM [IP_PROC] p
                     join IP_DOC pt on pt.FILE_NBR=p.FILE_NBR and pt.FILE_SEQ=p.FILE_SEQ and pt.FILE_SER=p.FILE_SER and pt.FILE_TYP=p.FILE_TYP
                     JOIN EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES ruc on ruc.PROC_NBR = p.PROC_NBR and ruc.PROC_TYP = p.PROC_TYP
            where pt.FILE_SEQ=@fileSeq and pt.FILE_TYP=@fileTyp and pt.FILE_NBR=@fileNbr and pt.FILE_SER=@fileSer;

            -- main patent IP_PROC
            delete p
            FROM [IP_PROC] p
                     join IP_DOC pt on pt.FILE_NBR=p.FILE_NBR and pt.FILE_SEQ=p.FILE_SEQ and pt.FILE_SER=p.FILE_SER and pt.FILE_TYP=p.FILE_TYP
            where pt.FILE_SEQ=@fileSeq and pt.FILE_TYP=@fileTyp and pt.FILE_NBR=@fileNbr and pt.FILE_SER=@fileSer;

            -- main patent IP_DOC
            delete
            FROM [IP_DOC]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;

            delete
            FROM [IP_DOC_FILES]
            where FILE_SEQ=@fileSeq and FILE_TYP=@fileTyp and FILE_NBR=@fileNbr and FILE_SER=@fileSer;
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        DECLARE @ErrorSeverity INT,
            @ErrorNumber  INT,
            @ErrorMessage NVARCHAR(4000),
            @ErrorState INT,
            @ErrorLine INT,
            @ErrorProc NVARCHAR(200)
        SET @ErrorSeverity = ERROR_SEVERITY()
        SET @ErrorNumber  = ERROR_NUMBER()
        SET @ErrorMessage = ERROR_MESSAGE()
        SET @ErrorState  = ERROR_STATE()

        -- Not all errors generate an error state, to set to 1 if it's zero
        IF @ErrorState = 0
            SET @ErrorState = 1
        -- If the error renders the transaction as uncommittable or we have open transactions, we may want to rollback
        IF @@TRANCOUNT > 0
            BEGIN
                ROLLBACK TRANSACTION
            END
        RAISERROR (@ErrorMessage , @ErrorSeverity, @ErrorState, @ErrorNumber)
    END CATCH
    RETURN @@ERROR
END
