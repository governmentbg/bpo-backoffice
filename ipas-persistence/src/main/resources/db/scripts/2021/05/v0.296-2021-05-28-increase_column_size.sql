--liquibase formatted sql

--changeset mmihova:296.1
ALTER TABLE EXT_CORE.ENOTIF_MARK ALTER COLUMN BASIC_REGISTRATION_NUMBER varchar(800);