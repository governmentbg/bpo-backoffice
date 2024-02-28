--liquibase formatted sql

--changeset mmurlev:326.1
create table EXT_CORE.ACP_INFRINGER
(
	FILE_SEQ varchar(2) not null,
	FILE_TYP varchar(1) not null,
	FILE_SER numeric(4) not null,
	FILE_NBR numeric(10) not null,
	INFRINGER_PERSON_NBR numeric(8),
	INFRINGER_ADDR_NBR numeric(4),
	constraint ACP_INFRINGER_PK
		primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
)

--changeset mmurlev:326.2
create table EXT_CORE.ACP_REPRS
(
	FILE_SEQ varchar(2) not null,
	FILE_TYP varchar(1) not null,
	FILE_SER numeric(4) not null,
	FILE_NBR numeric(10) not null,
	PERSON_NBR numeric(8) not null,
	ADDR_NBR numeric(4) not null,
	REPRESENTATIVE_TYP varchar(2) not null,
	constraint ACP_REPRS_PK
		primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, PERSON_NBR, ADDR_NBR, REPRESENTATIVE_TYP)
)

--changeset mmurlev:326.3
create table EXT_CORE.ACP_SERVICE_PERSON
(
	FILE_SEQ varchar(2) not null,
	FILE_TYP varchar(1) not null,
	FILE_SER numeric(4) not null,
	FILE_NBR numeric(10) not null,
	SERVICE_PERSON_NBR numeric(8),
	SERVICE_ADDR_NBR numeric(4),
	constraint ACP_SERVICE_PERSON_PK
		primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
)

--changeset mmurlev:326.4
create table EXT_CORE.CF_EXT_FILE_TYPE
(
	FILE_TYP varchar(20) not null
		constraint CF_EXT_FILE_TYPE_PK
			primary key,
	FILE_TYPE_NAME varchar(30) not null,
	FILE_TYP_ORDER int
)

--changeset mmurlev:326.5
insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('N,D','Марка',1)

insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('I,R','Международна регистрация',2)

insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('Г','Географски означения',3)

insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('Н','Международни НП',4)

insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('Д','Промишлен дизайн',5)

insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('Х','Международен дизайн',6)

insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('P','Патент за изобретение',7)

insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('T','Европейски патент',8)

insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('U','Полезен модел',9)

insert into ext_Core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER)
values('С','Сортове и породи',10)


--changeset mmurlev:326.6
--validCheckSum: 8:46b9b5cd8f31a35fac2aabc705124466
INSERT INTO EXT_CORE.CF_ACP_CHECK_REASON (ID, DESCRIPTION) VALUES (1, N'По инициатива на ПВ');
INSERT INTO EXT_CORE.CF_ACP_CHECK_REASON (ID, DESCRIPTION) VALUES (2, N'Сезиране от друга институция');
INSERT INTO EXT_CORE.CF_ACP_CHECK_REASON (ID, DESCRIPTION) VALUES (4, N'Сигнал на съпритежател на право');
INSERT INTO EXT_CORE.CF_ACP_CHECK_REASON (ID, DESCRIPTION) VALUES (5, N'Сигнал на лицензополучател на изключителна лицензия');
INSERT INTO EXT_CORE.CF_ACP_CHECK_REASON (ID, DESCRIPTION) VALUES (6, N'Сигнал на лицензополучател на неизключителна лицензия');
INSERT INTO EXT_CORE.CF_ACP_CHECK_REASON (ID, DESCRIPTION) VALUES (7, N'Сигнал на лице с право да използва колективна марка');
INSERT INTO EXT_CORE.CF_ACP_CHECK_REASON (ID, DESCRIPTION) VALUES (8, N'Сигнал на притежател на право или на вписан ползвател');
INSERT INTO EXT_CORE.CF_ACP_CHECK_REASON (ID, DESCRIPTION) VALUES (10, N'Искане на органите на МВР');
INSERT INTO EXT_CORE.CF_ACP_CHECK_REASON (ID, DESCRIPTION) VALUES (11, N'Искане на прокурор');

--changeset mmurlev:326.7
insert into EXT_CORE.CF_FILE_TYP_EXTENDED (file_typ,code,file_type_name_en)
values('A','ACP','Administrative criminal proceedings')
