ALTER TABLE EXT_CORE.IP_FILE_RECORDAL
    ADD INVALIDATION_PROC_TYP varchar(4);

ALTER TABLE EXT_CORE.IP_FILE_RECORDAL
    ADD INVALIDATION_PROC_NBR numeric(8);

ALTER TABLE EXT_CORE.IP_FILE_RECORDAL
    ADD INVALIDATION_ACTION_NBR numeric(10);
