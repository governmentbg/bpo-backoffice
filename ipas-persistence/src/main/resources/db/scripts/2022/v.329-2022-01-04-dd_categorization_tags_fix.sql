--liquibase formatted sql

--changeset raneva:329.1
update EXT_CORE.CF_DD_CATEGORIZATION_TAGS set DOSSIER_TYPE = 'TM' where DOSSIER_TYPE='ТМ';