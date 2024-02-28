--liquibase formatted sql

--changeset ggeorgiev:229.1 splitStatements:false
alter table ext_reception.RECEPTION_USERDOC_REQUEST alter column notes text;