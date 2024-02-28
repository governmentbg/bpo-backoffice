--liquibase formatted sql

--changeset ggeorgiev:325.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('ipobjects.history', 'История на обектите');

