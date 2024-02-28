--liquibase formatted sql

--changeset mmurlev:316.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('payments.edit','Плащания: редакция');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('payments.extended_rights','Плащания: разширени права');
