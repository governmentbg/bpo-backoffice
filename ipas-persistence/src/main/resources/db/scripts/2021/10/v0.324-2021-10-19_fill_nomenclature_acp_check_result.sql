--liquibase formatted sql

--changeset mmurlev:324.1
insert into EXT_CORE.CF_ACP_CHECK_RESULT(ID, DESCRIPTION) values (1,'АУАН')
--changeset mmurlev:324.2
insert into EXT_CORE.CF_ACP_CHECK_RESULT(ID, DESCRIPTION) values (2,'Констативен протокол')
--changeset mmurlev:324.3
insert into EXT_CORE.CF_ACP_CHECK_RESULT(ID, DESCRIPTION) values (3,'Нередовен сигнал')
--changeset mmurlev:324.4
insert into EXT_CORE.CF_ACP_CHECK_RESULT(ID, DESCRIPTION) values (4,'Наказателно постановление')
--changeset mmurlev:324.5
insert into EXT_CORE.CF_ACP_CHECK_RESULT(ID, DESCRIPTION) values (5,'Постановление за прекратяване')
