--liquibase formatted sql

--changeset mmurlev:347.1
insert into ext_core.CF_ATTACHMENT_TYPE (id, name,ATTACHMENT_ORDER,ATTACHMENT_FILE_TYPES,ATTACHMENT_EXTENSION) values (20,'Официално описание',20,'С','pdf')
update ext_Core.CF_ATTACHMENT_TYPE set ATTACHMENT_FILE_TYPES = 'U,T,P,С' where ID = 1

