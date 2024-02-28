--liquibase formatted sql

--changeset mmurlev:198.1
create table EXT_CORE.IP_USERDOC_ESIGNATURE
(
    DOC_ORI       varchar(4)  not null,
    DOC_LOG       varchar(1)  not null,
    DOC_SER       numeric(4)  not null,
    DOC_NBR       numeric(15) not null,
    ES_USER_ID    numeric(5)
        constraint IP_OBJECT_ESIGNATURE_USER_ID_FK
            references IP_USER,
    ES_USER       varchar(500),
    ES_USER_NAME  varchar(500),
    ES_USER_EMAIL varchar(200),
    ES_VALID_FROM datetime,
    ES_VALID_TO   datetime,
    ES_DATE       datetime,
    constraint IP_USERDOC_ESIGNATURE_PK
        primary key nonclustered (DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR),
    foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER) references IP_USERDOC
        on update cascade on delete cascade
)

--changeset mmurlev:198.2
create table EXT_CORE.IP_OBJECT_ESIGNATURE
(
    FILE_SEQ      varchar(2)  not null,
    FILE_TYP      varchar(1)  not null,
    FILE_SER      numeric(4)  not null,
    FILE_NBR      numeric(10) not null,
    ES_USER_ID    numeric(5)
        constraint IP_OBJECT_ESIGNATURE_USER_FK
            references IP_USER,
    ES_USER       varchar(500),
    ES_USER_NAME  varchar(500),
    ES_USER_EMAIL varchar(200),
    ES_VALID_FROM datetime,
    ES_VALID_TO   datetime,
    ES_DATE       datetime,
    constraint IP_OBJECT_ESIGNATURE_PK
        primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR),
    foreign key (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) references IP_PATENT
        on update cascade on delete cascade
)




