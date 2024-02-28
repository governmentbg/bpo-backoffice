--liquibase formatted sql

--changeset dveizov:137
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Вторични действия: Преглед на новопостъпили вторични действия' where ROLE_NAME = 'userdoc.view-last-added';
