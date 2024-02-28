--liquibase formatted sql

--changeset mmurlev:309.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-payments','Вторични действия: Преглед на панел "Плащания"');

insert into ext_user.CF_GROUP_SECURITY_ROLE (GROUP_ID, ROLE_NAME, DATE_CREATED, USER_CREATED)
VALUES (1, 'userdoc.userdoc-payments',getdate(), 4);


INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN)
VALUES ('Payments', 'N', 'Плащания', 'Payments');
