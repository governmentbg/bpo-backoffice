--liquibase formatted sql

--changeset mmurlev:150
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('ipobjects.my-objects', 'Начална страница: Панел Моите заявки');


