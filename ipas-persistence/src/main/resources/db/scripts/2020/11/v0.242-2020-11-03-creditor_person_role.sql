--liquibase formatted sql

--changeset dveizov:242
INSERT INTO EXT_CORE.CF_USERDOC_PERSON_ROLE (ROLE, IND_TAKE_FROM_OWNER, NAME, NAME_EN) VALUES ('CREDITOR', null, 'Взискател', 'Creditor');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE (USERDOC_TYP, ROLE) VALUES ('ВОМ', 'CREDITOR');