--liquibase formatted sql

--changeset murlev:361
alter table  ipasprod.IP_PATENT_REPRS  add  PRIOR_REPRS_REVOCATION  bit;
alter table  ipasprod.IP_MARK_REPRS  add  PRIOR_REPRS_REVOCATION  bit;
alter table  EXT_CORE.IP_USERDOC_PERSON  add  PRIOR_REPRS_REVOCATION  bit;