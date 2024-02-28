--liquibase formatted sql

--changeset murlev:358.1
create table ipasprod.CF_CLASS_CPC
(
    ROW_VERSION numeric(9) not null,
    CPC_EDITION_CODE varchar(20) not null,
    CPC_SECTION_CODE varchar not null,
    CPC_CLASS_CODE varchar(2) not null,
    CPC_SUBCLASS_CODE varchar(2) not null,
    CPC_GROUP_CODE varchar(10) not null,
    CPC_SUBGROUP_CODE varchar(10) not null,
    CPC_NAME varchar(4000),
    CPC_LATEST_VERSION varchar(20),
    SYMBOL varchar(20),
    constraint CF_CLASS_CPC_PK
        primary key nonclustered (CPC_EDITION_CODE, CPC_SECTION_CODE, CPC_CLASS_CODE, CPC_SUBCLASS_CODE, CPC_GROUP_CODE,
            CPC_SUBGROUP_CODE)
);

--changeset murlev:358.2
create table ipasprod.IP_PATENT_CPC_CLASSES
(
    ROW_VERSION numeric(9) not null,
    FILE_SEQ varchar(2) not null,
    FILE_TYP varchar not null,
    FILE_SER numeric(4) not null,
    FILE_NBR numeric(10) not null,
    CPC_EDITION_CODE varchar(20) not null,
    CPC_SECTION_CODE varchar not null,
    CPC_CLASS_CODE varchar(2) not null,
    CPC_SUBCLASS_CODE varchar(2) not null,
    CPC_GROUP_CODE varchar(10) not null,
    CPC_SUBGROUP_CODE varchar(10) not null,
    CPC_QUALIFICATION_CODE varchar(2) not null,
    CPC_SYMBOL_POSITION varchar,
    CPC_SYMBOL_CAPTURE_DATE datetime,
    CPC_WPUBLISH_VALIDATED varchar,
    constraint IP_PATENT_CPC_CLASSES_PK
        primary key nonclustered (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER, CPC_EDITION_CODE, CPC_SECTION_CODE,
            CPC_CLASS_CODE, CPC_SUBCLASS_CODE, CPC_GROUP_CODE, CPC_SUBGROUP_CODE,
            CPC_QUALIFICATION_CODE),
    constraint CF_CLASS_CPC_FK
        foreign key (CPC_EDITION_CODE, CPC_SECTION_CODE, CPC_CLASS_CODE, CPC_SUBCLASS_CODE, CPC_GROUP_CODE,
                     CPC_SUBGROUP_CODE) references ipasprod.CF_CLASS_CPC
            on update cascade,
    constraint IP_PATENT_FK
        foreign key (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) references ipasprod.IP_PATENT
            on delete cascade
);

--changeset murlev:358.3
insert into IPASPROD.EXT_CONFIG_PARAM values ('CPC_LATEST_VERSION', null);

--changeset murlev:358.4
INSERT INTO IpasProd.EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION, DATE_CREATED) VALUES (N'patent.patent-cpc-data', N'Патенти: Преглед на панел "Класификация по КПК"', N'2023-12-13 17:44:38.460');
