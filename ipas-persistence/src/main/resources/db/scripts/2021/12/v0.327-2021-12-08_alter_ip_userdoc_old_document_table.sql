--liquibase formatted sql

--changeset mmihova:327.1
ALTER TABLE EXT_CORE.IP_USERDOC_OLD_DOCUMENT ADD RESPONSIBLE_USER_ID numeric(5);