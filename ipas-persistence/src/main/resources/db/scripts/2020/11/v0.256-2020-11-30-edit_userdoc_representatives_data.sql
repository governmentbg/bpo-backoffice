--liquibase formatted sql

--changeset dveizov:255
UPDATE u
SET u.APPLICANT_PERSON_NBR = p.PERSON_NBR,
    u.APPLICANT_ADDR_NBR   = p.ADDR_NBR
FROM EXT_CORE.IP_USERDOC_PERSON p
         JOIN IPASPROD.IP_USERDOC_REPRS re
              on p.DOC_ORI = re.DOC_ORI AND p.DOC_SER = re.DOC_SER AND p.DOC_LOG = re.DOC_LOG AND
                 p.DOC_NBR = re.DOC_NBR and p.PERSON_NBR = re.PERSON_NBR AND p.ADDR_NBR = re.ADDR_NBR
         JOIN IPASPROD.IP_PERSON pe on pe.PERSON_NBR = re.PERSON_NBR
         JOIN IPASPROD.IP_USERDOC u
              on p.DOC_NBR = u.DOC_NBR and p.DOC_ORI = u.DOC_ORI and p.DOC_LOG = u.DOC_LOG and
                 p.DOC_SER = u.DOC_SER
where p.ROLE IN ('REPRESENTATIVE', 'OLD_REPRESENTATIVE', 'NEW_REPRESENTATIVE')
  and p.REPRESENTATIVE_TYP is null
  AND u.APPLICANT_PERSON_NBR is null
  AND u.APPLICANT_ADDR_NBR is NULL
  and pe.PERSON_NAME IN ('Велика Пенчева Христова', '"ОСЦЕОЛА" ЕООД', 'ПАТЕНТ УНИВЕРС');

DELETE p
FROM EXT_CORE.IP_USERDOC_PERSON p
         JOIN IPASPROD.IP_PERSON pe on p.PERSON_NBR = pe.PERSON_NBR
         JOIN IPASPROD.IP_USERDOC u
              on p.DOC_NBR = u.DOC_NBR and p.DOC_ORI = u.DOC_ORI and p.DOC_LOG = u.DOC_LOG and
                 p.DOC_SER = u.DOC_SER
where REPRESENTATIVE_TYP is NULL
  and pe.PERSON_NAME IN ('Велика Пенчева Христова', '"ОСЦЕОЛА" ЕООД', 'ПАТЕНТ УНИВЕРС')
  and u.APPLICANT_PERSON_NBR = p.PERSON_NBR and u.APPLICANT_ADDR_NBR = p.ADDR_NBR;
