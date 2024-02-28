INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (9, 'Авторско право');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (10, 'Право на име и портрет');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (11, 'Право на селекционер върху наименование на сорт или порода');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TYPES (id, name) VALUES (12, 'Право на индустриална собственост');

delete from EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL  where earlier_right_type_id=2 and panel='Invalidity';

INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL (earlier_right_type_id, panel) VALUES (9, 'Invalidity');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL (earlier_right_type_id, panel) VALUES (10, 'Invalidity');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL (earlier_right_type_id, panel) VALUES (11, 'Invalidity');
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL (earlier_right_type_id, panel) VALUES (12, 'Invalidity');

INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY (earlier_right_type_id, applicant_authority_type_id) VALUES (9, 1);
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY (earlier_right_type_id, applicant_authority_type_id) VALUES (10, 1);
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY (earlier_right_type_id, applicant_authority_type_id) VALUES (11, 1);
INSERT INTO EXT_CORE.CF_EARLIER_RIGHT_TO_APPLICANT_AUTHORITY (earlier_right_type_id, applicant_authority_type_id) VALUES (12, 1);

create table EXT_CORE.CF_GROUND_MARK_TYPE
(
    id int not null primary key,
    name varchar(30)
);

insert into EXT_CORE.CF_GROUND_MARK_TYPE values(1, 'Национална марка');
insert into EXT_CORE.CF_GROUND_MARK_TYPE values(2, 'Международна марка');
insert into EXT_CORE.CF_GROUND_MARK_TYPE values(3, 'Марка на общността');


alter table ext_core.IP_USERDOC_ROOT_GROUNDS add EARLIER_RIGHT_TYPE_ID int;
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add constraint EARLIER_RIGHT_TYPE_ID_FK
foreign key (EARLIER_RIGHT_TYPE_ID) references ext_core.CF_EARLIER_RIGHT_TYPES

alter table ext_core.IP_USERDOC_ROOT_GROUNDS add APPLICANT_AUTHORITY_ID int;
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add constraint APPLICANT_AUTHORITY_ID_FK
foreign key (APPLICANT_AUTHORITY_ID) references ext_core.CF_APPLICANT_AUTHORITY

alter table ext_core.IP_USERDOC_ROOT_GROUNDS add REGISTRATION_COUNTRY varchar(2);
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add constraint REGISTRATION_COUNTRY_FK
foreign key (REGISTRATION_COUNTRY) references CF_GEO_COUNTRY

alter table ext_core.IP_USERDOC_ROOT_GROUNDS add GROUND_MARK_TYPE_ID int;
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add constraint GROUND_MARK_TYPE_ID_FK
foreign key (GROUND_MARK_TYPE_ID) references ext_core.CF_GROUND_MARK_TYPE

alter table ext_core.IP_USERDOC_ROOT_GROUNDS add REGISTRATION_NBR int;
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add REGISTRATION_DATE datetime;
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add NICE_CLASSES_IND varchar(1);
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add GROUND_COMMON_TEXT text;
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add NAME_TEXT text;
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add NAME_DATA image;
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add FILING_DATE datetime;
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add FILING_NUMBER varchar(50);

create table EXT_CORE.IP_USERDOC_GROUND_NICE_CLASSES
(
    ROW_VERSION numeric(9) not null,
    ROOT_GROUND_ID int not null,
    DOC_ORI     varchar(4)  not null,
    DOC_LOG     varchar(1)  not null,
    DOC_SER     numeric(4)  not null,
    DOC_NBR     numeric(15) not null,
    NICE_CLASS_CODE numeric(2) not null,
    NICE_CLASS_STATUS_WCODE varchar(1) not null,
    NICE_CLASS_DESCRIPTION text,
    NICE_CLASS_EDITION numeric(2) not null,
    NICE_CLASS_DESCR_LANG2 text,
    NICE_CLASS_VERSION varchar(10) not null,
    ALL_TERMS_DECLARATION varchar(1),

    constraint IP_USERDOC_GROUND_NICE_CLASSES_PK
        primary key nonclustered (ROOT_GROUND_ID, DOC_ORI,DOC_SER,DOC_LOG,DOC_NBR, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE),
    constraint IP_USERDOC_GROUND_NICE_CLASSES_NICE_CLASS_FK
        foreign key (NICE_CLASS_EDITION, NICE_CLASS_VERSION, NICE_CLASS_CODE) references CF_CLASS_NICE (NICE_CLASS_EDITION, NICE_CLASS_VERSION, NICE_CLASS_CODE),
    constraint IP_USERDOC_GROUND_NICE_CLASSES_IP_USERDOC_GROUND_FK
        foreign key (ROOT_GROUND_ID, DOC_ORI,DOC_SER,DOC_LOG,DOC_NBR) references EXT_CORE.IP_USERDOC_ROOT_GROUNDS
            on update cascade on delete cascade
)

