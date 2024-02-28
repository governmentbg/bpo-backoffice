--liquibase formatted sql

--changeset mmurlev:314.1
insert into EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ABDOCS_DOC_TYPE_ID, TYPE, NAME, IPAS_OBJECT, DOC_REGISTRATION_TYPE) values (288,'A','АНП','MARK',3)