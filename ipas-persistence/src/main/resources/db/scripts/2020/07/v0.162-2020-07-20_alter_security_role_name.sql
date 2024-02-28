--liquibase formatted sql

--changeset mmurlev:162
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Начална страница: Панел моите обекти на ИС' where ROLE_NAME='ipobjects.my-objects'