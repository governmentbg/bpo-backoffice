--liquibase formatted sql

--changeset mmurlev:335.1
create table EXT_CORE.CF_USAGE_RULE
(
	id int not null
		primary key,
	name varchar(100)
)

--changeset mmurlev:335.2
insert into EXT_CORE.CF_USAGE_RULE(id, name) values (1,'Правила за ползване')
insert into EXT_CORE.CF_USAGE_RULE(id, name) values (2,'Правила за ползване (променени)')

--changeset mmurlev:335.3
create table EXT_CORE.IP_MARK_USAGE_RULE
(
	ID int not null,
	FILE_SEQ varchar(2) not null,
	FILE_TYP varchar(1) not null,
	FILE_SER numeric(4) not null,
	FILE_NBR numeric(10) not null,
	TYPE int not null
		references EXT_CORE.CF_USAGE_RULE,
	CONTENT varbinary(max),
	NAME varchar(255),
	DATE_CREATED datetime default getdate() not null,
	constraint IP_MARK_USAGE_RULE_PK
		primary key (ID, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, TYPE)
)


