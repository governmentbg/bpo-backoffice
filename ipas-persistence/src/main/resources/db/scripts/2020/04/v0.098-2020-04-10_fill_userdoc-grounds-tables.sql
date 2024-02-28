delete from EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL where PANEL in ('Claim', 'Opposition', 'Opposition', 'Invalidity', 'Revocation', 'Objection','Goods');
delete from EXT_CORE.CF_USERDOC_UI_PANEL where PANEL in ('Claim', 'Opposition', 'Goods', 'Invalidity', 'Revocation', 'Objection','Goods');

INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) VALUES ('Claim', 'N', 'Жалба', 'Appeal');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) VALUES ('Opposition', 'N', 'Основания за опозиция', 'Opposition');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) VALUES ('Invalidity', 'N', 'Основания за заличаване', 'Invalidity claim');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) VALUES ('Revocation', 'N', 'Основания за отмяна', 'Revocation claim');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) VALUES ('Goods', 'N', 'Засегнати стоки и услуги', 'Affected goods and services');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) VALUES ('Objection', 'N', 'Основания за възражение', 'Objection');


INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ОАА', 'Claim');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ЖО', 'Claim');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ЖПП', 'Claim');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ЖРО', 'Claim');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ОПО', 'Opposition');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('Въз', 'Objection');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ИЗРM', 'Invalidity');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('СЗ', 'Invalidity');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ИОСПЗ', 'Invalidity');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ИОРМ', 'Revocation');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('Въз', 'Goods');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ОПО', 'Goods');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ОАА', 'Goods');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ЖО', 'Goods');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ЖПП', 'Goods');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ЖРО', 'Goods');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ИЗРM', 'Goods');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('СЗ', 'Goods');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ИОСПЗ', 'Goods');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ИОРМ', 'Goods');


create table EXT_CORE.CF_LEGAL_GROUND_CATEGORIES
(
    code varchar(5) not null primary key,
    name varchar(30)
);
insert into EXT_CORE.CF_LEGAL_GROUND_CATEGORIES values('ABS', 'Абсолютни основания');
insert into EXT_CORE.CF_LEGAL_GROUND_CATEGORIES values('REL', 'Относителни основания');

create table EXT_CORE.CF_LEGAL_GROUND_TYPES
(
    id integer not null primary key,
    legal_ground_category varchar(5) not null references EXT_CORE.CF_LEGAL_GROUND_CATEGORIES(code),
	version varchar(10),
    title varchar(100) not null,
    descr varchar(1000)

);
INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category,version, title, descr) VALUES (1, 'ABS','2019', 'ЗМГО 2019, чл.2', 'Разпоредбите на този закон се прилагат по отношение на български физически и юридически лица, както и по отношение на чуждестранни физически и юридически лица от държави, които участват в международни договори, по които Република България е страна.');
INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category,version, title, descr) VALUES (2, 'ABS','2019', 'ЗМГО 2019, чл.11, ал.1, т.1', 'Не се регистрира: т.1. знак, който не е марка по смисъла на чл. 9, ал. 1;');
INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version,title, descr) VALUES (3, 'ABS','2019', 'ЗМГО 2019, чл.11, ал.1, т.2', 'Не се регистрира: т.2. марка, която няма отличителен характер;');
INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version,title, descr) VALUES (4, 'ABS','2019', 'ЗМГО 2019, чл.11, ал.1, т.3', 'Не се регистрира: т.3. марка, която се състои изключително от знаци или означения, станали обичайни в говоримия език или в установената търговска практика в Република България по отношение на заявените стоки или услуги;');
INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category,version, title, descr) VALUES (5, 'ABS','2019', 'ЗМГО 2019, чл.11, ал.1, т.4', 'Не се регистрира: т.4. марка, която се състои изключително от знаци или означения, които указват вида, качеството, количеството, предназначението, стойността, географския произход, времето или метода на производство на стоките, начина на предоставяне на услугите или други характеристики на стоките или услугите;');
INSERT INTO EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version,title, descr) VALUES (6, 'REL','2019', 'ЗМГО 2019, чл.12, ал.1, т.1', 'Когато е подадена опозиция съгласно чл. 52, не се регистрира марка: т.1. която е идентична на по-ранна марка и стоките или услугите на заявената и на по-ранната марка са идентични;');

------------------------------------------------------------------------------------------------------
create table EXT_CORE.CF_EARLIER_RIGHT_TYPES
(
    id integer not null primary key,
    name varchar(300) not null
);
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (1, 'По-ранна марка по смисъла на чл.12, ал.2, т.1-8 от ЗМГО');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (2, 'Общоизвестна марка-чл.12, ал.2, т.9 от ЗМГО');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (3, 'Ползваща се с известност марка – чл.12, ал.3 от ЗМГО');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (4, 'Нерегистрирана марка, която се използва в търговската дейност – чл.12, ал.4 от ЗМГО');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (5, 'Недобросъвестно подадена заявка – чл.12, ал.5 от ЗМГО');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (6, 'Марка, заявена от агент/ представител – чл.12, ал.6 от ЗМГО');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (7, 'Фирма на търговец, съдържаща се в заявената марка по чл.12, ал.7 от ЗМГО');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (8, 'По-ранно регистрирано географско означение – чл.12, ал.8 от ЗМГО');

