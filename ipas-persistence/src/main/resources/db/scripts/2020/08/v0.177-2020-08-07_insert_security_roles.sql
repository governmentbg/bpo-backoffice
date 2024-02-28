--liquibase formatted sql

--changeset dveizov:177
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('ipas.admin.module', 'Достъп до административен модул');


