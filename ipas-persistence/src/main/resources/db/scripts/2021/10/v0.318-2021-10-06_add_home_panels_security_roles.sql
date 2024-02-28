--liquibase formatted sql

--changeset mmurlev:318.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('ipobjects.waiting-term-zmr-list', 'Начална страница: Изчакване на срокове по ЗМР');


--changeset mmurlev:318.2
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('ipobjects.expired-term-zmr-list', 'Начална страница: Изтекли срокове по ЗМР');