--liquibase formatted sql

--changeset dveizov:282.1
DROP VIEW VW_IP_PERSON;

--changeset dveizov:282.2
CREATE VIEW VW_IP_PERSON as
(
select                 p.PERSON_NBR,
                       p.PERSON_NAME,
                       p.PERSON_WCODE,
                       p.NATIONALITY_COUNTRY_CODE,
                       pa.CITY_NAME,
                       pa.ADDR_STREET,
                       pa.ZIPCODE,
                       p.EMAIL,
                       p.TELEPHONE,
                       pa.RESIDENCE_COUNTRY_CODE,
                       p.GRAL_PERSON_ID_NBR as NEWER_VERSION,
                       p.GRAL_PERSON_ID_TYP as HIERARCHY_ROLE,
                       (CASE
                            WHEN p.AGENT_CODE is not null THEN CAST(p.AGENT_CODE as varchar)
                            WHEN ep.PARTNERSHIP_CODE is not null THEN ep.PARTNERSHIP_CODE
                            ELSE NULL
                           END)             as REPRESENTATIVE_NUMBER,
    WIPO_IDENTIFIER  = NULL,
    CREATION_DATE    = su.CREATION_DATE,
    LAST_UPDATE_DATE = (CASE
                            WHEN su.LAST_UPDATE_DATE is not null THEN su.LAST_UPDATE_DATE
                            WHEN (su.LAST_UPDATE_DATE is null AND su.CREATION_DATE is not null) THEN su.CREATION_DATE
                            ELSE NULL
        END)
from IPASPROD.IP_PERSON p
         JOIN IPASPROD.IP_PERSON_ADDRESSES pa on p.PERSON_NBR = pa.PERSON_NBR
         LEFT JOIN EXT_AGENT.EXTENDED_PARTNERSHIP ep on p.PERSON_NBR = ep.PERSON_NBR
         LEFT JOIN AUDIT.VW_IP_PERSON_DATES_SUMMARY su on p.PERSON_NBR = su.PERSON_NBR
    );
