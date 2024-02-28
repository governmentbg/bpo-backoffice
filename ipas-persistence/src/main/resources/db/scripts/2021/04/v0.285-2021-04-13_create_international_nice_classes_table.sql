--liquibase formatted sql

--changeset mmihova:285.1
create table EXT_CORE.IP_INTERNATIONAL_NICE_CLASSES
(
    ID                     numeric(8) identity
        constraint PK_IP_INTERNATIONAL_NICE_CLASSES
            primary key,
    FILE_SEQ               varchar(2),
    FILE_TYP               varchar(1),
    FILE_SER               numeric(4),
    FILE_NBR               numeric(10),
    DOC_ORI                varchar(4),
    DOC_LOG                varchar(1),
    DOC_SER                numeric(4),
    DOC_NBR                numeric(15),
    INTL_TAG_CODE          varchar(100),
    INTL_TAG_DESCRIPTION   text,
    NICE_CLASS_CODE        numeric(2),
    NICE_CLASS_DESCRIPTION text

    constraint IP_INTERNATIONAL_NICE_CLASSES_IP_USERDOC_FK
        foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER) references IPASPROD.IP_USERDOC
            on update cascade on delete cascade,
    constraint IP_INTERNATIONAL_NICE_CLASSES_IP_MARK_FK
        foreign key (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) references IPASPROD.IP_MARK
            on update cascade on delete cascade
);