--liquibase formatted sql

--changeset mmurlev:260.1
IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'REGISTRATION_COUNTRY_FK')
alter table ext_core.IP_USERDOC_ROOT_GROUNDS drop constraint REGISTRATION_COUNTRY_FK;

IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'GROUND_MARK_TYPE_ID_FK')
alter table ext_core.IP_USERDOC_ROOT_GROUNDS drop constraint GROUND_MARK_TYPE_ID_FK;

IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'CATEGORY_CODE_FK')
alter table ext_core.IP_USERDOC_ROOT_GROUNDS drop constraint CATEGORY_CODE_FK;

IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'MARK_SIGN_TYPE_FK')
alter table ext_core.IP_USERDOC_ROOT_GROUNDS drop constraint MARK_SIGN_TYPE_FK;

IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'GI_APPL_SUBTYP_FK')
alter table ext_core.IP_USERDOC_ROOT_GROUNDS drop constraint GI_APPL_SUBTYP_FK;

IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where CONSTRAINT_NAME = 'IP_USERDOC_GROUND_NICE_CLASSES_IP_USERDOC_GROUND_FK')
alter table ext_core.IP_USERDOC_GROUND_NICE_CLASSES drop constraint IP_USERDOC_GROUND_NICE_CLASSES_IP_USERDOC_GROUND_FK

--changeset mmurlev:260.2
create table EXT_CORE.IP_MARK_GROUND_DATA
(
    ROOT_GROUND_ID       int         not null,
    DOC_ORI              varchar(4)  not null,
    DOC_LOG              varchar(1)  not null,
    DOC_SER              numeric(4)  not null,
    DOC_NBR              numeric(15) not null,
    REGISTRATION_COUNTRY varchar(2),
    GROUND_MARK_TYPE_ID  int
        constraint GROUND_MARK_TYPE_ID_FK
            references EXT_CORE.CF_GROUND_MARK_TYPE,
    REGISTRATION_DATE    datetime,
    NICE_CLASSES_IND     varchar(1),
    NAME_TEXT            text,
    NAME_DATA            image,
    FILING_DATE          datetime,
    FILING_NUMBER        varchar(50),
    CATEGORY_CODE        varchar(5)
        constraint CATEGORY_CODE_FK
            references EXT_CORE.CF_LEGAL_GROUND_CATEGORIES,
    REGISTRATION_NBR     varchar(50),
    MARK_SIGN_TYPE       varchar(10),
    GI_APPL_TYP          varchar(3),
    GI_APPL_SUBTYP       varchar(2),
    MARK_IMPORTED_IND    varchar(1),
    primary key (ROOT_GROUND_ID, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR),
    constraint ROOT_GROUNT_FK
        foreign key (ROOT_GROUND_ID, DOC_ORI, DOC_SER, DOC_LOG, DOC_NBR) references EXT_CORE.IP_USERDOC_ROOT_GROUNDS
)

--changeset mmurlev:260.3
insert into ext_core.IP_MARK_GROUND_DATA
(ROOT_GROUND_ID, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, REGISTRATION_COUNTRY, GROUND_MARK_TYPE_ID, REGISTRATION_DATE,
 NICE_CLASSES_IND, NAME_TEXT, NAME_DATA, FILING_DATE, FILING_NUMBER, CATEGORY_CODE, REGISTRATION_NBR, MARK_SIGN_TYPE, GI_APPL_TYP, GI_APPL_SUBTYP, MARK_IMPORTED_IND)
select  gr.ROOT_GROUND_ID, gr.DOC_ORI, gr.DOC_LOG, gr.DOC_SER, gr.DOC_NBR, gr.REGISTRATION_COUNTRY, gr.GROUND_MARK_TYPE_ID, gr.REGISTRATION_DATE,
gr.NICE_CLASSES_IND, gr.NAME_TEXT, gr.NAME_DATA, gr.FILING_DATE, gr.FILING_NUMBER, gr.CATEGORY_CODE,
gr.REGISTRATION_NBR, gr.MARK_SIGN_TYPE, gr.GI_APPL_TYP, gr.GI_APPL_SUBTYP, gr.MARK_IMPORTED_IND
from ext_core.IP_USERDOC_ROOT_GROUNDS gr
inner join IP_PROC pr on pr.DOC_NBR = gr.DOC_NBR and pr.DOC_SER = gr.DOC_SER and pr.DOC_LOG = gr.DOC_LOG and pr.DOC_ORI = gr.DOC_ORI
where pr.USERDOC_TYP in ('ИЗРM','ИОСПЗ','ОПО','СЗ')


