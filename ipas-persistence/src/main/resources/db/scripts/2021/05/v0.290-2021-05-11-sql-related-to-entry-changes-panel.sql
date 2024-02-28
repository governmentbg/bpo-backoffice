--liquibase formatted sql

--changeset mmurlev:290.1
insert into ext_core.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) values('Entry_changes','S','Промяна на вписването','Entry changes')

--changeset mmurlev:290.2
insert into  ext_core.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) values('ИПРВП','Entry_changes')

--changeset mmurlev:290.3
insert into ext_core.CF_USERDOC_EXTRA_DATA_TO_UI_PANEL(CODE, PANEL) values('DESCRIPTION','Entry_changes')

--changeset mmurlev:290.4
insert into ext_core.CF_USERDOC_EXTRA_DATA_TO_UI_PANEL(CODE, PANEL) values('EFFECTIVE_DATE','Entry_changes')

--changeset mmurlev:290.5
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('userdoc.userdoc-entry-changes-data','Вторични действия: Преглед на панел "Промяна на вписването"');

