INSERT INTO EXT_CORE.CF_USERDOC_PERSON_ROLE (ROLE)
VALUES ('REPRESENTATIVE');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE (USERDOC_TYP, ROLE)
SELECT u.USERDOC_TYP,
       'REPRESENTATIVE'
FROM IPASPROD.CF_USERDOC_TYPE u;


UPDATE a
SET a.APPLICANT_PERSON_NBR = r.PERSON_NBR,
    a.APPLICANT_ADDR_NBR   = r.ADDR_NBR
FROM IP_USERDOC a
         INNER JOIN IP_USERDOC_REPRS r
                    on a.DOC_NBR = r.DOC_NBR and a.DOC_ORI = r.DOC_ORI and a.DOC_LOG = r.DOC_LOG and
                       a.DOC_SER = r.DOC_SER
where r.REPRESENTATIVE_TYP = 'AS';

INSERT INTO EXT_CORE.IP_USERDOC_PERSON (ROW_VERSION, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, PERSON_NBR, ADDR_NBR, ROLE,
                                        NOTES)
SELECT 1                as ROW_NUMBER,
       u.DOC_ORI,
       u.DOC_LOG,
       u.DOC_SER,
       u.DOC_NBR,
       u.PERSON_NBR     as PERSON_NBR,
       u.ADDR_NBR       as ADDR_NBR,
       'REPRESENTATIVE' as ROLE,
       null             as NOTES
FROM IPASPROD.IP_USERDOC_REPRS u
         JOIN IPASPROD.IP_PERSON_ADDRESSES pa
              on pa.PERSON_NBR = u.PERSON_NBR AND pa.ADDR_NBR = u.ADDR_NBR
where u.PERSON_NBR is not null
  AND u.ADDR_NBR is not null
  and u.REPRESENTATIVE_TYP <> 'AS';



