

CREATE TABLE EXT_CORE.IP_FILE_RELATIONSHIP_EXTENDED
(
    FILE_SEQ VARCHAR(2) NOT NULL,
    FILE_TYP VARCHAR(1) NOT NULL,
    FILE_SER NUMERIC(4) NOT NULL,
    FILE_NBR NUMERIC(10) NOT NULL,
    RELATIONSHIP_TYP VARCHAR(3),
    APPLICATION_TYPE varchar(2),
    FILING_NUMBER VARCHAR(50),
    FILING_DATE DATETIME,
    REGISTRATION_NUMBER VARCHAR(50),
    REGISTRATION_DATE DATETIME,
    REGISTRATION_COUNTRY VARCHAR(10),
    CANCELLATION_DATE DATETIME,
    PRIORITY_DATE DATETIME
    CONSTRAINT IP_FILE_RELATIONSHIP_EXTENDED_PK PRIMARY KEY (FILE_SEQ, FILE_TYP, FILE_NBR, FILE_SER)
)
;

ALTER TABLE EXT_CORE.IP_FILE_RELATIONSHIP_EXTENDED
ADD CONSTRAINT IP_FILE_RELATIONSHIP_REL_TYP_FK
FOREIGN KEY (RELATIONSHIP_TYP) REFERENCES CF_RELATIONSHIP_TYPE (RELATIONSHIP_TYP)
;

insert into CF_RELATIONSHIP_TYPE (row_version, relationship_typ, relationship_name, ind_renewal, direct_relationship_name, ind_direct_function, ind_direct_required, inverse_relationship_name, ind_inverse_function, ind_inverse_required)
values(1,'ТМ','Трансформирана марка','N','Трансформирана в','N','N','Трансформирана от','N','N')
;

INSERT INTO EXT_CORE.IP_FILE_RELATIONSHIP_EXTENDED
(file_seq, file_typ, file_ser, file_nbr, relationship_typ, application_type, filing_number, filing_date, registration_number, registration_date, registration_country, cancellation_date, priority_date)
select FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, 'ТМ',TRANSFORM_TYPE,TRANSFORM_NUMBER,FILING_DATE,null,REGISTRATION_DATE,null,CANCELLATION_DATE,PRIORITY_DATE from ext_core.IP_MARK_TRANSFORMATION
;

INSERT INTO EXT_CORE.IP_FILE_RELATIONSHIP_EXTENDED
(file_seq, file_typ, file_ser, file_nbr, relationship_typ, application_type, filing_number, filing_date, registration_number, registration_date, registration_country, cancellation_date, priority_date)
select FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, 'ТП',TRANSFORM_TYPE,APPL_ID,APPL_DATE,PUBL_ID,PUBL_DATE,PUBL_COUNTRY,NULL,NULL from ext_core.IP_PATENT_TRANSFORMATION
;

drop table ext_core.IP_PATENT_TRANSFORMATION
;
drop table ext_core.IP_MARK_TRANSFORMATION
;

