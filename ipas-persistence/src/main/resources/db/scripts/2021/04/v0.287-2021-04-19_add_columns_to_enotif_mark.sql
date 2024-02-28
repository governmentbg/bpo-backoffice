--liquibase formatted sql

--changeset mmihova:287.1
alter table ext_core.ENOTIF_MARK add ORIGINAL_LANG VARCHAR(15);
alter table ext_core.ENOTIF_MARK add ORIGINAL_COUNTRY VARCHAR(10);
alter table ext_core.ENOTIF_MARK add BASIC_REGISTRATION_NUMBER VARCHAR(100);
alter table ext_core.ENOTIF_MARK add DESIGNATION_TYPE VARCHAR(150);
