--liquibase formatted sql

--changeset mmurlev:286.1
alter table ext_core.CF_USERDOC_TYPE_CONFIG add MARK_INHERIT_RESPONSIBLE_USER varchar(50)

--changeset mmurlev:286.2
update ext_core.CF_USERDOC_TYPE_CONFIG set MARK_INHERIT_RESPONSIBLE_USER = INHERIT_RESPONSIBLE_USER where 1=1
