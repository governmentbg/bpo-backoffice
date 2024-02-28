--liquibase formatted sql

--changeset mmurlev:313
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('home_panels.zm-and-zmr', 'Home панели за МР и ЗМР');