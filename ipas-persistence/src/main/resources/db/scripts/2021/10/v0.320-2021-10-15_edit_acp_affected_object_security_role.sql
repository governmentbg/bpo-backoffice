--liquibase formatted sql

--changeset mmurlev:320.1
update ext_user.CF_SECURITY_ROLES set DESCRIPTION = 'АНП: Преглед на панел "Нарушени права"'
where ROLE_NAME = 'acp.acp-affected-objects-data'