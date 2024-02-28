--liquibase formatted sql

--changeset mmurlev:326.1
insert into EXT_RECEPTION.CF_SUBMISSION_TYPE (ID, NAME, NAME_EN) values(12,'Разделена','Divided');