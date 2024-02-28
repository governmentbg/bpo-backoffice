--liquibase formatted sql

--changeset ggeorgiev:280.1

CREATE VIEW [EXT_CORE].[VW_ACTION_INDEX] AS
SELECT a.PROC_TYP, a.PROC_NBR, a.ACTION_NBR, ACTION_TYP, ACTION_DATE, NEW_STATUS_CODE, PRIOR_STATUS_CODE, PRIOR_STATUS_DATE, RESPONSIBLE_USER_ID,
j.YEAR JOURNAL_YEAR, j.BULETIN JOURNAL_BULETIN,  j.SECT JOURNAL_SECT
from IPASPROD.IP_ACTION a
LEFT JOIN EXT_JOURNAL.VW_JOURNAL j on a.PROC_TYP = j.PROC_TYP and a.PROC_NBR = j.PROC_NBR and a.ACTION_NBR = j.ACTION_NBR;


CREATE VIEW [EXT_CORE].[VW_PROC_INDEX] AS
SELECT p.PROC_TYP, p.PROC_NBR, p.RESPONSIBLE_USER_ID, STATUS_CODE, STATUS_DATE
from IPASPROD.IP_PROC p;


CREATE VIEW [EXT_CORE].[VW_USERDOC_INDEX] AS
SELECT ud.DOC_ORI, ud.DOC_LOG, ud.DOC_SER, ud.DOC_NBR,
       p.USERDOC_FILE_SEQ FILE_SEQ, p.USERDOC_FILE_TYP FILE_TYP, p.USERDOC_FILE_SER FILE_SER, p.USERDOC_FILE_NBR FILE_NBR,
       d.EXTERNAL_SYSTEM_ID, d.FILING_DATE, p.PROC_TYP, p.PROC_NBR, p.USERDOC_TYP
from IPASPROD.IP_USERDOC ud
JOIN ipasprod.IP_DOC d on d.DOC_ORI = ud.DOC_ORI and d.DOC_LOG = ud.DOC_LOG and d.DOC_SER = ud.DOC_SER and d.DOC_NBR = ud.DOC_NBR
join ipasprod.IP_PROC p on p.DOC_ORI = d.DOC_ORI and p.DOC_LOG = d.DOC_LOG and d.DOC_SER = p.DOC_SER and d.DOC_NBR = p.DOC_NBR