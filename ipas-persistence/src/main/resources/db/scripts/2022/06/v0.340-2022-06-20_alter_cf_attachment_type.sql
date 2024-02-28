--liquibase formatted sql

--changeset mmurlev:340.1
ALTER TABLE EXT_CORE.CF_ATTACHMENT_TYPE ADD ATTACHMENT_EXTENSION varchar(50);

--changeset mmurlev:340.2
update EXT_CORE.CF_ATTACHMENT_TYPE set ATTACHMENT_EXTENSION = 'pdf' where 1=1

--changeset mmurlev:340.3
insert into EXT_CORE.CF_ATTACHMENT_TYPE(id, name, ATTACHMENT_NAME_SUFFIX, ATTACHMENT_ORDER, ATTACHMENT_FILE_TYPES, ATTACHMENT_EXTENSION) values(19,'Листинг на последователностите',null,19,'U,T,P','xml')