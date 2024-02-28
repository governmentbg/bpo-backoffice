--liquibase formatted sql

--changeset ggeorgiev:134
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('ipas.change.working.date', 'Работна дата: Промяна');