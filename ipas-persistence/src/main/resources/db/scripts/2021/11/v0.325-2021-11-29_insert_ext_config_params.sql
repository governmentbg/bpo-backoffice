--liquibase formatted sql

--changeset mmihova:325.1
INSERT INTO IPASPROD.EXT_CONFIG_PARAM (CONFIG_CODE, VALUE) VALUES ('ZMR_MADRID_EFILING_ACTION_TYPE', '3528');