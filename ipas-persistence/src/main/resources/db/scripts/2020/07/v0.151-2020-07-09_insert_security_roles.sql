--liquibase formatted sql

--changeset mmurlev:151
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('ipobjects.expired-term-list', 'Начална страница: Панел изтекли срокове');


