--liquibase formatted sql

--changeset mmurlev:309.1
alter table ext_Core.CF_OFFIDOC_TYPE_TEMPLATE add NAME_FILE_CONFIG VARCHAR(50);
