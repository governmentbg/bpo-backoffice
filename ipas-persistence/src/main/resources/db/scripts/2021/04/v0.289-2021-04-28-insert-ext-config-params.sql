--liquibase formatted sql

--changeset mmihova:289.1
INSERT INTO IPASPROD.EXT_CONFIG_PARAM (CONFIG_CODE, VALUE) VALUES ('INTL_MARK_SURRENDER_PROTECTION_ACTION_TYPE', '3198');
INSERT INTO IPASPROD.EXT_CONFIG_PARAM (CONFIG_CODE, VALUE) VALUES ('INTL_MARK_SURRENDER_PROTECTION_STATUS_CODE', '1221');