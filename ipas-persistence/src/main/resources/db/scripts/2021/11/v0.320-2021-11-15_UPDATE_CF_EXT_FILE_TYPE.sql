--liquibase formatted sql

--changeset mmurlev:320.1
update ext_core.CF_EXT_FILE_TYPE set FILE_TYP = 'I,R,B' WHERE FILE_TYP = 'I,R'
