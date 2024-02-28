--liquibase formatted sql

--changeset ggeorgiev:230
alter table ext_reception.RECEPTION_REQUEST add NOTES text;
alter table ext_reception.RECEPTION_USERDOC_REQUEST add NOTES2 text;