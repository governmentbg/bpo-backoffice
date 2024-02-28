--liquibase formatted sql

--changeset murlev:359.1
alter table IPASPROD.CF_CLASS_IPC add SYMBOL varchar(20);
