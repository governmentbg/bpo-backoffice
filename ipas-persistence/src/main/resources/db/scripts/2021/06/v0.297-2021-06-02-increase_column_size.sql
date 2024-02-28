--liquibase formatted sql

--changeset mmihova:297.1
ALTER TABLE EXT_CORE.ENOTIF_MARK ALTER COLUMN BASIC_REGISTRATION_NUMBER varchar(5000);