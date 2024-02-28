--liquibase formatted sql

--changeset mmurlev:321.1
create table EXT_CORE.ACP_DETAILS
(
	FILE_SEQ varchar(2) not null,
	FILE_TYP varchar(1) not null,
	FILE_SER numeric(4) not null,
	FILE_NBR numeric(10) not null,
    AFFECTED_OBJECT_OTHERS text ,
	constraint ACP_DETAILS_PK
		primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
)