--liquibase formatted sql

--changeset dveizov:182.1
DELETE
FROM EXT_CORE.IP_USERDOC_PERSON
WHERE CONCAT(DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, '-', PERSON_NBR, '-', ROLE) IN (
    SELECT CONCAT(p.DOC_ORI, p.DOC_LOG, p.DOC_SER, p.DOC_NBR, '-', p.PERSON_NBR, '-', p.ROLE)
    FROM EXT_CORE.IP_USERDOC_PERSON p
             JOIN IPASPROD.IP_PERSON pe ON pe.PERSON_NBR = p.PERSON_NBR
             LEFT JOIN IP_PROC pro
                       ON pro.DOC_ORI = p.DOC_ORI AND pro.DOC_LOG = p.DOC_LOG AND pro.DOC_SER = p.DOC_SER AND
                          pro.DOC_NBR = p.DOC_NBR
             LEFT JOIN EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE pr
                       ON p.ROLE = pr.ROLE AND pr.USERDOC_TYP = pro.USERDOC_TYP
             LEFT JOIN ext_core.IP_USERDOC_PERSON p1
                       ON p.doc_ori = p1.doc_ori AND p.doc_log = p1.doc_log AND p.doc_ser = p1.doc_ser AND
                          p.doc_nbr = p1.doc_nbr AND p1.ROLE = 'APPLICANT'
             LEFT JOIN ipasprod.ip_person prs1 ON p1.PERSON_NBR = prs1.PERSON_NBR
    WHERE pr.USERDOC_TYP IS NULl
      AND p1.PERSON_NBR IS NOT NULL
      AND pe.person_name = prs1.PERSON_NAME
      AND pro.USERDOC_TYP NOT IN ('ПОЗ', 'ПОДОЗ', 'ПРОМОЗ', 'ПЛ')
);

--changeset dveizov:182.2
UPDATE p
SET p.ROLE = 'APPLICANT'
FROM EXT_CORE.IP_USERDOC_PERSON p
         JOIN IPASPROD.IP_PERSON pe ON pe.PERSON_NBR = p.PERSON_NBR
         LEFT JOIN IP_PROC pro ON pro.DOC_ORI = p.DOC_ORI AND pro.DOC_LOG = p.DOC_LOG AND pro.DOC_SER = p.DOC_SER AND pro.DOC_NBR = p.DOC_NBR
         LEFT JOIN EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE pr ON p.ROLE = pr.ROLE AND pr.USERDOC_TYP = pro.USERDOC_TYP
         LEFT JOIN ext_core.IP_USERDOC_PERSON p1 ON p.doc_ori = p1.doc_ori AND p.doc_log = p1.doc_log AND p.doc_ser = p1.doc_ser AND p.doc_nbr = p1.doc_nbr AND p1.ROLE = 'APPLICANT'
         LEFT JOIN ipasprod.ip_person prs1 ON p1.PERSON_NBR = prs1.PERSON_NBR
WHERE pr.USERDOC_TYP IS NULl
  AND p1.PERSON_NBR IS NULL
  AND pro.USERDOC_TYP NOT IN ('ПОЗ', 'ПОДОЗ', 'ПРОМОЗ', 'ПЛ')
  AND ((SELECT count(*)
        from EXT_CORE.IP_USERDOC_PERSON po
        where po.DOC_ORI = p.DOC_ORI
          AND po.DOC_LOG = p.DOC_LOG
          AND po.DOC_SER = p.DOC_SER
          AND po.DOC_NBR = p.DOC_NBR
          AND po.PERSON_NBR = p.PERSON_NBR
          ) = 1);
