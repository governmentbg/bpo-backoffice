--liquibase formatted sql

--changeset mnakova:348.1
ALTER TABLE EXT_CORE.IP_MARK_INTL_REPLACEMENT ADD REPLACEMENT_FILING_NUMBER VARCHAR(25);