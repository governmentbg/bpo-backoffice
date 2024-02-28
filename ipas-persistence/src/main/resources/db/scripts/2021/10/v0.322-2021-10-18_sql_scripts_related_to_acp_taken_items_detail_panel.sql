--liquibase formatted sql

--changeset mmurlev:322.1
create table EXT_CORE.ACP_TAKEN_ITEM
(
	TAKEN_ITEM_ID int not null,
	FILE_SEQ varchar(2) not null,
	FILE_TYP varchar(1) not null,
	FILE_SER numeric(4) not null,
	FILE_NBR numeric(10) not null,
	TYPE text,
	COUNT int,
	STORAGE text,
	FOR_DESTRUCTION bit default 0,
	RETURNED bit default 0,
	constraint ACP_TAKEN_ITEM_PK
		primary key nonclustered (TAKEN_ITEM_ID, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
)

--changeset mmurlev:322.2
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('acp.acp-taken-items-data','АНП: Преглед на панел "Отнети вещи"');