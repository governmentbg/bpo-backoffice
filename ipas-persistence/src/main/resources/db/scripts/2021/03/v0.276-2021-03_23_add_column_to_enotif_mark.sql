--liquibase formatted sql

--changeset mmihova:276.1
alter table ext_core.ENOTIF_MARK add transaction_code VARCHAR(10);
