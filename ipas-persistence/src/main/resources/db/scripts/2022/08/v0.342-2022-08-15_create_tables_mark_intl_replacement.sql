--liquibase formatted sql

--changeset mnakova:342.1
create table EXT_CORE.IP_MARK_INTL_REPLACEMENT_NICE_CLASSES
(
    ROW_VERSION               numeric(9) not null,
    FILE_SEQ                  varchar(2)  not null,
    FILE_TYP                  varchar(1)  not null,
    FILE_SER                  numeric(4)  not null,
    FILE_NBR                  numeric(10) not null,
    NICE_CLASS_CODE           numeric(2)  not null,
    NICE_CLASS_STATUS_WCODE   varchar     not null,
    NICE_CLASS_DESCRIPTION    text,
    NICE_CLASS_EDITION        numeric(2)  not null,
    NICE_CLASS_DESCR_LANG2    text,
    NICE_CLASS_VERSION        varchar(10) not null,
    ALL_TERMS_DECLARATION     varchar,
        constraint PK_IP_MARK_INTL_REPLACEMENT_NICE_CLASSES
            primary key (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE)
);

--changeset mnakova:342.2
create table EXT_CORE.IP_MARK_INTL_REPLACEMENT
(
    FILE_SEQ                  varchar(2)  not null,
    FILE_TYP                  varchar(1)  not null,
    FILE_SER                  numeric(4)  not null,
    FILE_NBR                  numeric(10) not null,
    REGISTRATION_NBR          numeric(10) not null,
    ALL_SERVICES              bit default 1,
    constraint PK_IP_MARK_INTL_REPLACEMENT
        primary key (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
);
