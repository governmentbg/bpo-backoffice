--liquibase formatted sql

--changeset raneva:302.1
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (53, 'Д', null, 'DS', 'Application_Formal', null);

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (54, 'Д', 'ППР', 'DS', 'Transfer,Recordal_Procedure', 'TRANSFER,DS_REGISTERED');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (55, 'Д', 'ППЗ', 'DS', 'Transfer,Recordal_Procedure', 'TRANSFER,DS_APPLICATION');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (56, 'Д', 'НИЛ', 'DS', 'Licence,Recordal_Procedure', 'LICENCE,LICENCE_NONEXCLUSIVE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (57, 'Д', 'Лиц', 'DS', 'Licence,Recordal_Procedure', 'LICENCE,LICENCE_EXCLUSIVE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (58, 'Д', 'ПЛ', 'DS', 'Licence,Recordal_Procedure', 'LICENCE,LICENCE_EXCLUSIVE');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (59, 'Д', 'ОЗ', 'DS', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (60, 'Д', 'ПОДОЗ', 'DS', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (61, 'Д', 'ПОЗ', 'DS', 'Pledge,Recordal_Procedure', 'PLEDGE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (62, 'Д', 'ПРОМОЗ', 'DS', 'Pledge,Recordal_Procedure', 'PLEDGE');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (63, 'Д', 'ВОМ', 'DS', 'Security,Recordal_Procedure', 'SECURITY_MEASURE');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (64, 'Д', 'ПОМ', 'DS', 'Security,Recordal_Procedure', 'SECURITY_MEASURE');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (65, 'Д', 'ВМН', 'DS', 'Bankruptcy,Recordal_Procedure', 'BANKRUPTCY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (66, 'Д', 'ОМН', 'DS', 'Bankruptcy,Recordal_Procedure', 'BANKRUPTCY');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (67, 'Д', 'ЖО', 'DS', 'Appeal,Dispute_Procedure', 'APPEAL,AGAINST_REFUSAL');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (68, 'Д', 'ЖПП', 'DS', 'Appeal,Dispute_Procedure', 'APPEAL,AGAINST_TERMINATION');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (69, 'Д', 'ИЗРД', 'DS', 'Cancellation,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (70, 'Д', 'СЗ', 'DS', 'Cancellation,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (71, 'Д', 'ИОСПЗ', 'DS', 'Cancellation,Dispute_Procedure', 'INVALIDITY');
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS)
values (72, 'Д', 'ИОДЗ', 'DS', 'Cancellation,Dispute_Procedure', 'REVOCATION');

insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS, FETCH_FROM_PARENT)
values (73, 'Д', 'ИСВП', '', '', '', 1);
insert into  EXT_CORE.CF_DD_CATEGORIZATION_TAGS(ID, FILE_TYP, USERDOC_TYP, DOSSIER_TYPE, CATEGORIES, TAGS, FETCH_FROM_PARENT)
values (74, 'Д', 'ИПРВП', '', '', '', 1);

