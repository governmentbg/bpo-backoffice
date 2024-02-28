--liquibase formatted sql

--changeset dveizov:185
INSERT INTO EXT_RECEPTION.USERDOC_RECEPTION_RELATION (LINKED_USERDOC_TYPE, MAIN_TYPE, IS_VISIBLE) VALUES ('ЖРОСП', 'С', 'S');
INSERT INTO EXT_RECEPTION.USERDOC_RECEPTION_RELATION (LINKED_USERDOC_TYPE, MAIN_TYPE, IS_VISIBLE) VALUES ('ЖРПЛСП', 'С', 'S');
delete from EXT_RECEPTION.USERDOC_RECEPTION_RELATION where LINKED_USERDOC_TYPE in ('ЕПЛИЦ','ЕПНИЛ','ЕПИОН','ИЧПЗМ','ИЧПРМ','ИОСПЗ','ИОЧН');