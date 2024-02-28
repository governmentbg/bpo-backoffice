create table EXT_RECEPTION.CF_RECEPTION_TYPE
(
    ID             numeric(18) identity
        constraint CF_RECEPTION_TYPE_pk
            primary key nonclustered,
    TITLE          varchar(255),
    SHORT_TITLE    varchar(255),
    TITLE_EN       varchar(255),
    SHORT_TITLE_EN varchar(255),
    FILE_TYPE      varchar(1) not null
);

create unique index CF_RECEPTION_TYPE_FILE_TYPE_uindex
    on EXT_RECEPTION.CF_RECEPTION_TYPE (FILE_TYPE);

SET IDENTITY_INSERT EXT_RECEPTION.CF_RECEPTION_TYPE ON;
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE(ID, TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE)
VALUES (1, 'Заявка за регистрация на марка', 'Марка', '', '', 'N')
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE(ID, TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE)
VALUES (2, 'Заявка за патент за изобретение', 'Патент', '', '', 'P')
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE(ID, TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE)
VALUES (3, 'Заявка за издаване на SPC', 'SPC', '', '', 'S')
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE(ID, TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE)
VALUES (4, 'Заявка за регистрация на полезен модел', 'Полезен модел', '', '', 'U')
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE(ID, TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE)
VALUES (5, 'Заявка за регистрация на географско означение', 'Географско означение', '', '', 'Г')
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE(ID, TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE)
VALUES (6, 'Заявка за регистрация на промишлен дизайн', 'Дизайн', '', '', 'Д')
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE(ID, TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE)
VALUES (7, 'Заявка за регистрация за нов сорт/порода', 'Сорт/Порода', '', '', 'С')
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE(ID, TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE)
VALUES (8, 'Заявка за регистрация за Европейски патент', 'Европейски патент', '', '', 'T')
INSERT INTO EXT_RECEPTION.CF_RECEPTION_TYPE(ID, TITLE, SHORT_TITLE, TITLE_EN, SHORT_TITLE_EN, FILE_TYPE)
VALUES (9, 'Заявка за регистрация на вторично действие', 'Вторично действие', '', '', 'E')
SET IDENTITY_INSERT EXT_RECEPTION.CF_RECEPTION_TYPE OFF;



