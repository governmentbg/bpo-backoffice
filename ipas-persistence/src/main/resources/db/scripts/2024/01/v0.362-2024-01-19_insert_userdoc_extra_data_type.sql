--liquibase formatted sql

--changeset mnakova:362
INSERT INTO EXT_CORE.CF_USERDOC_EXTRA_DATA_TYPE (CODE, TITLE, IS_DATE) VALUES ('IRREGULARITY_LETTER_REPLY_BY_DATE', N'Срок за отговор', 1);