--liquibase formatted sql

--changeset mmihova:306.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('mark.mark-international-data','Марки: Преглед на панел "Международни данни"');
