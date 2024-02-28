--liquibase formatted sql

--changeset ggeorgiev:286.1
drop procedure [IPASPROD].[deleteUserdocDetails];

--changeset ggeorgiev:286.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[deleteUserdocDetails]
    @docOri [varchar](2),
    @docLog [varchar](1),
    @docSer [numeric](4, 0),
    @docNbr [numeric](10, 0)
AS
BEGIN

    SET NOCOUNT ON;
    DECLARE @rowcount INT;

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
    delete FROM EXT_CORE.IP_MARK_GROUND_DATA WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
    delete FROM EXT_CORE.IP_PATENT_GROUND_DATA WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
    delete FROM EXT_CORE.IP_SINGLE_DESIGN_GROUND_DATA WHERE DOC_ORI = @docOri and DOC_LOG = @docLog and DOC_SER = @docSer and DOC_NBR = @docNbr;
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
    SET @rowcount = @@ROWCOUNT;
    delete FROM [IP_DOC_FILES] where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;
    delete FROM [IP_DOC] where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;
    delete FROM [IP_USERDOC_PROCS] where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;
    delete FROM [IP_PROC] where DOC_ORI=@docOri and DOC_LOG=@docLog and DOC_SER=@docSer and DOC_NBR=@docNbr;
    print cast (@rowcount as varchar(max)) + ' record(s) deleted';
END;
