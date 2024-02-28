--liquibase formatted sql

--changeset mmurlev:237.1
insert into EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ABDOCS_DOC_TYPE_ID, TYPE, NAME, IPAS_OBJECT, DOC_REGISTRATION_TYPE)
values (237,'Въз51-IMARK','Възражение срещу международна марка','USERDOC',3)

--changeset mmurlev:237.2
insert into EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ABDOCS_DOC_TYPE_ID, TYPE, NAME, IPAS_OBJECT, DOC_REGISTRATION_TYPE)
values (238,'ИЗРM-IMARK','Искане за заличаване на международна марка','USERDOC',3)

--changeset mmurlev:237.3
insert into EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ABDOCS_DOC_TYPE_ID, TYPE, NAME, IPAS_OBJECT, DOC_REGISTRATION_TYPE)
values (239,'ИОРМ-IMARK','Искане за отмяна на международна марка','USERDOC',3)

--changeset mmurlev:237.4
insert into EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ABDOCS_DOC_TYPE_ID, TYPE, NAME, IPAS_OBJECT, DOC_REGISTRATION_TYPE)
values (236,'ОПО-IMARK','Опозиция по международна марка','USERDOC',3)

