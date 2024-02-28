--liquibase formatted sql

--changeset dveizov:264
ALTER TABLE EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES ADD ABDOCS_USER_TARGETING_PROCESSED_DATE DATETIME;
UPDATE EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES SET ABDOCS_USER_TARGETING_PROCESSED_DATE = GETDATE() WHERE 1 = 1;
INSERT INTO IPASPROD.EXT_CONFIG_PARAM (CONFIG_CODE, VALUE) VALUES ('ABDOCS_NOTE_FOR_USER_TARGETING_ON_RESPONSIBLE_USER_CHANGE', 'Промяна на отговорен потребител в IPAS');
