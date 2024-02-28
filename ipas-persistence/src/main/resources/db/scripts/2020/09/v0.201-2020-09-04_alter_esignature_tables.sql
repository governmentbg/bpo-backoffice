--liquibase formatted sql

--changeset mmurlev:201.1
drop table ext_core.IP_USERDOC_ESIGNATURE

--changeset mmurlev:201.2
drop table ext_core.IP_OBJECT_ESIGNATURE

--changeset mmurlev:201.3
create table EXT_CORE.IP_OBJECT_EFILING_DATA
(
    FILE_SEQ      varchar(2)  not null,
    FILE_TYP      varchar(1)  not null,
    FILE_SER      numeric(4)  not null,
    FILE_NBR      numeric(10) not null,
    LOG_USER_NAME varchar(75),
    ES_USER       varchar(500),
    ES_USER_NAME  varchar(500),
    ES_USER_EMAIL varchar(200),
    ES_VALID_FROM datetime,
    ES_VALID_TO   datetime,
    ES_DATE       datetime,
    constraint IP_OBJECT_EFILING_DATA_PK
        primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
)

--changeset mmurlev:201.4
create table EXT_CORE.IP_USERDOC_EFILING_DATA
(
    DOC_ORI       varchar(4)  not null,
    DOC_LOG       varchar(1)  not null,
    DOC_SER       numeric(4)  not null,
    DOC_NBR       numeric(15) not null,
    LOG_USER_NAME varchar(75),
    ES_USER       varchar(500),
    ES_USER_NAME  varchar(500),
    ES_USER_EMAIL varchar(200),
    ES_VALID_FROM datetime,
    ES_VALID_TO   datetime,
    ES_DATE       datetime,
    constraint IP_USERDOC_EFILING_DATA_PK
        primary key nonclustered (DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR),
    foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER) references IP_USERDOC
        on update cascade on delete cascade
)



