--liquibase formatted sql

--changeset dveizov:215
create table EXT_CORE.IP_USERDOC_SINGLE_DESIGN
(
    ROW_VERSION numeric(9)  not null,
    DOC_ORI     varchar(4)  not null,
    DOC_LOG     varchar(1)  not null,
    DOC_SER     numeric(4)  not null,
    DOC_NBR     numeric(15) not null,
    FILE_SEQ    varchar(2)  not null,
    FILE_TYP    varchar(1)  not null,
    FILE_SER    numeric(4)  not null,
    FILE_NBR    numeric(10) not null,
    APPL_TYP    varchar(3)  not null,
    APPL_SUBTYP    varchar(2)  not null,
    PRODUCT_TITLE varchar (2000),

    constraint IP_USERDOC_SINGLE_DESIGNS_PK
        primary key nonclustered (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR),
    constraint IP_USERDOC_SINGLE_DESIGNS_IP_USERDOC_FK
        foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER) references IPASPROD.IP_USERDOC
            on update cascade on delete cascade
);


create table EXT_CORE.IP_USERDOC_SINGLE_DESIGN_LOCARNO_CLASSES
(
    ROW_VERSION numeric(9)  not null,
    DOC_ORI     varchar(4)  not null,
    DOC_LOG     varchar(1)  not null,
    DOC_SER     numeric(4)  not null,
    DOC_NBR     numeric(15) not null,
    FILE_SEQ    varchar(2)  not null,
    FILE_TYP    varchar(1)  not null,
    FILE_SER    numeric(4)  not null,
    FILE_NBR    numeric(10) not null,
    LOCARNO_CLASS_CODE    varchar(5)  not null,
    LOCARNO_EDITION_CODE    varchar(10)  not null

    constraint IP_USERDOC_SINGLE_DESIGN_LOCARNO_CLASSES_PK
        primary key nonclustered (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR,LOCARNO_CLASS_CODE),
    constraint IP_USERDOC_SINGLE_DESIGN_LOCARNO_CLASSES_IP_USERDOC_SINGLE_DESIGN_FK
        foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER, FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR) references EXT_CORE.IP_USERDOC_SINGLE_DESIGN
            on update cascade on delete cascade,
    constraint IP_USERDOC_SINGLE_DESIGN_LOCARNO_CLASSES_CF_LOCARNO_FK
		foreign key (LOCARNO_EDITION_CODE, LOCARNO_CLASS_CODE) references CF_CLASS_LOCARNO (LOCARNO_EDITION_CODE, LOCARNO_CLASS_CODE),
);


