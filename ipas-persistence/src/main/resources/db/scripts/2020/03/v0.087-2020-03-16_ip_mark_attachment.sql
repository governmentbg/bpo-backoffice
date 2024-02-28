create table EXT_CORE.IP_MARK_ATTACHMENT
(
    ROW_VERSION              numeric(9)     not null,
    ID                       numeric(9) identity
        constraint PK_IP_MARK_ATTACHMENT primary key,
    FILE_SEQ                 varchar(2)     not null,
    FILE_TYP                 varchar(1)     not null,
    FILE_SER                 numeric(4)     not null,
    FILE_NBR                 numeric(10)    not null,
    MIME_TYPE                varchar(255)   not null,
    DATA                     varbinary(max) not null,
    COLOUR_DESCRIPTION       text,
    COLOUR_DESCRIPTION_LANG2 text,
    IMAGE_VIEW_TITLE         varchar(2000),
    SEQ_NBR                  numeric(3)
);
;

alter table EXT_CORE.IP_MARK_ATTACHMENT
    add constraint IP_MARK_ATTACHMENT_IP_MARK_FK
        foreign key (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) references IPASPROD.IP_MARK (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) on update cascade on delete cascade
;


create table EXT_CORE.IP_MARK_ATTACHMENT_VIENNA_CLASSES
(
    ROW_VERSION            numeric(9)  not null,
    ATTACHMENT_ID          numeric(9)  not null,
    FILE_SEQ               varchar(2)  not null,
    FILE_TYP               varchar(1)  not null,
    FILE_SER               numeric(4)  not null,
    FILE_NBR               numeric(10) not null,
    VIENNA_CLASS_CODE      numeric(2)  not null,
    VIENNA_GROUP_CODE      numeric(2)  not null,
    VIENNA_ELEM_CODE       numeric(2)  not null,
    VIENNA_EDITION_CODE    varchar(10) not null,
    VCL_WPUBLISH_VALIDATED varchar(1),
    constraint PK_IP_MARK_ATTACHMENT_VIENNA_CLASSES
        primary key nonclustered (ATTACHMENT_ID, FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER, VIENNA_CLASS_CODE,
                                  VIENNA_GROUP_CODE,
                                  VIENNA_ELEM_CODE)
);
;

alter table EXT_CORE.IP_MARK_ATTACHMENT_VIENNA_CLASSES
    add constraint IP_MARK_ATTACHMENT_VIENNA_CLASSES_IP_MARK_ATTACHMENT_FK
        foreign key (ATTACHMENT_ID) references EXT_CORE.IP_MARK_ATTACHMENT (ID) on update cascade on delete cascade
;


