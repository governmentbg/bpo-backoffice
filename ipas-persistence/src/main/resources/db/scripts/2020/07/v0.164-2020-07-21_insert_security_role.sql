--liquibase formatted sql

--changeset mmurlev:164
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('ipobjects.userdoc-expired-terms', 'Начална страница:  Панел Изтекли срокове по вторични документи');


