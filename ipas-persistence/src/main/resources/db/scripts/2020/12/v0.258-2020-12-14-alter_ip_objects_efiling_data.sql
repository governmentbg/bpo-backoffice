--liquibase formatted sql

--changeset mmurlev:258.1
alter table ext_core.IP_OBJECT_EFILING_DATA
	add PRIORITY_REQUEST bit default 0