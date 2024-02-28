--liquibase formatted sql

--changeset mmurlev:292.1
ALTER TABLE ext_core.IP_FILE_RELATIONSHIP_EXTENDED ADD SERVE_MESSAGE_DATE datetime;