create table EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL
(
    earlier_right_type_id integer not null references EXT_CORE.CF_EARLIER_RIGHT_TYPES(id),
    panel varchar(50) not null references EXT_CORE.CF_USERDOC_UI_PANEL (panel),
    primary key (earlier_right_type_id, panel)
);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(1, 'Invalidity');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(2, 'Invalidity');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(3, 'Invalidity');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(4, 'Invalidity');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(5, 'Invalidity');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(6, 'Invalidity');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(7, 'Invalidity');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(8, 'Invalidity');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(1, 'Opposition');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(2, 'Opposition');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(3, 'Opposition');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(4, 'Opposition');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(5, 'Opposition');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(6, 'Opposition');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(7, 'Opposition');
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL(earlier_right_type_id, panel) values(8, 'Opposition');

create table EXT_CORE.CF_EARLIER_RIGHT_TO_LEGAL_GROUND
(
    earlier_right_type_id integer not null references EXT_CORE.CF_EARLIER_RIGHT_TYPES(id),
    legal_ground_type_id integer not null references EXT_CORE.CF_LEGAL_GROUND_TYPES (id),
    primary key (earlier_right_type_id, legal_ground_type_id)
);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_LEGAL_GROUND (earlier_right_type_id, legal_ground_type_id) values(1, 6);
--------------------------------------------------------------------------------------------------

create table EXT_CORE.CF_APPLICANT_AUTHORITY
(
    id integer not null primary key,
    name varchar(300) not null
);
INSERT INTO EXT_CORE.CF_APPLICANT_AUTHORITY(id, name) VALUES(1, 'Притежател');
INSERT INTO EXT_CORE.CF_APPLICANT_AUTHORITY(id, name) VALUES(2, 'Действителен притежател');
INSERT INTO EXT_CORE.CF_APPLICANT_AUTHORITY(id, name) VALUES(3, 'Съпритежател');
INSERT INTO EXT_CORE.CF_APPLICANT_AUTHORITY(id, name) VALUES(4, 'Лицензополучател на изключителна лицензия');
INSERT INTO EXT_CORE.CF_APPLICANT_AUTHORITY(id, name) VALUES(5, 'Търговец, чиято фирма се съдържа в марката');
INSERT INTO EXT_CORE.CF_APPLICANT_AUTHORITY(id, name) VALUES(6, 'Лице с права върху географско означение или лице по смисъла на – чл.12, ал.8 от ЗМГО');

create table EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY
(
    earlier_right_type_id integer not null references EXT_CORE.CF_EARLIER_RIGHT_TYPES(id),
    applicant_authority_type_id integer not null references EXT_CORE.CF_APPLICANT_AUTHORITY(id),
    primary key (earlier_right_type_id, applicant_authority_type_id)
);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(1, 1);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(1, 3);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(1, 4);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(2, 1);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(2, 3);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(2, 4);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(3, 1);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(3, 3);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(3, 4);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(4, 2);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(5, 2);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(6, 2);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(7, 5);
insert into EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY(earlier_right_type_id, applicant_authority_type_id) values(8, 6);

-------------------------------------------------------------------------------------------------------
create table EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL
(
    legal_ground_type_id integer not null references EXT_CORE.CF_LEGAL_GROUND_TYPES (id),
    panel varchar(50) not null references EXT_CORE.CF_USERDOC_UI_PANEL (panel),
    primary key (legal_ground_type_id, panel)
);
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(1, 'Invalidity');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(2, 'Invalidity');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(3, 'Invalidity');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(4, 'Invalidity');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(5, 'Invalidity');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(6, 'Invalidity');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(1, 'Opposition');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(2, 'Opposition');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(3, 'Opposition');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(4, 'Opposition');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(5, 'Opposition');
insert into EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL(legal_ground_type_id, panel) values(6, 'Opposition');


create table EXT_CORE.IP_USERDOC_SUB_GROUNDS
(
    ROOT_GROUND_ID integer not null,
    DOC_ORI varchar(4) not null,
    DOC_LOG varchar(1) not null,
    DOC_SER numeric(4) not null,
    DOC_NBR numeric(15) not null,
    LEGAL_GROUND_TYPE_ID integer not null references EXT_CORE.CF_LEGAL_GROUND_TYPES(id) ,
    primary key (ROOT_GROUND_ID,DOC_ORI,DOC_LOG,DOC_SER,DOC_NBR,LEGAL_GROUND_TYPE_ID)
);
;

create table EXT_CORE.IP_USERDOC_ROOT_GROUNDS
(
    ROOT_GROUND_ID integer not null,
    DOC_ORI varchar(4) not null,
    DOC_LOG varchar(1) not null,
    DOC_SER numeric(4) not null,
    DOC_NBR numeric(15) not null,
    MOTIVES varchar(2000) ,
    primary key (ROOT_GROUND_ID, DOC_ORI,DOC_SER,DOC_LOG,DOC_NBR)
);
;

