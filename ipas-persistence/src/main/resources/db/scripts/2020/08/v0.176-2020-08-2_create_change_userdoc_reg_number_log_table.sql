--liquibase formatted sql

--changeset dveizov:176
create table EXT_CORE.IP_USERDOC_REGISTRATION_NUMBER_CHANGE_LOG
(
	ID numeric(8) identity
		constraint PK_IP_USERDOC_REGISTRATION_NUMBER_CHANGE_LOG
			primary key,
    DOC_ORI varchar(4) not null,
    DOC_LOG varchar(1) not null,
    DOC_SER numeric(4) not null,
    DOC_NBR numeric(15) not null,
	OLD_REGISTRATION_NUMBER varchar (50) not null,
	NEW_REGISTRATION_NUMBER varchar (50) not null,
	DATE datetime not null,
	USERNAME varchar(500) not null
);