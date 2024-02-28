--liquibase formatted sql

--changeset mmurlev:264.1
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
values (20,'Заличаване по искане на всяко лице','everyPerson',2019)

update ext_Core.CF_APPLICANT_AUTHORITY set name ='Лицензополучател на изключителна лицензия' where id = 8

INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
values (21,'Заличаване по искане на всяко лице','everyPerson',2011)

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(85,'REL',2019,
'чл. 29. (1)',
'Регистрацията на дизайна се заличава по искане на всяко лице, когато: чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 3 не отговаря на определението за дизайн;',
'ds.invalidityGround.29.1.1.3a')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(86,'REL',2011,
'чл. 29. (1)',
'Регистрацията на дизайна се заличава по искане на всяко лице, когато: чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 3 не отговаря на определението за дизайн;',
'ds.invalidityGround.29.1.1.3a')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(87,'REL',2019,
'чл. 29. (1)',
'Регистрацията на дизайна се заличава по искане на всяко лице, когато: чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 3 не е изделие, получено по промишлен или занаятчийски начин.',
'ds.invalidityGround.29.1.1.3b')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(88,'REL',2011,
'чл. 29. (1)',
'Регистрацията на дизайна се заличава по искане на всяко лице, когато: чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 3 не е изделие, получено по промишлен или занаятчийски начин.',
'ds.invalidityGround.29.1.1.3b')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(89,'REL',2019,
'чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 11, ал. 1',
'дизайнът не е нов по смисъла на чл. 12;',
'ds.invalidityGround.29.1.1.12')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(90,'REL',2011,
'чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 11, ал. 1',
'дизайнът не е нов по смисъла на чл. 12;',
'ds.invalidityGround.29.1.1.12')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(91,'REL',2019,
'чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 11, ал. 1',
'дизайнът не е оригинален по смисъла на чл. 13;',
'ds.invalidityGround.29.1.1.13')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(92,'REL',2011,
'чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 11, ал. 1',
'дизайнът не е оригинален по смисъла на чл. 13;',
'ds.invalidityGround.29.1.1.13')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(93,'REL',2019,
'чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 11, ал. 1',
'дизайнът не е нов и оригинален по смисъла на чл. 13а.',
'ds.invalidityGround.29.1.1.13a')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(94,'REL',2011,
'чл. 29. (1) т. 1 Дизайнът е регистриран в противоречие с изискванията на чл. 11, ал. 1',
'дизайнът не е нов и оригинален по смисъла на чл. 13а.',
'ds.invalidityGround.29.1.1.13a')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(95,'REL',2019,
'чл. 29, (1) т. 2 Дизайнът е изключен от закрила съгласно чл. 11, ал. 2 на ЗПД',
'т. 1 дизайнът противоречи на обществения ред или на добрите нрави;',
'ds.invalidityGround.29.1.2.1')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(96,'REL',2011,
'чл. 29, (1) т. 2 Дизайнът е изключен от закрила съгласно чл. 11, ал. 2 на ЗПД',
'т. 1 дизайнът противоречи на обществения ред или на добрите нрави;',
'ds.invalidityGround.29.1.2.1')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(97,'REL',2019,
'чл. 29, (1) т. 2 Дизайнът е изключен от закрила съгласно чл. 11, ал. 2 на ЗПД',
'т. 2 особеностите на дизайна са обусловени единствено от техническата функция на продукта;',
'ds.invalidityGround.29.1.2.2')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(98,'REL',2011,
'чл. 29, (1) т. 2 Дизайнът е изключен от закрила съгласно чл. 11, ал. 2 на ЗПД',
'т. 2 особеностите на дизайна са обусловени единствено от техническата функция на продукта;',
'ds.invalidityGround.29.1.2.2')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(99,'REL',2019,
'чл. 29, (1) т. 2 Дизайнът е изключен от закрила съгласно чл. 11, ал. 2 на ЗПД',
'т. 3 особеностите на дизайна са обусловени от необходимостта продуктът, към който е приложен дизайнът, да бъде механически свързан или поставен във, около или срещу друг продукт така, че и двата продукта да изпълняват функциите си.',
'ds.invalidityGround.29.1.2.3')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(100,'REL',2011,
'чл. 29, (1) т. 2 Дизайнът е изключен от закрила съгласно чл. 11, ал. 2 на ЗПД',
'т. 3 особеностите на дизайна са обусловени от необходимостта продуктът, към който е приложен дизайнът, да бъде механически свързан или поставен във, около или срещу друг продукт така, че и двата продукта да изпълняват функциите си.',
'ds.invalidityGround.29.1.2.3')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(101,'REL',2019,
'чл. 29, (1) т. 3 ',
'дизайнът е идентичен по смисъла на чл. 12, ал. 2 на дизайн, станал общодостъпен по какъвто и да е начин преди датата на подаване на заявката, съответно приоритетната дата, където и да е по света;',
'ds.invalidityGround.29.1.3a')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(102,'REL',2011,
'чл. 29, (1) т. 3 ',
'дизайнът е идентичен по смисъла на чл. 12, ал. 2 на дизайн, станал общодостъпен по какъвто и да е начин преди датата на подаване на заявката, съответно приоритетната дата, където и да е по света;',
'ds.invalidityGround.29.1.3a')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(103,'REL',2019,
'чл. 29, (1) т. 3 ',
'дизайнът е идентичен по смисъла на чл. 12, ал. 2 на дизайн, който е заявен преди датата на подаване на заявката за регистрация, съответно приоритетната дата, и регистриран по национален или международен ред или като дизайн на Общността.',
'ds.invalidityGround.29.1.3b')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(104,'REL',2011,
'чл. 29, (1) т. 3 ',
'дизайнът е идентичен по смисъла на чл. 12, ал. 2 на дизайн, който е заявен преди датата на подаване на заявката за регистрация, съответно приоритетната дата, и регистриран по национален или международен ред или като дизайн на Общността.',
'ds.invalidityGround.29.1.3b')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(105,'REL',2019,
'чл. 29. (1) т. 5',
' Дизайнът е регистриран при неспазване на изискванията на чл. 2.',
'ds.invalidityGround.29.1.5')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(106,'REL',2011,
'чл. 29. (1) т. 5',
' Дизайнът е регистриран при неспазване на изискванията на чл. 2.',
'ds.invalidityGround.29.1.5')

INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
values (22,'Заличаване по искане на лице с правен интерес','personLegalInterest',2019)

INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
values (23,'Заличаване по искане на лице с правен интерес','personLegalInterest',2011)

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(107,'REL',2019,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 1 по-ранно авторско право по Закона за авторското право и сродните му права;',
'ds.invalidityGround.29.2.1')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(108,'REL',2011,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 1 по-ранно авторско право по Закона за авторското право и сродните му права;',
'ds.invalidityGround.29.2.1')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(109,'REL',2019,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон:  по-ранно право върху марка;',
'ds.invalidityGround.29.2.2a')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(110,'REL',2011,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон:  по-ранно право върху марка;',
'ds.invalidityGround.29.2.2a')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(111,'REL',2019,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон: по-ранно право върху марка на Общността;',
'ds.invalidityGround.29.2.2b')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(112,'REL',2011,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон: по-ранно право върху марка на Общността;',
'ds.invalidityGround.29.2.2b')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(113,'REL',2019,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон: по-ранно право върху полезен модел, който се ползва от закрилата на ЗПРПМ; ',
'ds.invalidityGround.29.2.2v')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(114,'REL',2011,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон: по-ранно право върху полезен модел, който се ползва от закрилата на ЗПРПМ; ',
'ds.invalidityGround.29.2.2v')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(115,'REL',2019,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон: по-ранно право върху патент, който се ползва от закрилата на ЗПРПМ. ',
'ds.invalidityGround.29.2.2g')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(116,'REL',2011,
'Чл. 29. (2)',
'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон: по-ранно право върху патент, който се ползва от закрилата на ЗПРПМ. ',
'ds.invalidityGround.29.2.2g')

insert into ext_Core.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
values (24,'Заличаване на база съдебно решение','courtAdmittedOwner ',2019)

insert into ext_Core.CF_EARLIER_RIGHT_TYPES (id, name, earlier_right_type_code, version)
values (25,'Заличаване на база съдебно решение','courtAdmittedOwner ',2011)

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(117,'REL',2019,
'чл. 29. (4) ',
'Регистрацията се заличава и когато по исков ред е установено, че вписаният притежател не е едно от лицата, посочени в чл. 16 в едномесечен срок от влизането в сила на съдебното решение не е постъпило искане за вписване на действителния притежател.',
'ds.invalidityGround.29.4')

insert into ext_Core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values(118,'REL',2011,
'чл. 29. (4) ',
'Регистрацията се заличава и когато по исков ред е установено, че вписаният притежател не е едно от лицата, посочени в чл. 16 в едномесечен срок от влизането в сила на съдебното решение не е постъпило искане за вписване на действителния притежател.',
'ds.invalidityGround.29.4')

INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,85)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,87)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,89)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,91)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,93)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,95)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,97)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,99)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,101)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,103)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(20,105)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(22,107)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(22,109)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(22,111)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(22,113)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(22,115)
INSERT INTO ext_core.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(24,117)

insert into ext_core.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN)
values ('DesignAnnulmentRequest','N','Основания за заличаване регистрацията на дизайн','Design invalidity')

insert into ext_core.CF_EARLIER_RIGHT_TO_PANEL (earlier_right_type_id, panel) values (20,'DesignAnnulmentRequest')
insert into ext_core.CF_EARLIER_RIGHT_TO_PANEL (earlier_right_type_id, panel) values (22,'DesignAnnulmentRequest')
insert into ext_core.CF_EARLIER_RIGHT_TO_PANEL (earlier_right_type_id, panel) values (24,'DesignAnnulmentRequest')

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('userdoc.userdoc-design-annulment-request-data','Вторични действия: Преглед на панел "Основания за заличаване регистрацията на дизайн"');

insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (85,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (87,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (89,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (91,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (93,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (95,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (97,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (99,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (101,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (103,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (105,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (107,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (109,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (111,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (113,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (115,'DesignAnnulmentRequest')
insert into ext_core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values (117,'DesignAnnulmentRequest')
insert into ext_core.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY (earlier_right_type_id, applicant_authority_type_id) values(22,1)
insert into ext_core.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY (earlier_right_type_id, applicant_authority_type_id) values(22,4)
insert into ext_core.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) values('ИЗРД','DesignAnnulmentRequest')

--changeset mmurlev:264.2
ALTER TABLE EXT_cORE.IP_PATENT_GROUND_DATA add DESIGN_FILING_NUMBER VARCHAR(100)