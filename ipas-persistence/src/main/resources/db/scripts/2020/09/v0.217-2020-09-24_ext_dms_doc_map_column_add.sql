--liquibase formatted sql

--changeset raneva:217
ALTER TABLE EXT_DMS_DOC_MAP ADD ABDOCS_REGISTRATION_DATE datetime;