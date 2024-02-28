--liquibase formatted sql

--changeset mnakova:358
INSERT INTO ext_core.CF_EXT_FILE_TYPE(FILE_TYP, FILE_TYPE_NAME, FILE_TYP_ORDER) values ('EXT_EM', 'Марка на ЕС', 2);