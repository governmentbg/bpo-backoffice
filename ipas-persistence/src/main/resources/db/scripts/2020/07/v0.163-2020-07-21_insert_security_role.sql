--liquibase formatted sql

--changeset mmurlev:163
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('ipobjects.userdoc-waiting-terms', 'Начална страница:  Панел Изчакване на срокове по вторични документи');


