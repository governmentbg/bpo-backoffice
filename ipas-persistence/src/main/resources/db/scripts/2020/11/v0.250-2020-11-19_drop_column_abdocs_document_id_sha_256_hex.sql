--liquibase formatted sql

--changeset dveizov:250
ALTER TABLE EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT DROP COLUMN ABDOCS_DOCUMENT_ID_SHA256_SALTED_HEX;
