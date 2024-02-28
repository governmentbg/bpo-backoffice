--liquibase formatted sql

--changeset raneva:301.1
drop table EXT_CORE.CF_DD_CATEGORIZATION_TAGS;

--changeset raneva:301.2
create table EXT_CORE.CF_DD_CATEGORIZATION_TAGS
(
    ID numeric(5) constraint PK_CF_DD_CATEGORIZATION_TAGS primary key,
    FILE_TYP varchar(1) not null,
    USERDOC_TYP varchar(7),
    DOSSIER_TYPE varchar(200),
    CATEGORIES varchar(400),
    TAGS varchar (400),
    FETCH_FROM_PARENT bit default 0,
    CONSTRAINT CF_DD_CATEGORIZATION_TAGS_UNIQ UNIQUE (FILE_TYP, USERDOC_TYP)
);

--changeset raneva:301.3
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (1, 'N', null, 'TM', 'Application_Formal,Application_AG', null);
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (2, 'D', null, 'TM', 'Application_Formal,Application_AG', null);

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (3, 'N', 'ОПО', 'TM', 'Opposition_Substance,Opposition_Termination,Opposition_Suspension', null);
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (4, 'D', 'ОПО', 'TM', 'Opposition_Substance,Opposition_Termination,Opposition_Suspension', null);

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (5, 'N', 'ППР', 'TM', 'Transfer,Recordal_Procedure', 'TRANSFER,TM_REGISTERED');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (6, 'D', 'ППР', 'TM', 'Transfer,Recordal_Procedure', 'TRANSFER,TM_REGISTERED');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (7, 'N', 'ППЗ', 'TM', 'Transfer,Recordal_Procedure', 'TRANSFER,TM_APPLICATION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (8, 'D', 'ППЗ', 'TM', 'Transfer,Recordal_Procedure', 'TRANSFER,TM_APPLICATION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (9, 'N', 'ИЧПРМ', 'TM', 'Transfer,Recordal_Procedure', 'TRANSFER,TM_APPLICATION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (10, 'D', 'ИЧПРМ', 'TM', 'Transfer,Recordal_Procedure', 'TRANSFER,TM_APPLICATION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (11, 'N', 'ИЧПЗМ', 'TM', 'Transfer,Recordal_Procedure', 'TRANSFER,TM_APPLICATION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (12, 'D', 'ИЧПЗМ', 'TM', 'Transfer,Recordal_Procedure', 'TRANSFER,TM_APPLICATION');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (13, 'N', 'НИЛ', 'TM', 'Licence,Recordal_Procedure', 'LICENCE,LICENCE_NONEXCLUSIVE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (14, 'D', 'НИЛ', 'TM', 'Licence,Recordal_Procedure', 'LICENCE,LICENCE_NONEXCLUSIVE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (15, 'N', 'Лиц', 'TM', 'Licence,Recordal_Procedure', 'LICENCE,LICENCE_EXCLUSIVE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (16, 'D', 'Лиц', 'TM', 'Licence,Recordal_Procedure', 'LICENCE,LICENCE_EXCLUSIVE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (17, 'N', 'ПЛ', 'TM', 'Licence,Recordal_Procedure', 'LICENCE,LICENCE_EXCLUSIVE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (18, 'D', 'ПЛ', 'TM', 'Licence,Recordal_Procedure', 'LICENCE,LICENCE_EXCLUSIVE');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (19, 'N', 'ОЗ', 'TM', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (20, 'D', 'ОЗ', 'TM', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (21, 'N', 'ПОДОЗ', 'TM', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (22, 'D', 'ПОДОЗ', 'TM', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (23, 'N', 'ПОЗ', 'TM', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (24, 'D', 'ПОЗ', 'TM', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (25, 'N', 'ПРОМОЗ', 'TM', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (26, 'D', 'ПРОМОЗ', 'TM', 'Pledge,Recordal_Procedure', 'PLEDGE');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (27, 'N', 'ВОМ', 'TM', 'Security,Recordal_Procedure', 'SECURITY_MEASURE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (28, 'D', 'ВОМ', 'TM', 'Security,Recordal_Procedure', 'SECURITY_MEASURE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (29, 'N', 'ПОМ', 'TM', 'Security,Recordal_Procedure', 'SECURITY_MEASURE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (30, 'D', 'ПОМ', 'TM', 'Security,Recordal_Procedure', 'SECURITY_MEASURE');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (31, 'N', 'ВМН', 'TM', 'Bankruptcy,Recordal_Procedure', 'BANKRUPTCY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (32, 'D', 'ВМН', 'TM', 'Bankruptcy,Recordal_Procedure', 'BANKRUPTCY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (33, 'N', 'ОМН', 'TM', 'Bankruptcy,Recordal_Procedure', 'BANKRUPTCY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (34, 'D', 'ОМН', 'TM', 'Bankruptcy,Recordal_Procedure', 'BANKRUPTCY');


insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (35, 'N', 'ЖРО', 'TM', 'Appeal,Dispute_Procedure', 'APPEAL,AGAINST_OPPOSITION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (36, 'D', 'ЖРО', 'TM', 'Appeal,Dispute_Procedure', 'APPEAL,AGAINST_OPPOSITION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (37, 'N', 'ЖО', 'TM', 'Appeal,Dispute_Procedure', 'APPEAL,AGAINST_REFUSAL');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (38, 'D', 'ЖО', 'TM', 'Appeal,Dispute_Procedure', 'APPEAL,AGAINST_REFUSAL');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (39, 'N', 'ЖПП', 'TM', 'Appeal,Dispute_Procedure', 'APPEAL,AGAINST_TERMINATION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (40, 'D', 'ЖПП', 'TM', 'Appeal,Dispute_Procedure', 'APPEAL,AGAINST_TERMINATION');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (41, 'N', 'ИЗРM', 'TM', 'Cancellation,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (42, 'D', 'ИЗРM', 'TM', 'Cancellation,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (43, 'N', 'СЗ', 'ТМ', 'Cancellation,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (44, 'D', 'СЗ', 'ТМ', 'Cancellation,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (45, 'N', 'ИОСПЗ', 'ТМ', 'Cancellation,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (46, 'D', 'ИОСПЗ', 'ТМ', 'Cancellation,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (47, 'N', 'ИОРМ', 'ТМ', 'Cancellation,Dispute_Procedure', 'REVOCATION');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (48, 'D', 'ИОРМ', 'ТМ', 'Cancellation,Dispute_Procedure', 'REVOCATION');


insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS, FETCH_FROM_PARENT)
values (49, 'N', 'ИСВП', '', '', '', 1);
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS, FETCH_FROM_PARENT)
values (50, 'D', 'ИСВП', '', '', '', 1);
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS, FETCH_FROM_PARENT)
values (51, 'N', 'ИПРВП', '', '', '', 1);
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS, FETCH_FROM_PARENT)
values (52, 'D', 'ИПРВП', '', '', '', 1);