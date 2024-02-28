create table EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE
(
    ID   numeric(5)
        constraint CF_ABDOCS_DOCUMENT_TYPE_pk
            primary key nonclustered,
    TYPE varchar(25) not null,
    NAME varchar(255)
)
;

create unique index CF_ABDOCS_DOCUMENT_TYPE_TYPE_uindex
    on EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (TYPE)
;

INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (2, 'ЗУSPC', 'SPC - Заявление за удължение на срока на SPC');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (3, 'ППДД', 'ОК - Представяне на превод на документи или доказателства');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (4, 'Въз', 'ОП - Възражение (чл.38а)');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (5, 'ОПО', 'ОП - Опозиция');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (12, 'ОпС', 'ОК - Отговор/възражение/становище/споразумение по опозиция/предварителен отказ/жалба/искане');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (25, 'ОФНОИ', 'ОК - Отговор във връзка с установен формални нередности по заявка / опозиция / искане');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (26, 'ВДДД', 'ОК - Внасяне на допълнителни документи или доказателства');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (27, 'СЗ', 'ИС - Служебно заличаване');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (28, 'ИЧПЗМ', 'ИС - Частично прехвърляне на заявка за марка');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (29, 'ИЧПРМ', 'ИС - Частично прехвърляне на регистрирана марка');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (36, 'ОК', 'ОК - Обща кореспонденция (писма, отговори и други некласифицирани)');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (60, 'ПС', 'СД - Приоритетно свидетелство');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (72, 'МПСТ', 'СД - Молба за проучване на състоянието на техниката');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (73, 'МПЕ', 'СД - Молба за проучване и експертиза');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (87, 'ДДИ', 'СД - Декларация за действителни изобретатели');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (88, 'ДЛГ', 'СД - Декларация за лицензионна готовност');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (89, 'ДММП', 'СД - Декларация за микро- и малко предприятие');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (90, 'Д75', 'СД - Декларация по чл.75в, ал.1, т.5 във връзка с чл.75б от ЗПРПМ');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (91, 'ИОЛГ', 'СД - Искане за оттегляне на лицензионна готовност');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (93, 'ИУП', 'СД - Искане за ускорена публикация');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (95, 'МОРПМ', 'СД - Молба за отлагане регистрация на полезен модел');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (98, 'ППЛН', 'ИС - Пълномощно');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (99, 'ПИЗ', 'ИС - Промяна на името и/или адреса на заявителя на заявка за обект на ИС');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (101, 'ЖО', 'Ж - Жалба срещу отказ');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (102, 'ЖПП', 'Ж - Жалба срещу прекратяване на производство');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (104, 'ЖРО', 'Ж - Жалба срещу решение по опозиция');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (105, 'ОАА', 'Ж - Обжалване на административни актове');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (122, 'ЕПТ5', 'ЕП - Т5 Коригиран превод към изменен ЕП');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (123, 'ЕПТ4', 'ЕП - Т4 Превод на претенциите към изменен ЕП');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (129, 'ВД', 'Вътрешни документи');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (130, 'ИЧПРТМ', 'ГО - Искане за частично подновяване на регистрацията на марка');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (135, 'ЕПИВ', 'ЕП - Искане за валидиране');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (136, 'ЕПИВЗ', 'ЕП - Искане за временна закрила');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (138, 'ЕПИВС72', 'ЕП - Искане за възстановяване на срока по чл.72в, ал.1');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (142, 'ЕППП', 'ЕП - Т1 Превод на претенциите');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (144, 'ЕППО', 'ЕП - Т3 Превод на описанието');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (148, 'ИК', 'Изходяща кореспонденция');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (149, 'ИКТГ', 'ИС - Корекция на техническа грешка');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (150, 'ИОСПЗ', 'ИС - Образуване на служебно производство за заличаване');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (151, 'ИОПН', 'ИС - Обявяване на пълна недействителност');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (153, 'ОГР', 'ИС - Ограничаване на списъка на стоките и/или услугите на марка');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (154, 'ИОРМ', 'ИС - Отменяне на регистрация');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (156, 'ИОЗ', 'ИС - Оттегляне на заявка за марка');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (157, 'ПРМ', 'ИС - Подновяване на регистрацията на марка/дизайн');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (158, 'ИСВП', 'ИС - Искане за спиране / възобновяване на производство');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (160, 'ИПДР', 'ИС - Прекратяване действието на регистрация');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (161, 'ПЛ', 'ИС - Прекратяване на лицензия');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (162, 'ПОМ', 'ИС - Прекратяване на обезпечителни мерки');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (163, 'ПОЗ', 'ИС - Прекратяване на особен залог');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (165, 'ППЗ', 'ИС - Прехвърляне на заявка за регистрация на обект на ИС');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (166, 'ПСД', 'ИС - Продължаване срока на действие на ПМ');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (168, 'ИПАК', 'ИС - Промяна на адрес за кореспонденция');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (172, 'ИКТГПВ', 'ИС - Искане за поправка на допусната от Патентно ведомство техническа грешка');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (174, 'ВМН', 'ИС - Вписване в масата на несъстоятелността');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (175, 'Лиц', 'ИС - Вписване на изключителна лицензия');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (176, 'НИЛ', 'ИС - Вписване на неизключителна лицензия');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (178, 'ВОМ', 'ИС - Вписване на обезпечителни мерки (ЗАПОР)');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (179, 'ОЗ', 'ИС - Вписване на особен залог');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (180, 'ИОП', 'ИС - Вписване на отказ от право');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (181, 'ППР', 'ИС - Вписване на прехвърляне на правото върху обект на ИС');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (182, 'ПИР', 'ИС - Вписване на промяна в името и / или адреса на притежателя на марка');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (184, 'ИЗВС', 'ИС - Възстановяване на срок');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (185, 'ДПр', 'ИС - Декларация за приоритет');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (186, 'ДД', 'ИС - Достъп до досие');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (187, 'ИЗРM', 'ИС - Заличаване на регистрация');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (188, 'ИИДД', 'ИС - Издаване на дубликат на документ');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (189, 'Удо', 'ИС - Издаване на удостоверение за обект на ИС');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (190, 'ИВПСО', 'ИС - Искане за второ продължаване на срок за споразумение по опозиция');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (192, 'ИКТГЗИ', 'ИС - Искане за поправка на допусната от заявителя техническа грешка в заявка или искане');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (193, 'ИКТГЗР', 'ИС - Искане за поправка на допусната от заявителя техническа грешка в регистър или защитен документ');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (116, 'N', 'Заявка за регистрация на марка');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (118, 'P', 'Заявка за патент за изобретение');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (111, 'S', 'Заявка за издаване на SPC');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (119, 'U', 'Заявка за регистрация на полезен модел');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (112, 'Г', 'Заявка за регистрация на географско означение');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (113, 'Д', 'Заявка за регистрация на промишлен дизайн');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (145, 'С', 'Заявка за регистрация за нов сорт/порода');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (114, 'T', 'Заявка за регистрация за Европейски патент');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (204, 'ЕПНИЛ', 'ЕП - Незключителна лицензия пред ЕПВ');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (205, 'ЕПЛИЦ', 'ЕП - Изключителна лицензия пред ЕПВ');
INSERT INTO EXT_RECEPTION.CF_ABDOCS_DOCUMENT_TYPE (ID, TYPE, NAME)
VALUES (206, 'ЕПОПО', 'ЕП - Опозиция пред ЕПВ');


