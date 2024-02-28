--liquibase formatted sql

--changeset mmurlev:187.1
IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'CF_OFFIDOC_TYPE_TEMPLATE_CF_OFFIDOC_TYPE_fk')
alter table EXT_CORE.CF_OFFIDOC_TYPE_TEMPLATE
drop constraint CF_OFFIDOC_TYPE_TEMPLATE_CF_OFFIDOC_TYPE_fk;

--changeset mmurlev:187.2
IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'CF_USERDOC_TYPE_TO_PERSON_ROLE_USERDOC_TYPE_USERDOC_TYP_fk')
alter table EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE
drop constraint CF_USERDOC_TYPE_TO_PERSON_ROLE_USERDOC_TYPE_USERDOC_TYP_fk;

--changeset mmurlev:187.3
IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'CF_USERDOC_TYPE_TO_UI_PANEL_USERDOC_TYPE_USERDOC_TYP_fk')
alter table EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL
drop constraint CF_USERDOC_TYPE_TO_UI_PANEL_USERDOC_TYPE_USERDOC_TYP_fk;

--changeset mmurlev:187.4
IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'IP_USERDOC_NICE_CLASSES_CF_CLASS_NICE_FK')
alter table EXT_CORE.IP_USERDOC_NICE_CLASSES
drop constraint IP_USERDOC_NICE_CLASSES_CF_CLASS_NICE_FK;