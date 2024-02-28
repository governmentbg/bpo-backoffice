--liquibase formatted sql

--changeset ggeorgiev:351
ALTER TABLE EXT_CORE.IP_PATENT_DETAILS ADD NOT_IN_FORCE_DATE DATETIME;