--liquibase formatted sql

--changeset mmurlev:323.1
create table EXT_CORE.CF_ACP_CHECK_REASON
(
	ID int not null,
	DESCRIPTION text,
constraint CF_ACP_CHECK_REASON_PK
		primary key nonclustered (id)
)

--changeset mmurlev:323.2
create table EXT_CORE.CF_ACP_CHECK_RESULT
(
	ID int not null,
	DESCRIPTION text,
constraint CF_ACP_CHECK_RESULT_PK
		primary key nonclustered (id)
)

--changeset mmurlev:323.3
create table EXT_CORE.ACP_CHECK_REASON
(
	REASON_ID int not null,
	FILE_SEQ varchar(2) not null,
	FILE_TYP varchar(1) not null,
	FILE_SER numeric(4) not null,
	FILE_NBR numeric(10) not null,
	constraint ACP_CHECK_REASON_PK
		primary key nonclustered (REASON_ID, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
)

--changeset mmurlev:323.4
ALTER TABLE  EXT_CORE.ACP_CHECK_REASON  WITH CHECK ADD  CONSTRAINT [REASON_ID_FK] FOREIGN KEY(REASON_ID)
REFERENCES EXT_CORE.CF_ACP_CHECK_REASON (id)

--changeset mmurlev:323.5
create table EXT_CORE.ACP_CHECK_DATA
(
	FILE_SEQ varchar(2) not null,
	FILE_TYP varchar(1) not null,
	FILE_SER numeric(4) not null,
	FILE_NBR numeric(10) not null,
	RESULT_ID int,
	CHECK_DATE datetime
	constraint ACP_CHECK_DATA_PK
		primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
)

--changeset mmurlev:323.6
ALTER TABLE EXT_CORE.ACP_CHECK_DATA  WITH CHECK ADD  CONSTRAINT [RESULT_ID_FK] FOREIGN KEY(RESULT_ID)
REFERENCES EXT_CORE.CF_ACP_CHECK_RESULT (id)

--changeset mmurlev:323.7
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('acp.acp-check-data','АНП: Преглед на панел "Проверка"');
