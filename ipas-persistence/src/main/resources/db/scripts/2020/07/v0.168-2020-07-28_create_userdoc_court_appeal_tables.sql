--liquibase formatted sql

--changeset mmurlev:168.1
create table EXT_CORE.CF_JUDICIAL_ACT_TYPE
(
 ID integer not null,
 ACT_TYPE_NAME varchar(100),
 primary key (ID)
);

--changeset mmurlev:168.2

INSERT INTO EXT_CORE.CF_JUDICIAL_ACT_TYPE (ID, ACT_TYPE_NAME) VALUES (1, 'Решение');
INSERT INTO EXT_CORE.CF_JUDICIAL_ACT_TYPE (ID, ACT_TYPE_NAME) VALUES (2, 'Определение');

--changeset mmurlev:168.3
create table EXT_CORE.IP_USERDOC_COURT_APPEAL
(
    COURT_APPEAL_ID integer not null,
    DOC_ORI varchar(4) not null,
    DOC_LOG varchar(1) not null,
    DOC_SER numeric(4) not null,
    DOC_NBR numeric(15) not null,
    COURT_ID integer,
    COURT_CASE_NBR varchar(20),
    COURT_CASE_DATE datetime,
    JUDICIAL_ACT_NBR varchar(20),
    JUDICIAL_ACT_DATE datetime,
    JUDICIAL_ACT_TYPE_ID integer,
    primary key (COURT_APPEAL_ID, DOC_ORI,DOC_SER,DOC_LOG,DOC_NBR),
    constraint IP_USERDOC_COURT_APPEAL_COURT_ID_FK
        foreign key (COURT_ID) references EXT_LEGAL.Courts(CourtId),
    constraint IP_USERDOC_COURT_APPEAL_JUDICIAL_ACT_TYPE_ID_FK
        foreign key (JUDICIAL_ACT_TYPE_ID) references EXT_CORE.CF_JUDICIAL_ACT_TYPE(ID),

);

