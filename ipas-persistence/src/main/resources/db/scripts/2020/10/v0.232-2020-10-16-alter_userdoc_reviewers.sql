--liquibase formatted sql

--changeset mmurlev:232.1
alter table ext_core.IP_USERDOC_REVIEWERS
	add MAIN bit default 0