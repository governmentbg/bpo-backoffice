--liquibase formatted sql

--changeset murlev:360.1
DROP TABLE EXT_CORE.ACP_TAKEN_ITEM;

--changeset murlev:360.2
create table ext_Core.CF_ACP_TAKEN_ITEM_TYPE
(
    ID numeric(8) not null
        constraint PK_CF_ACP_TAKEN_ITEM_TYPE
            primary key,
    NAME varchar(255)
);

--changeset murlev:360.3
create table ext_Core.CF_ACP_TAKEN_ITEM_STORAGE
(
    ID numeric(8) not null
        constraint PK_CF_ACP_TAKEN_ITEM_STORAGE
            primary key,
    NAME varchar(255)
);

--changeset murlev:360.4
insert into ext_core.CF_ACP_TAKEN_ITEM_STORAGE(ID, NAME) values(1,'ПВ');
insert into ext_core.CF_ACP_TAKEN_ITEM_STORAGE(ID, NAME) values(2,'Друго');
insert into ext_core.CF_ACP_TAKEN_ITEM_TYPE(ID, NAME) values (1,'Облекло и обувки и шапки');
insert into ext_core.CF_ACP_TAKEN_ITEM_TYPE(ID, NAME) values (2,'Часовници, аксесоари');
insert into ext_core.CF_ACP_TAKEN_ITEM_TYPE(ID, NAME) values (3,'Парфюмерия и козметика');
insert into ext_core.CF_ACP_TAKEN_ITEM_TYPE(ID, NAME) values (4,'Очила');
insert into ext_core.CF_ACP_TAKEN_ITEM_TYPE(ID, NAME) values (5,'Услуги');
insert into ext_core.CF_ACP_TAKEN_ITEM_TYPE(ID, NAME) values (6,'Други');

--changeset murlev:360.5
create table EXT_CORE.ACP_TAKEN_ITEM
(
    TAKEN_ITEM_ID   int         not null,
    FILE_SEQ        varchar(2)  not null,
    FILE_TYP        varchar     not null,
    FILE_SER        numeric(4)  not null,
    FILE_NBR        numeric(10) not null,
    TYPE_ID numeric(8)
        constraint TYPE_ID_FK
            references EXT_CORE.CF_ACP_TAKEN_ITEM_TYPE,
    TYPE_DESCRIPTION            text,
    COUNT           int,
    STORAGE_ID numeric(8)
        constraint STORAGE_ID_FK
            references EXT_CORE.CF_ACP_TAKEN_ITEM_STORAGE,
    STORAGE_DESCRIPTION         text,
    FOR_DESTRUCTION bit default 0,
    RETURNED        bit default 0,
    IN_STOCK        bit default 0,
    constraint ACP_TAKEN_ITEMS_PK
        primary key nonclustered (TAKEN_ITEM_ID, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
);

