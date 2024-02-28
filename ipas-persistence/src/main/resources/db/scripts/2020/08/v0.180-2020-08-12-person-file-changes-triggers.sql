--liquibase formatted sql

--changeset ggeorgiev:180.1
create table IPASPROD.IP_FILE_OWNER_CHANGES
(
    FILE_SEQ                 varchar(2)     not null,
    FILE_TYP                 varchar(1)     not null,
    FILE_SER                 numeric(4)     not null,
    FILE_NBR                 numeric(10)    not null,
    PERSON_NBR               numeric(8)     not null,
    ADDR_NBR                 numeric(4),
    DATE_CHANGED             datetime       not null
);
create index IP_FILE_OWNER_CHANGES_FILE_INDEX
    on IPASPROD.IP_FILE_OWNER_CHANGES (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER);
create index IP_FILE_OWNER_CHANGES_DATE_CHANGED
    on IPASPROD.IP_FILE_OWNER_CHANGES (DATE_CHANGED);

alter table IPASPROD.IP_FILE_OWNER_CHANGES
    add constraint IP_FILE_OWNER_CHANGES_IP_FILE_FK
        foreign key (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) references IPASPROD.IP_FILE (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) on update cascade on delete cascade
;
--changeset ggeorgiev:180.2 splitStatements:false

CREATE TRIGGER IPASPROD.[FILE_OWNER_CHANGES]
    ON IPASPROD.IP_PERSON
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO IPASPROD.IP_FILE_OWNER_CHANGES(
        FILE_SEQ,
        FILE_TYP,
        FILE_SER,
        FILE_NBR,
        PERSON_NBR,
        DATE_CHANGED
    )
    SELECT
        FILE_SEQ,
        FILE_TYP,
        FILE_SER,
        FILE_NBR,
        i.PERSON_NBR,
        GETDATE()
    FROM
         inserted i
         join IPASPROD.VW_FILE_OWNERS o on o.PERSON_NBR = i.PERSON_NBR;
END;

ALTER TABLE IPASPROD.IP_PERSON ENABLE TRIGGER FILE_OWNER_CHANGES;



--changeset ggeorgiev:180.3 splitStatements:false

CREATE TRIGGER IPASPROD.[FILE_OWNER_ADDRESS_CHANGES]
    ON IPASPROD.IP_PERSON_ADDRESSES
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO IPASPROD.IP_FILE_OWNER_CHANGES(
        FILE_SEQ,
        FILE_TYP,
        FILE_SER,
        FILE_NBR,
        PERSON_NBR,
        ADDR_NBR,
        DATE_CHANGED
    )
    SELECT
        FILE_SEQ,
        FILE_TYP,
        FILE_SER,
        FILE_NBR,
        i.PERSON_NBR,
        i.ADDR_NBR,
        GETDATE()
    FROM
         inserted i
        JOIN IPASPROD.VW_FILE_OWNERS o on o.PERSON_NBR = i.PERSON_NBR and o.ADDR_NBR = i.ADDR_NBR;
END;

ALTER TABLE IPASPROD.IP_PERSON_ADDRESSES ENABLE TRIGGER FILE_OWNER_ADDRESS_CHANGES;