--changeset mmurlev:260.4
insert into ext_core.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN)
values ('PatentAnnulmentRequest','N','Основания за обявяване в недействителност на патент','Patent invalidity')

insert into ext_core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values (81,'ABS','2019','ЗПРПМ, чл. 26, ал. 3, т. 1','Патентът се обявява за недействителен, когато изобретението е непатентоспособно','pt.invalidityGround.26.3.1')

insert into ext_core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values (82,'ABS','2019','ЗПРПМ, чл. 26, ал. 3, т. 2','Патентът се обявява за недействителен, когато не е разкрита достатъчно ясно и пълно същността на изобретението','pt.invalidityGround.26.3.2')

insert into ext_core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values (83,'ABS','2019','ЗПРПМ, чл. 26, ал. 3, т. 3','Патентът се обявява за недействителен, когато патентопритежателят не е имал право на патент, което е установено с влязло в сила съдебно решение','pt.invalidityGround.26.3.3')

insert into ext_core.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code)
values (84,'ABS','2019','ЗПРПМ, чл. 26, ал. 3, т. 4','Патентът се обявява за недействителен, когато предметът на патента излиза извън съдържанието на заявката, както е подадена, или когато патентът е издаден по разделена заявка - извън съдържанието на по-ранната заявка, както е подадена','pt.invalidityGround.26.3.4')

insert into ext_Core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(81,'PatentAnnulmentRequest')
insert into ext_Core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(82,'PatentAnnulmentRequest')
insert into ext_Core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(83,'PatentAnnulmentRequest')
insert into ext_Core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel)
values(84,'PatentAnnulmentRequest')

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('userdoc.userdoc-patent-annulment-request-data','Вторични действия: Преглед на панел "Основания за обявяване в недействителност на патент"');

--changeset mmurlev:260.5
create table EXT_CORE.IP_PATENT_GROUND_DATA
(
    ROOT_GROUND_ID       int         not null,
    DOC_ORI              varchar(4)  not null,
    DOC_LOG              varchar(1)  not null,
    DOC_SER              numeric(4)  not null,
    DOC_NBR              numeric(15) not null,
    PARTIAL_INVALIDITY   bit default 0,
    primary key (ROOT_GROUND_ID, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR),
    constraint PATENT_GROUND_DATA_FK
        foreign key (ROOT_GROUND_ID, DOC_ORI, DOC_SER, DOC_LOG, DOC_NBR) references EXT_CORE.IP_USERDOC_ROOT_GROUNDS
)

--changeset mmurlev:260.6
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES (N'ИОПН', N'PatentAnnulmentRequest');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES (N'ЕПИОН', N'PatentAnnulmentRequest');

--changeset mmurlev:260.7
delete from ext_Core.CF_USERDOC_TYPE_TO_UI_PANEL where USERDOC_TYP ='ИОПН' and panel = 'Invalidity'

--changeset mmurlev:260.8
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN REGISTRATION_COUNTRY ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN GROUND_MARK_TYPE_ID ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN REGISTRATION_DATE ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN NICE_CLASSES_IND ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN NAME_TEXT ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN NAME_DATA ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN FILING_DATE ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN FILING_NUMBER ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN CATEGORY_CODE ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN REGISTRATION_NBR ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN MARK_SIGN_TYPE ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN GI_APPL_TYP ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN GI_APPL_SUBTYP ;
ALTER TABLE EXT_CORE.IP_USERDOC_ROOT_GROUNDS DROP COLUMN MARK_IMPORTED_IND ;