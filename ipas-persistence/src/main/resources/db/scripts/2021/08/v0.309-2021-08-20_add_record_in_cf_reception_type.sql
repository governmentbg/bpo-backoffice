--liquibase formatted sql

--changeset mmurlev:309.1
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE ( TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE, APPL_TYP) VALUES ('Заявка за регистрация на международна марка', 'Mеждународна марка', null, null, 'I', 'МР');