--liquibase formatted sql

--changeset ggeorgiev:332.1
alter table ext_core.CF_FILE_TYPE_GROUPS add userdoc_flag varchar(1);
update ext_core.CF_FILE_TYPE_GROUPS set userdoc_flag = 'N' where 1 = 1;
update ext_core.CF_FILE_TYPE_GROUPS set userdoc_flag = 'S' where GROUP_CODE in ('N', 'P', 'U', 'Г', 'Д', 'С', 'T', 'I', 'S');
alter table ext_core.CF_FILE_TYPE_GROUPS alter column userdoc_flag varchar(1) not null;