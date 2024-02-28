--liquibase formatted sql

--changeset mmurlev:310.1
UPDATE ext_Core.CF_OFFIDOC_TYPE_TEMPLATE SET NAME_FILE_CONFIG = 'TEMPLATE' where 1=1