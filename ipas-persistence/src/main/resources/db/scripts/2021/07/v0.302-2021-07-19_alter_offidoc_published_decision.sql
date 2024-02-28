--liquibase formatted sql

--changeset mmurlev:302.1
ALTER TABLE EXT_CORE.IP_OFFIDOC_PUBLISHED_DECISION drop column DOC_NBR

--changeset mmurlev:302.2
ALTER TABLE EXT_CORE.IP_OFFIDOC_PUBLISHED_DECISION drop column OFFIDOC_TYP
