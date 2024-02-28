--liquibase formatted sql

--changeset ggeorgiev:282.1


CREATE VIEW [EXT_CORE].[VW_MARK_INDEX] AS
SELECT f.FILE_SEQ, f.FILE_TYP, f.FILE_SER, f.FILE_NBR, f.APPL_TYP, f.APPL_SUBTYP, f.TITLE_LANG2, f.ENTITLEMENT_DATE, f.EXPIRATION_DATE, f.FILING_DATE,
       STUFF
           (
               (
                   SELECT  ';' + DOC_ORI + '/' + DOC_LOG + '/' + CAST(DOC_SER as varchar(MAX)) + '/' + CAST(DOC_NBR as varchar(MAX))
                   FROM IPASPROD.IP_DOC_FILES df
                   where df.FILE_SEQ = f.FILE_SEQ and df.FILE_TYP = f.FILE_TYP and df.FILE_SER = f.FILE_SER and df.FILE_NBR = f.FILE_NBR
                   FOR XMl PATH('')
               ),1,1,''
       ) as DOC_FILES,
       f.PROC_TYP, f.PROC_NBR, f.REGISTRATION_DATE, f.REGISTRATION_DUP, f.REGISTRATION_NBR, p.STATUS_CODE, f.TITLE,
       (select top 1 1 from ipasprod.IP_LOGO l where l.FILE_SEQ = m.FILE_SEQ and l.FILE_TYP = m.FILE_TYP and l.FIlE_SER = m.FILE_SER and l.FILE_NBR = m.FILE_NBR) as IMG,
       mo.PERSON_NAME MAIN_OWNER_PERSON_NAME,
       sp.PERSON_NAME SERVICE_PERSON_NAME,
       STUFF
                     (
                         (
                             SELECT  ';' + cast (PERSON_NBR as varchar(MAX))
                             FROM IPASPROD.IP_MARK_OWNERS mo
                             where mo.FILE_SEQ = f.FILE_SEQ and mo.FILE_TYP = f.FILE_TYP and mo.FILE_SER = f.FILE_SER and mo.FILE_NBR = f.FILE_NBR
                             FOR XMl PATH('')
                         ),1,1,''
                     ) as OWNER_PERSON_NBRS,
       STUFF
                             (
                                 (
                                     SELECT  ';' + cast (PERSON_NBR as varchar(MAX))
                                     FROM IPASPROD.IP_MARK_REPRS mr
                                     where mr.FILE_SEQ = f.FILE_SEQ and mr.FILE_TYP = f.FILE_TYP and mr.FILE_SER = f.FILE_SER and mr.FILE_NBR = f.FILE_NBR
                                     FOR XMl PATH('')
                                 ),1,1,''
                             ) as REPRESENTATIVE_PERSON_NBRS,
       SIGN_WCODE
from  IPASPROD.IP_MARK m
join IPASPROD.IP_FILE f on m.FILE_NBR = f.FILE_NBR and m.FILE_SEQ = f.FILE_SEQ and m.FILE_TYP = f.FILE_TYP and m.FILE_SER = f.FILE_SER
JOIN ipasprod.IP_PROC p on f.PROC_NBR = p.PROC_NBR and f.PROC_TYP = p.PROC_TYP
left JOIN ipasprod.ip_PERSON mo on mo.PERSON_NBR = f.MAIN_OWNER_PERSON_NBR
left JOIN ipasprod.ip_PERSON sp on sp.PERSON_NBR = f.SERVICE_PERSON_NBR