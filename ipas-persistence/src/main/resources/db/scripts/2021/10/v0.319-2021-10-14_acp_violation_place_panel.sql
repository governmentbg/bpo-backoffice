--liquibase formatted sql

--changeset mmurlev:319.1
create table EXT_CORE.ACP_VIOLATION_PLACE
(
    VIOLATION_PLACE_ID int not null,
	FILE_SEQ varchar(2) not null,
	FILE_TYP varchar(1) not null,
	FILE_SER numeric(4) not null,
	FILE_NBR numeric(10) not null,
    DESCRIPTION text not null,
	constraint ACP_VIOLATION_PLACE_PK
		primary key nonclustered (VIOLATION_PLACE_ID,FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
)

--changeset mmurlev:319.2
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('acp.acp-violation-places-data','АНП: Преглед на панел "Място на нарушение"');

