--liquibase formatted sql

--changeset mmurlev:211.1
create table EXT_CORE.ENOTIF
(
      GAZNO                 varchar(25)
        constraint enotif_pkey
            primary key,
    NOT_DATE              datetime,
    PUB_DATE              datetime,
    WEEKNO                varchar(10),
    NOTLANG               varchar(10),
    PAID_COUNT            integer,
    LICENCE_NEWNAME_COUNT integer,
    LICENCE_BIRTH_COUNT   integer,
    CREATED_COUNT         integer,
    PROCESSED_COUNT       integer,
    CORRECTION_COUNT      integer,
    PROLONG_COUNT         integer,
    NEWBASE_COUNT         integer,
    RESTRICT_COUNT        integer,
    NEWNAME_COUNT         integer,
    DEATH_COUNT           integer,
    BIRTH_COUNT           integer
);


--changeset mmurlev:211.2
create table ext_core.ENOTIF_MARK
(
    ID integer not null,
    FILE_SEQ      varchar(2)  not null,
    FILE_TYP      varchar(1)  not null,
    FILE_SER      numeric(4)  not null,
    FILE_NBR      numeric(10) not null,
    GAZNO VARCHAR(25),
    transaction_type varchar(50),
    constraint ENOTIF_MARK_PK
        primary key nonclustered (ID,FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER),
          FOREIGN KEY (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) REFERENCES IP_MARK (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER),
          FOREIGN KEY (GAZNO ) REFERENCES ext_core.ENOTIF (GAZNO)
);