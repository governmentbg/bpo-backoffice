--liquibase formatted sql

--changeset ggeorgiev:247.1
drop procedure [IPASPROD].[deleteUserdocDetails];

--changeset ggeorgiev:247.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[deleteUserdocDetails]
    @docOri [varchar](2),
    @docLog [varchar](1),
    @docSer [numeric](4, 0),
    @docNbr [numeric](10, 0)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            delete c
            from ext_reception.USERDOC_CORRESPONDENT c
                     join EXT_RECEPTION.RECEPTION_USERDOC_REQUEST rr on rr.ID = c.RECEPTION_USERDOC_REQUEST_ID
            where rr.DOC_ORI = @docOri and rr.DOC_LOG = @docLog and rr.DOC_SER = @docSer and rr.DOC_NBR = @docNbr;

            delete FROM EXT_RECEPTION.RECEPTION_USERDOC_REQUEST where DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;

            delete FROM EXT_CORE.IP_USERDOC_APPROVED_DATA WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_APPROVED_NICE_CLASSES WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_COURT_APPEAL WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_EFILING_DATA WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_GROUND_NICE_CLASSES WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_NICE_CLASSES WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_PERSON WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_REGISTRATION_NUMBER_CHANGE_LOG WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_REVIEWERS WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_ROOT_GROUNDS WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_SINGLE_DESIGN WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_SINGLE_DESIGN_LOCARNO_CLASSES WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
            delete FROM EXT_CORE.IP_USERDOC_SUB_GROUNDS WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;

            delete ruc
            from EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES ruc
                JOIN IP_PROC p on ruc.PROC_NBR = p.PROC_NBR and ruc.PROC_TYP = p.PROC_TYP
            where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;

            delete FROM [IP_USERDOC] where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;
            delete FROM [IP_DOC_FILES] where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;
            delete FROM [IP_DOC] where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;
            delete FROM [IP_USERDOC_PROCS] where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;
            delete FROM [IP_PROC] where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;

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
END;
