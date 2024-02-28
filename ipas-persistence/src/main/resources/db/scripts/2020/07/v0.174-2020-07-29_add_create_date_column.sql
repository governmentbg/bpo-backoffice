--liquibase formatted sql

--changeset dveizov:174.1
alter table EXT_RECEPTION.RECEPTION_REQUEST
	add CREATE_DATE datetime default GETDATE() not null

--changeset dveizov:174.2
alter table EXT_RECEPTION.RECEPTION_USERDOC_REQUEST
	add CREATE_DATE datetime default GETDATE() not null