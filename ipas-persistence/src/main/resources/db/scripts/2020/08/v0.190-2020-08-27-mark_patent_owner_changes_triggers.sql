--liquibase formatted sql

--changeset ggeorgiev:190.1 splitStatements:false

CREATE TRIGGER IPASPROD.[MARK_OWNER_CHANGES]
    ON IPASPROD.IP_MARK_OWNERS
    AFTER INSERT, UPDATE, DELETE
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
        PERSON_NBR,
        ADDR_NBR,
        GETDATE()
    FROM
        inserted i;

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
        PERSON_NBR,
        ADDR_NBR,
        GETDATE()
    FROM
        deleted d;
END;

ALTER TABLE IPASPROD.IP_MARK_OWNERS ENABLE TRIGGER MARK_OWNER_CHANGES;


--changeset ggeorgiev:190.2 splitStatements:false

CREATE TRIGGER IPASPROD.[PATENT_OWNER_CHANGES]
    ON IPASPROD.IP_PATENT_OWNERS
    AFTER INSERT, UPDATE, DELETE
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
        PERSON_NBR,
        ADDR_NBR,
        GETDATE()
    FROM
        inserted i;

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
        PERSON_NBR,
        ADDR_NBR,
        GETDATE()
    FROM
        deleted d;
END;

ALTER TABLE IPASPROD.IP_PATENT_OWNERS ENABLE TRIGGER PATENT_OWNER_CHANGES;