--liquibase formatted sql

--changeset vnikolov:59.1
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD CHECKED BIT DEFAULT 0;
;
--changeset vnikolov:59.2
UPDATE [EXT_CORE].[INDEX_QUEUE] SET [CHECKED] = 1;
;


