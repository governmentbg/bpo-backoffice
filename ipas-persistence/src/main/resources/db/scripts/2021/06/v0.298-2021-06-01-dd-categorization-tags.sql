--liquibase formatted sql

--changeset raneva:298.1 splitStatements:false

create table EXT_CORE.CF_DD_CATEGORIZATION_TAGS
(
    ID numeric(3) constraint PK_CF_DD_CATEGORIZATION_TAGS primary key,
    FILE_TYP varchar(1) not null,
    USERDOC_TYP varchar(7),
    DOSSIER_TYPE varchar(200),
    CATEGORIES varchar(400),
    TAGS varchar (400),


    constraint CF_DD_CATEGORIZATION_TAGS_FILE_TYP_FK
        foreign key (FILE_TYP) references IPASPROD.CF_FILE_TYPE
            on update cascade on delete cascade,
    constraint CF_DD_CATEGORIZATION_TAGS_USERDOC_TYP_FK
        foreign key (USERDOC_TYP) references IPASPROD.CF_USERDOC_TYPE
            on update cascade on delete cascade,
    CONSTRAINT CF_DD_CATEGORIZATION_TAGS_UNIQ UNIQUE (FILE_TYP, USERDOC_TYP)
);

--changeset raneva:298.2 splitStatements:false

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (1, 'N', null, 'TM', 'Application,Application AG', null);
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (2, 'N', 'ОПО', 'TM', 'Opposition,Opposition_Termination,Opposition_Suspension', null);
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (3, 'N', 'ППР', 'TM', 'Recordal', 'TRANSFER,TM_REGISTERED');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (4, 'N', 'ППЗ', 'TM', 'Recordal', 'TRANSFER,TM_APPLICATION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (5, 'N', 'НИЛ', 'TM', 'Recordal', 'LICENCE,LICENCE_NONEXCLUSIVE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (6, 'N', 'Лиц', 'TM', 'Recordal', 'LICENCE,LICENCE_EXCLUSIVE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (7, 'N', 'ОЗ', 'TM', 'Recordal', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (8, 'N', 'ВОМ', 'TM', 'Recordal', 'SECURITY_MEASURE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (9, 'N', 'ВМН', 'TM', 'Recordal', 'BANKRUPTCY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (10, 'N', 'ЖРО', 'TM', 'Dispute,Dispute_Procedure', 'APPEAL,AGAINST_OPPOSITION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (11, 'N', 'ЖО', 'TM', 'Dispute,Dispute_Procedure', 'APPEAL,AGAINST_REFUSAL');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (12, 'N', 'ЖПП', 'TM', 'Dispute,Dispute_Procedure', 'APPEAL,AGAINST_TERMINATION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (13, 'N', 'ИЗРM', 'TM', 'Dispute,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
    values (14, 'N', 'ИОРМ', 'TM', 'Dispute,Dispute_Procedure', 'REVOCATION');
