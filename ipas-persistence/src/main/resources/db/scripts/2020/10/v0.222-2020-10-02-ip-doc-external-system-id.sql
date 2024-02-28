--liquibase formatted sql

--changeset ggeorgiev:221.1 splitStatements:false
alter table ip_doc alter column EXTERNAL_SYSTEM_ID varchar(50);