--liquibase formatted sql

--changeset mmihova:331.1
ALTER TABLE EXT_CORE.IP_MARK_OLD_INTL_REGISTRATION ADD STATUS varchar(50);