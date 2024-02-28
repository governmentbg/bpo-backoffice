--liquibase formatted sql

--changeset mmurlev:311.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('ipobjects.international-mark-receptions-list', 'Новопостъпили международни марки');

