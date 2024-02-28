--liquibase formatted sql

--changeset mmurlev:348.1
alter table EXT_CORE.IP_USERDOC_COURT_APPEAL add COURT_LINK text;

