--liquibase formatted sql

--changeset dveizov:157
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('ipobjects.my-userdocs', 'Начална страница:  Панел Моите вторични действия');


