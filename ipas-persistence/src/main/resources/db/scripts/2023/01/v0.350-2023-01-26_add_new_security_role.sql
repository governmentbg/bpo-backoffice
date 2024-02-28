--liquibase formatted sql

--changeset mmurlev:350
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('home_panels.corresp-exact', 'Панел "Обща кореспонденция"');