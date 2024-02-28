--liquibase formatted sql

--changeset dveizov:146
alter table IPASPROD.EXT_DMS_DOC_MAP alter column ABDOCS_REGISTRATION_NUMBER varchar(255) null

