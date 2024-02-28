--liquibase formatted sql

--changeset mmurlev:315.1
create table EXT_CORE.ACP_AFFECTED_OBJECT
(
    FILE_SEQ      varchar(2)  not null,
    FILE_TYP      varchar(1)  not null,
    FILE_SER      numeric(4)  not null,
    FILE_NBR      numeric(10) not null,
    AO_FILE_SEQ      varchar(2)  not null,
    AO_FILE_TYP      varchar(1)  not null,
    AO_FILE_SER      numeric(4)  not null,
    AO_FILE_NBR      numeric(10) not null,
    constraint ACP_AFFECTED_OBJECT_PK
        primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR,AO_FILE_SEQ,AO_FILE_TYP,AO_FILE_SER,AO_FILE_NBR)
)

--changeset mmurlev:315.2
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('acp.acp-affected-objects-data','АНП: Преглед на панел "Засегнати Обекти"');

--changeset mmurlev:315.3
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('acp.view.own','АНП: Преглед на собствени производства');

--changeset mmurlev:315.4
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('acp.view.all','АНП: Преглед на всички производства');