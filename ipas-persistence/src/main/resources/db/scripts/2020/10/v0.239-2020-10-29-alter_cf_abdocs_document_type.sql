--liquibase formatted sql

--changeset mmurlev:239.1
delete from EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE where TYPE in ('Въз51-IMARK','ИЗРM-IMARK','ИОРМ-IMARK','ОПО-IMARK')

--changeset mmurlev:239.2
insert into EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ABDOCS_DOC_TYPE_ID, TYPE, NAME, IPAS_OBJECT, DOC_REGISTRATION_TYPE)
values (237,'Въз51-IMARK','Възражение срещу международна марка','USERDOC_IMARK',3)

--changeset mmurlev:239.3
insert into EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ABDOCS_DOC_TYPE_ID, TYPE, NAME, IPAS_OBJECT, DOC_REGISTRATION_TYPE)
values (238,'ИЗРM-IMARK','Искане за заличаване на международна марка','USERDOC_IMARK',3)

--changeset mmurlev:239.4
insert into EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ABDOCS_DOC_TYPE_ID, TYPE, NAME, IPAS_OBJECT, DOC_REGISTRATION_TYPE)
values (239,'ИОРМ-IMARK','Искане за отмяна на международна марка','USERDOC_IMARK',3)

--changeset mmurlev:239.5
insert into EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ABDOCS_DOC_TYPE_ID, TYPE, NAME, IPAS_OBJECT, DOC_REGISTRATION_TYPE)
values (236,'ОПО-IMARK','Опозиция по международна марка','USERDOC_IMARK',3)
