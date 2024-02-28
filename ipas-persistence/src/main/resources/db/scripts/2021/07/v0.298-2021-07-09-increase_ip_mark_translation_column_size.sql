--liquibase formatted sql

--changeset mmihova:298.1
ALTER TABLE IPASPROD.IP_MARK ALTER COLUMN TRANSLATION varchar(3000);