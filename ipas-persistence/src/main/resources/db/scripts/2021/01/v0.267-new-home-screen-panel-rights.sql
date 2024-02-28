--liquibase formatted sql

--changeset ggeorgiev:267
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('ipobjects.last-payments-list', 'Начална страница: Последни плащания');


