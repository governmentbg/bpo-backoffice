--liquibase formatted sql

--changeset ggeorgiev:132.1
alter table EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE add DOC_REGISTRATION_TYPE integer;

--changeset ggeorgiev:132.2
update EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE set DOC_REGISTRATION_TYPE = 3 where IPAS_OBJECT <> 'USERDOC';--ByDocType
update EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE set DOC_REGISTRATION_TYPE = 7 where IPAS_OBJECT = 'USERDOC';--ByParentDocRegistrationNumber
update EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE set DOC_REGISTRATION_TYPE = 4 where TYPE = 'T';--ExternalRegistrationNumber
update EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE set DOC_REGISTRATION_TYPE = 3 where TYPE = 'ЕПИВ';--ByDocType

--changeset ggeorgiev:132.3
alter table EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE alter column DOC_REGISTRATION_TYPE integer not null;

