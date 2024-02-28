--liquibase formatted sql

--changeset dveizov:342
create table EXT_CORE.CF_OFFIDOC_TYPE_STATIC_TEMPLATE
(
    OFFIDOC_TYP      varchar(8)   not null
        constraint CF_OFFIDOC_TYPE_STATIC_TEMPLATE_CF_OFFIDOC_TYPE_fk
            references IPASPROD.CF_OFFIDOC_TYPE,
    STATIC_FILE_NAME varchar(100) not null,
    constraint CF_OFFIDOC_TYPE_STATIC_TEMPLATE_pk
        primary key nonclustered (OFFIDOC_TYP, STATIC_FILE_NAME)
);

