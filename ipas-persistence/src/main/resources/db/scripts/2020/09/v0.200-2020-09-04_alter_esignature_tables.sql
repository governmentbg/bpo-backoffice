--liquibase formatted sql

--changeset mmurlev:200.1
alter table ext_core.IP_OBJECT_ESIGNATURE add LOG_USER_NAME VARCHAR(75)

--changeset mmurlev:200.2
alter table ext_core.IP_USERDOC_ESIGNATURE add LOG_USER_NAME VARCHAR(75)

--changeset mmurlev:200.3
alter table ext_core.IP_OBJECT_ESIGNATURE drop constraint IP_OBJECT_ESIGNATURE_USER_FK

--changeset mmurlev:200.4
alter table ext_core.IP_USERDOC_ESIGNATURE drop constraint IP_OBJECT_ESIGNATURE_USER_ID_FK

--changeset mmurlev:200.5
alter table ext_core.IP_OBJECT_ESIGNATURE drop column ES_USER_ID

--changeset mmurlev:200.6
alter table ext_core.IP_USERDOC_ESIGNATURE drop column ES_USER_ID