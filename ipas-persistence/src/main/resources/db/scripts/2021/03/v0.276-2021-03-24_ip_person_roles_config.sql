--liquibase formatted sql

--changeset dveizov:276
UPDATE EXT_CORE.CF_USERDOC_PERSON_ROLE SET IND_TAKE_FROM_OWNER = null where ROLE = 'PAYER';
UPDATE EXT_CORE.CF_USERDOC_PERSON_ROLE SET IND_TAKE_FROM_OWNER = 'S' where ROLE = 'PAYEE';
