--liquibase formatted sql

--changeset mmihova:286.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-international-nice-data','Вторични действия: Преглед на панел "Ограничения на стоки и услуги"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('mark.mark-international-nice-data','Марки: Преглед на панел "Ограничения на стоки и услуги"');

--changeset mmihova:286.2
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) VALUES ('InternationalNiceClassesRestrictions', 'N', 'Ограничения на стоки и услуги', 'International nice classes restrictions');