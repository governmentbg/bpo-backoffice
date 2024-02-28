--liquibase formatted sql

--changeset dveizov:272
CREATE VIEW VW_IP_PERSON_RELATIONS as
(
SELECT mr.PERSON_NBR,
       'REPRESENTATIVE'                                                          as ROLE,
       CONCAT(mr.FILE_SEQ, '/', mr.FILE_TYP, '/', mr.FILE_SER, '/', mr.FILE_NBR) as DOSSIER_ID,
       fte.CODE                                                                  as DOSSIER_TYPE,
       s.STATUS_NAME                                                             as DOSSIER_STATUS,
       p.CREATION_DATE                                                           as DOSSIER_DATE,
       mr.REPRESENTATIVE_TYP                                                     as REPRESENTATIVE_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT
FROM IPASPROD.IP_MARK_REPRS mr
         join EXT_CORE.CF_FILE_TYP_EXTENDED fte on fte.FILE_TYP = mr.FILE_TYP
         join IPASPROD.IP_PROC p ON mr.FILE_SEQ = p.FILE_SEQ AND mr.FILE_TYP = p.FILE_TYP AND mr.FILE_SER = p.FILE_SER AND mr.FILE_NBR = p.FILE_NBR
         join IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE
UNION ALL
SELECT mo.PERSON_NBR,
       'OWNER'                                                                   as ROLE,
       CONCAT(mo.FILE_SEQ, '/', mo.FILE_TYP, '/', mo.FILE_SER, '/', mo.FILE_NBR) as DOSSIER_ID,
       fte.CODE                                                                  as DOSSIER_TYPE,
       s.STATUS_NAME                                                             as DOSSIER_STATUS,
       p.CREATION_DATE                                                           as DOSSIER_DATE,
       NULL                                                                      as REPRESENTATIVE_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT
FROM IPASPROD.IP_MARK_OWNERS mo
         join EXT_CORE.CF_FILE_TYP_EXTENDED fte on fte.FILE_TYP = mo.FILE_TYP
         join IPASPROD.IP_PROC p ON mo.FILE_SEQ = p.FILE_SEQ AND mo.FILE_TYP = p.FILE_TYP AND mo.FILE_SER = p.FILE_SER AND mo.FILE_NBR = p.FILE_NBR
         join IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE
UNION ALL
SELECT pr.PERSON_NBR,
       'REPRESENTATIVE'                                                          as ROLE,
       CONCAT(pr.FILE_SEQ, '/', pr.FILE_TYP, '/', pr.FILE_SER, '/', pr.FILE_NBR) as DOSSIER_ID,
       fte.CODE                                                                  as DOSSIER_TYPE,
       s.STATUS_NAME                                                             as DOSSIER_STATUS,
       p.CREATION_DATE                                                           as DOSSIER_DATE,
       pr.REPRESENTATIVE_TYP                                                     as REPRESENTATIVE_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT
FROM IPASPROD.IP_PATENT_REPRS pr
         join EXT_CORE.CF_FILE_TYP_EXTENDED fte on fte.FILE_TYP = pr.FILE_TYP
         join IPASPROD.IP_PROC p ON pr.FILE_SEQ = p.FILE_SEQ AND pr.FILE_TYP = p.FILE_TYP AND pr.FILE_SER = p.FILE_SER AND pr.FILE_NBR = p.FILE_NBR
         join IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE
UNION ALL
SELECT pi.PERSON_NBR,
       'INVENTOR'                                                                as ROLE,
       CONCAT(pi.FILE_SEQ, '/', pi.FILE_TYP, '/', pi.FILE_SER, '/', pi.FILE_NBR) as DOSSIER_ID,
       fte.CODE                                                                  as DOSSIER_TYPE,
       s.STATUS_NAME                                                             as DOSSIER_STATUS,
       p.CREATION_DATE                                                           as DOSSIER_DATE,
       NULL                                                                      as REPRESENTATIVE_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT
FROM IPASPROD.IP_PATENT_INVENTORS pi
         join EXT_CORE.CF_FILE_TYP_EXTENDED fte on fte.FILE_TYP = pi.FILE_TYP
         join IPASPROD.IP_PROC p ON pi.FILE_SEQ = p.FILE_SEQ AND pi.FILE_TYP = p.FILE_TYP AND pi.FILE_SER = p.FILE_SER AND pi.FILE_NBR = p.FILE_NBR
         join IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE
UNION ALL
SELECT po.PERSON_NBR,
       'OWNER'                                                                   as ROLE,
       CONCAT(po.FILE_SEQ, '/', po.FILE_TYP, '/', po.FILE_SER, '/', po.FILE_NBR) as DOSSIER_ID,
       fte.CODE                                                                  as DOSSIER_TYPE,
       s.STATUS_NAME                                                             as DOSSIER_STATUS,
       p.CREATION_DATE                                                           as DOSSIER_DATE,
       NULL                                                                      as REPRESENTATIVE_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT_TYPE,
       NULL                                                                      as MAIN_IP_OBJECT
FROM IPASPROD.IP_PATENT_OWNERS po
         join EXT_CORE.CF_FILE_TYP_EXTENDED fte on fte.FILE_TYP = po.FILE_TYP
         join IPASPROD.IP_PROC p ON po.FILE_SEQ = p.FILE_SEQ AND po.FILE_TYP = p.FILE_TYP AND po.FILE_SER = p.FILE_SER AND po.FILE_NBR = p.FILE_NBR
         join IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE
UNION ALL
SELECT m.SERVICE_PERSON_NBR,
       'CORRESPONDENCE_ADDRESS'                                              as ROLE,
       CONCAT(m.FILE_SEQ, '/', m.FILE_TYP, '/', m.FILE_SER, '/', m.FILE_NBR) as DOSSIER_ID,
       fte.CODE                                                              as DOSSIER_TYPE,
       s.STATUS_NAME                                                         as DOSSIER_STATUS,
       p.CREATION_DATE                                                       as DOSSIER_DATE,
       NULL                                                                  as REPRESENTATIVE_TYPE,
       NULL                                                                  as MAIN_IP_OBJECT_TYPE,
       NULL                                                                  as MAIN_IP_OBJECT
FROM IPASPROD.IP_MARK m
         join EXT_CORE.CF_FILE_TYP_EXTENDED fte on fte.FILE_TYP = m.FILE_TYP
         join IPASPROD.IP_PROC p ON m.FILE_SEQ = p.FILE_SEQ AND m.FILE_TYP = p.FILE_TYP AND m.FILE_SER = p.FILE_SER AND m.FILE_NBR = p.FILE_NBR
         join IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE
WHERE m.SERVICE_PERSON_NBR is not null
UNION ALL
SELECT pa.SERVICE_PERSON_NBR,
       'CORRESPONDENCE_ADDRESS'                                              as ROLE,
       CONCAT(p.FILE_SEQ, '/', p.FILE_TYP, '/', p.FILE_SER, '/', p.FILE_NBR) as DOSSIER_ID,
       fte.CODE                                                              as DOSSIER_TYPE,
       s.STATUS_NAME                                                         as DOSSIER_STATUS,
       p.CREATION_DATE                                                       as DOSSIER_DATE,
       NULL                                                                  as REPRESENTATIVE_TYPE,
       NULL                                                                  as MAIN_IP_OBJECT_TYPE,
       NULL                                                                  as MAIN_IP_OBJECT
FROM IPASPROD.IP_PATENT pa
         join EXT_CORE.CF_FILE_TYP_EXTENDED fte on fte.FILE_TYP = pa.FILE_TYP
         join IPASPROD.IP_PROC p ON pa.FILE_SEQ = p.FILE_SEQ AND pa.FILE_TYP = p.FILE_TYP AND pa.FILE_SER = p.FILE_SER AND pa.FILE_NBR = p.FILE_NBR
         join IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE
WHERE pa.SERVICE_PERSON_NBR is not null
UNION ALL
SELECT u.APPLICANT_PERSON_NBR,
       'CORRESPONDENCE_ADDRESS'                                          as ROLE,
       CONCAT(u.DOC_ORI, '/', u.DOC_LOG, '/', u.DOC_SER, '/', u.DOC_NBR) as DOSSIER_ID,
       'USERDOC'                                                         as DOSSIER_TYPE,
       s.STATUS_NAME                                                     as DOSSIER_STATUS,
       p.CREATION_DATE                                                   as DOSSIER_DATE,
       NULL                                                              as REPRESENTATIVE_TYPE,
       fte.CODE                                                          as MAIN_IP_OBJECT_TYPE,
       CONCAT(p.USERDOC_FILE_SEQ, '/', p.USERDOC_FILE_TYP, '/', p.USERDOC_FILE_SER, '/',
              p.USERDOC_FILE_NBR)                                        as MAIN_IP_OBJECT
FROM IPASPROD.IP_USERDOC u
         join IPASPROD.IP_PROC p
              on u.DOC_ORI = p.DOC_ORI AND u.DOC_LOG = p.DOC_LOG AND u.DOC_SER = p.DOC_SER AND u.DOC_NBR = p.DOC_NBR
         join EXT_CORE.CF_FILE_TYP_EXTENDED fte on fte.FILE_TYP = p.USERDOC_FILE_TYP
         join IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE
WHERE u.APPLICANT_PERSON_NBR is not null
UNION ALL
SELECT up.PERSON_NBR,
       up.ROLE                                                               as ROLE,
       CONCAT(up.DOC_ORI, '/', up.DOC_LOG, '/', up.DOC_SER, '/', up.DOC_NBR) as DOSSIER_ID,
       'USERDOC'                                                             as DOSSIER_TYPE,
       s.STATUS_NAME                                                         as DOSSIER_STATUS,
       p.CREATION_DATE                                                       as DOSSIER_DATE,
       up.REPRESENTATIVE_TYP                                                 as REPRESENTATIVE_TYPE,
       fte.CODE                                                              as MAIN_IP_OBJECT_TYPE,
       CONCAT(p.USERDOC_FILE_SEQ, '/', p.USERDOC_FILE_TYP, '/', p.USERDOC_FILE_SER, '/',
              p.USERDOC_FILE_NBR)                                            as MAIN_IP_OBJECT
FROM EXT_CORE.IP_USERDOC_PERSON up
         join IPASPROD.IP_PROC p
              on up.DOC_ORI = p.DOC_ORI AND up.DOC_LOG = p.DOC_LOG AND up.DOC_SER = p.DOC_SER AND up.DOC_NBR = p.DOC_NBR
         join EXT_CORE.CF_FILE_TYP_EXTENDED fte on fte.FILE_TYP = p.USERDOC_FILE_TYP
         join IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE
    )
