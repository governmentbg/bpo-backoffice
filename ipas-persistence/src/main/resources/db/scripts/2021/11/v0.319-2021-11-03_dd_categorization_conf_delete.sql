--liquibase formatted sql

--changeset raneva:319.1
delete from EXT_CORE.CF_DD_CATEGORIZATION_TAGS where USERDOC_TYP in ('ИСВП', 'ИПРВП');