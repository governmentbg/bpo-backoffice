--liquibase formatted sql

--changeset ggeorgiev:323.1
drop view [EXT_CORE].[VW_USERDOC_INDEX];

--changeset ggeorgiev:323.2


CREATE VIEW [EXT_CORE].[VW_USERDOC_INDEX] AS
SELECT ud.DOC_ORI, ud.DOC_LOG, ud.DOC_SER, ud.DOC_NBR,
       p.USERDOC_FILE_SEQ FILE_SEQ, p.USERDOC_FILE_TYP FILE_TYP, p.USERDOC_FILE_SER FILE_SER, p.USERDOC_FILE_NBR FILE_NBR,
       d.EXTERNAL_SYSTEM_ID, d.FILING_DATE, p.PROC_TYP, p.PROC_NBR, p.USERDOC_TYP, p.STATUS_CODE
from IPASPROD.IP_USERDOC ud
         JOIN ipasprod.IP_DOC d on d.DOC_ORI = ud.DOC_ORI and d.DOC_LOG = ud.DOC_LOG and d.DOC_SER = ud.DOC_SER and d.DOC_NBR = ud.DOC_NBR
         join ipasprod.IP_PROC p on p.DOC_ORI = d.DOC_ORI and p.DOC_LOG = d.DOC_LOG and d.DOC_SER = p.DOC_SER and d.DOC_NBR = p.DOC_NBR