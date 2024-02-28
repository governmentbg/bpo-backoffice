--liquibase formatted sql

--changeset mmihova:317.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('payments.administration', 'Плащания: администриране');
