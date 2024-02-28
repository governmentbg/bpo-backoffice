--liquibase formatted sql

--changeset mmurlev:336.1
update EXT_RECEPTION.CF_RECEPTION_TYPE set RECEPTION_FROM_EXISTING_DOCUMENT = 1 where RECEPTION_ON_COUNTER = 1