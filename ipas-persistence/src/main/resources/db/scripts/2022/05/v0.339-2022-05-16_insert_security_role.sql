--liquibase formatted sql

--changeset raneva:339.1
insert into EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) values ('offidoc.offidoc-notifications', 'Офис документи: Преглед на нотификации по офис документи');