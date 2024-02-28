--liquibase formatted sql

--changeset mmurlev:349.1
insert into ext_core.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) values ('GenericRecordal','S','Данни по вписването','Generic recordal');

--changeset mmurlev:349.2
insert into ext_core.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) values('ИИИ','GenericRecordal');
insert into ext_core.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) values('ОМН','GenericRecordal');

--changeset mmurlev:349.3
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-generic-recordal','Вторични действия: Преглед на панел "Данни за вписването"');

--changeset mmurlev:349.4
insert into ext_Core.CF_USERDOC_EXTRA_DATA_TO_UI_PANEL (CODE, PANEL) VALUES('EFFECTIVE_DATE','GenericRecordal');
insert into ext_Core.CF_USERDOC_EXTRA_DATA_TO_UI_PANEL (CODE, PANEL) VALUES('DESCRIPTION','GenericRecordal');
