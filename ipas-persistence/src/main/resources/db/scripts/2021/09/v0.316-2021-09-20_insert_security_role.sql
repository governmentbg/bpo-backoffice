--liquibase formatted sql

--changeset mmihova:316.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('ipas.payments.module', 'Достъп до модул за плащания');

