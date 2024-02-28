--liquibase formatted sql

--changeset ggeorgiev:208
update EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE set DOC_REGISTRATION_TYPE = 1 where DOC_REGISTRATION_TYPE = 7;

