--liquibase formatted sql

--changeset mmurlev:313.1
insert into EXT_RECEPTION.CF_RECEPTION_TYPE (TITLE, SHORT_TITLE, FILE_TYPE, APPL_TYP) values ('Административно-наказателни производства (АНП)','АНП','A','СИГ')

--changeset mmurlev:313.2
alter table EXT_RECEPTION.CF_RECEPTION_TYPE add RECEPTION_ON_COUNTER bit default 0

--changeset mmurlev:313.3
alter table EXT_RECEPTION.CF_RECEPTION_TYPE add RECEPTION_FROM_EXISTING_DOCUMENT bit default 0

--changeset mmurlev:313.4
update EXT_RECEPTION.CF_RECEPTION_TYPE set RECEPTION_ON_COUNTER = 1 , RECEPTION_FROM_EXISTING_DOCUMENT = 0 where FILE_TYPE != 'A'

--changeset mmurlev:313.5
update EXT_RECEPTION.CF_RECEPTION_TYPE set RECEPTION_FROM_EXISTING_DOCUMENT = 1,RECEPTION_ON_COUNTER = 0 where FILE_TYPE = 'A'

--changeset mmurlev:313.6
update EXT_RECEPTION.CF_RECEPTION_TYPE set RECEPTION_FROM_EXISTING_DOCUMENT = 0 , RECEPTION_ON_COUNTER = 0 where FILE_TYPE = 'I'