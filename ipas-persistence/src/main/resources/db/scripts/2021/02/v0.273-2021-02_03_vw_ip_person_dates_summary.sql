--liquibase formatted sql

--changeset dveizov:273
CREATE VIEW AUDIT.VW_IP_PERSON_DATES_SUMMARY AS
(
SELECT a.PERSON_NBR,
       MIN(CASE WHEN a.REVTYPE = 0 THEN a.date ELSE NULL END) AS CREATION_DATE,
       MAX(CASE WHEN a.REVTYPE = 1 THEN a.date ELSE NULL END) AS LAST_UPDATE_DATE
FROM (
         SELECT p.PERSON_NBR, p.REVTYPE, ri.timestamp as date
         FROM AUDIT.IP_PERSON p
                  JOIN AUDIT.revinfo ri on ri.REV = p.REV
         UNION ALL
         SELECT pa.PERSON_NBR, pa.REVTYPE, ri.timestamp as date
         FROM AUDIT.IP_PERSON_ADDRESSES pa
                  JOIN AUDIT.revinfo ri on ri.REV = pa.REV
     ) as a
group by a.PERSON_NBR
    )