--liquibase formatted sql

--changeset mmurlev:277.1

INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(119,'ABS','2019','ЗПРПМ,  чл. 74б, ал. 1, т. 1 във вр. с чл. 73, ал. 3',
'Регистрацията се заличава по искане на всяко лице, когато полезният модел не отговаря на изискванията за новост по чл. 73а, промишлено приложимост по чл. 73г и изобретателска стъпка по чл. 73в','')

INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(120,'ABS','2019','ЗПРПМ,  чл. 74б, ал. 1, т. 2 във вр. с чл. 73, ал. 4',
'Регистрацията се заличава по искане на всяко лице, когато с полезния модел е предоставена закрила за обект по чл. 6, ал. 2 и 4','')


INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(121,'ABS','2019','ЗПРПМ,  чл. 74б, ал. 1, т. 2 във вр. с чл. 73, ал. 5',
'Регистрацията се заличава по искане на всяко лице, когато с полезния модел е предоставена закрила за биотехнологични изобретение по смисъла на чл. 7а, метод, химично съединение или тяхно използване, както и обектите по чл. 7.','')


INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(122,'ABS','2019','ЗПРПМ,  чл. 74б, ал. 1, т. 3',
'Регистрацията се заличава по искане на всяко лице, когато не е разкрита достатъчно ясно и пълно същността на полезния модел, така че лице с обичайни знания и умения в областта да може да го осъществи','')


INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(123,'ABS','2019','ЗПРПМ,  чл. 74б, ал. 1, т. 4, б. а)',
'Регистрацията се заличава по искане на всяко лице, когато регистрираният полезен модел излиза извън съдържанието на заявката, така както е подадена','')

INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(124,'ABS','2019','ЗПРПМ,  чл. 74б, ал. 1, т. 4, б. б)',
'Регистрацията се заличава по искане на всяко лице, когато регистрираният полезен модел излиза извън съдържанието на първоначалната заявка, от която съгласно чл. 41 е отделена заявката за регистрация на полезния модел','')

INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(125,'ABS','2019','ЗПРПМ,  чл. 74б, ал. 1, т. 4, б. в)',
'Регистрацията се заличава по искане на всяко лице, когато регистрираният полезен модел излиза извън съдържанието на първоначалната заявка за патент, на която се е позовала заявката за полезен модел съгласно чл. 75б','')



INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(126,'ABS','2019','ЗПРПМ,  чл. 74б, ал. 1, т. 4, б. г)',
'Регистрацията се заличава по искане на всяко лице, когато регистрираният полезен модел излиза извън съдържанието на първоначалната заявка за патент, от която заявката за регистрация на полезен модел е трансформирана съгласно чл. 47а и 72е','')


INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(127,'ABS','2019','ЗПРПМ,  чл. 74б, ал. 4',
'Регистрацията на полезния модел се заличава и когато притежателят не е имал право на заявяване, което е установено с влязло в сила съдебно решение.','')


--changeset mmurlev:277.2
insert into ext_core.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN)
values ('UtilityModelAnnulmentRequest','N','Основания за заличаване ПМ','Utility model invalidity')


--changeset mmurlev:277.3
insert into ext_core.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL)
values ('ИЗРПМ','UtilityModelAnnulmentRequest')

--changeset mmurlev:277.4
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(119,'UtilityModelAnnulmentRequest')

insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(120,'UtilityModelAnnulmentRequest')

insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(121,'UtilityModelAnnulmentRequest')

insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(122,'UtilityModelAnnulmentRequest')

insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(123,'UtilityModelAnnulmentRequest')

insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(124,'UtilityModelAnnulmentRequest')

insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(125,'UtilityModelAnnulmentRequest')

insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(126,'UtilityModelAnnulmentRequest')

insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(127,'UtilityModelAnnulmentRequest')


--changeset mmurlev:277.5
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('userdoc.userdoc-utility-model-annulment-request-data','Вторични действия: Преглед на панел "Основания за заличаване ПМ"');




