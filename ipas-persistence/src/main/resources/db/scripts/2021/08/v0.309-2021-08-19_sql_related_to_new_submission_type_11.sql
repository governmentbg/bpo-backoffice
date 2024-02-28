--liquibase formatted sql

--changeset mmurlev:309.1
insert into EXT_RECEPTION.CF_SUBMISSION_TYPE (ID, NAME, NAME_EN) values(11,'Импорт','Import')

--changeset mmurlev:309.2
UPDATE  EXT_RECEPTION.RECEPTION_USERDOC_REQUEST SET SUBMISSION_TYPE = 11
where EXTERNAL_SYSTEM_ID like '%/I/%' OR  EXTERNAL_SYSTEM_ID like '%/R/%'