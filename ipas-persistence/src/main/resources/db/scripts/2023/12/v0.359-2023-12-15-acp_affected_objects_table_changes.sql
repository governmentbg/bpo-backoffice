--liquibase formatted sql

--changeset mnakova:359.1
create table EXT_CORE.ACP_EXTERNAL_AFFECTED_OBJECT
(
    ID                  INT IDENTITY(1,1) PRIMARY KEY,
    EXTERNAL_ID         VARCHAR(250),
    REGISTRATION_NUMBER VARCHAR(250),
    NAME                VARCHAR(2500)
)

--changeset mnakova:359.2
create table EXT_CORE.ACP_AFFECTED_OBJECTS
(
    ID             INT IDENTITY(1,1) PRIMARY KEY,
    FILE_SEQ       varchar(2)  not null,
    FILE_TYP       varchar(1)  not null,
    FILE_SER       numeric(4)  not null,
    FILE_NBR       numeric(10) not null,
    AO_FILE_SEQ    varchar(2),
    AO_FILE_TYP    varchar(1),
    AO_FILE_SER    numeric(4),
    AO_FILE_NBR    numeric(10),
    EXTERNAL_AO_ID INT
        constraint ACP_AO_EXTERNAL_FK
            references EXT_CORE.ACP_EXTERNAL_AFFECTED_OBJECT
)

--changeset mnakova:359.3
    INSERT INTO EXT_CORE.ACP_AFFECTED_OBJECTS (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, AO_FILE_SEQ, AO_FILE_TYP, AO_FILE_SER, AO_FILE_NBR)
SELECT FILE_SEQ,
       FILE_TYP,
       FILE_SER,
       FILE_NBR,
       AO_FILE_SEQ,
       AO_FILE_TYP,
       AO_FILE_SER,
       AO_FILE_NBR
FROM EXT_CORE.ACP_AFFECTED_OBJECT
