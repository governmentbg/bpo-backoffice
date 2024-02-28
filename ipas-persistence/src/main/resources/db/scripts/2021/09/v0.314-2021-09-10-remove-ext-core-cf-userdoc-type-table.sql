--liquibase formatted sql

--changeset ggeorgiev:314.1
alter table ext_core.CF_USERDOC_TYPE_CONFIG add PUBLIC_LIABILITIES varchar(1);

--inserting missing records inside EXT_CORE.CF_USERDOC_TYPE_CONFIG
insert into EXT_CORE.CF_USERDOC_TYPE_CONFIG (USERDOC_TYP)
select udt.USERDOC_TYP from CF_USERDOC_TYPE udt
left join EXT_CORE.CF_USERDOC_TYPE_CONFIG utc on udt.USERDOC_TYP = utc.USERDOC_TYP
where utc.USERDOC_TYP is null

--transferring the ext_core.cf_userdoc_type.public_liabilities to ext_core.cf_userdoc_type_config
update utc
SET utc.PUBLIC_LIABILITIES = (CASE WHEN udt.PUBLIC_LIABILITIES = 1 THEN 'S' ELSE 'N' END)
    FROM ext_core.CF_USERDOC_TYPE_CONFIG utc
join ext_core.cf_userdoc_type udt on udt.USERDOC_TYP = utc.USERDOC_TYP

--updating the empty records
update ext_core.CF_USERDOC_TYPE_CONFIG set PUBLIC_LIABILITIES = 'S' where PUBLIC_LIABILITIES is null;

alter table ext_core.CF_USERDOC_TYPE_CONFIG alter column PUBLIC_LIABILITIES varchar(1) not null;

--changeset ggeorgiev:314.2
drop table ext_core.CF_USERDOC_TYPE;