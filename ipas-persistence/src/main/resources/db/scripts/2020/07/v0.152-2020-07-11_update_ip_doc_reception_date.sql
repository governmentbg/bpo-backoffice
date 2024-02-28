--liquibase formatted sql

--changeset dveizov:152
UPDATE d
SET d.RECEPTION_DATE = p.RECEPTION_DATE
FROM IPASPROD.IP_DOC d
         INNER JOIN IP_PATENT p
                    ON d.DOC_NBR = p.DOC_NBR AND d.DOC_ORI = p.DOC_ORI AND d.DOC_LOG = p.DOC_LOG AND d.DOC_SER = p.DOC_SER
WHERE d.RECEPTION_DATE is null
  AND p.RECEPTION_DATE is not null