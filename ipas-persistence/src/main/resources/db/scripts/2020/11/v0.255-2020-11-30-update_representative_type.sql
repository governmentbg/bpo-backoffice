--liquibase formatted sql

--changeset dveizov:255
UPDATE p
SET p.REPRESENTATIVE_TYP = re.REPRESENTATIVE_TYP
FROM EXT_CORE.IP_USERDOC_PERSON p
         JOIN IPASPROD.IP_USERDOC_REPRS re
              on p.DOC_ORI = re.DOC_ORI AND p.DOC_SER = re.DOC_SER AND p.DOC_LOG = re.DOC_LOG AND
                 p.DOC_NBR = re.DOC_NBR and p.PERSON_NBR = re.PERSON_NBR AND p.ADDR_NBR = re.ADDR_NBR
where p.ROLE IN ('REPRESENTATIVE', 'OLD_REPRESENTATIVE', 'NEW_REPRESENTATIVE')
  and p.REPRESENTATIVE_TYP is null
  AND re.REPRESENTATIVE_TYP = 'RE';

UPDATE d
SET d.REPRESENTATIVE_TYP = 'AG',
    d.PERSON_NBR         = (SELECT a.PERSON_NBR FROM IPASPROD.IP_PERSON a where a.AGENT_CODE = 37),
    d.ADDR_NBR           = 1
FROM EXT_CORE.IP_USERDOC_PERSON d
         JOIN IPASPROD.IP_PERSON p on d.PERSON_NBR = p.PERSON_NBR
WHERE p.PERSON_NAME IN ('Искра Христова и Партньори')
  and d.ROLE IN ('REPRESENTATIVE', 'OLD_REPRESENTATIVE', 'NEW_REPRESENTATIVE')
  AND d.REPRESENTATIVE_TYP IS NULL;

UPDATE d
SET d.REPRESENTATIVE_TYP = 'AG',
    d.PERSON_NBR         = (SELECT a.PERSON_NBR FROM IPASPROD.IP_PERSON a where a.AGENT_CODE = 199),
    d.ADDR_NBR           = 1
FROM EXT_CORE.IP_USERDOC_PERSON d
         JOIN IPASPROD.IP_PERSON p on d.PERSON_NBR = p.PERSON_NBR
WHERE p.PERSON_NAME IN ('Станислава Стефанова')
  and d.ROLE IN ('REPRESENTATIVE', 'OLD_REPRESENTATIVE', 'NEW_REPRESENTATIVE')
  AND d.REPRESENTATIVE_TYP IS NULL;