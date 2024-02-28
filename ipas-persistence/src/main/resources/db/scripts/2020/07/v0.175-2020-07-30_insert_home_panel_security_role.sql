--liquibase formatted sql

--changeset mmurlev:175
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('ipobjects.international-marks-list', 'Обекти на ИС: Преглед на международни марки');
