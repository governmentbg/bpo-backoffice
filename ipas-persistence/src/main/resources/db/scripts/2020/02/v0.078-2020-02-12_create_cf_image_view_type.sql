create table EXT_CORE.CF_IMAGE_VIEW_TYPE
(
    VIEW_TYPE_ID        numeric(3)  not null,
    VIEW_TYPE_NAME      varchar(50)
    constraint PK_CF_IMAGE_VIEW_TYPE
        primary key (VIEW_TYPE_ID)
)
;

INSERT INTO EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID, VIEW_TYPE_NAME) VALUES (1, 'Основен изглед');
INSERT INTO EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID, VIEW_TYPE_NAME) VALUES (2, 'Изглед в перспектива');
INSERT INTO EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID, VIEW_TYPE_NAME) VALUES (3, 'Изглед отпред');
INSERT INTO EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID, VIEW_TYPE_NAME) VALUES (4, 'Изглед отзад');
INSERT INTO EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID, VIEW_TYPE_NAME) VALUES (5, 'Изглед отляво');
INSERT INTO EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID, VIEW_TYPE_NAME) VALUES (6, 'Изглед отдясно');
INSERT INTO EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID, VIEW_TYPE_NAME) VALUES (7, 'Изглед отгоре');
INSERT INTO EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID, VIEW_TYPE_NAME) VALUES (8, 'Изглед отдолу');
INSERT INTO EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID, VIEW_TYPE_NAME) VALUES (9, 'Частичен изглед');


