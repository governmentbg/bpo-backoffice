create table EXT_RECEPTION.RECEPTION_USERDOC_REQUEST
(
    ID                 numeric(8) identity
        constraint PK_RECEPTION_USERDOC_REQUEST
            primary key,
    DOC_ORI            varchar(4)  not null,
    DOC_LOG            varchar(1)  not null,
    DOC_SER            numeric(4)  not null,
    DOC_NBR            numeric(15) not null,
    DOC_SEQ_TYP        varchar(12) not null,
    DOC_SEQ_NBR        numeric(15) not null,
    DOC_SEQ_SER        numeric(4)  not null,
    USERDOC_TYPE       varchar(50) not null,
    EXTERNAL_ID        numeric(8),
    EXTERNAL_SYSTEM_ID varchar(50),
    ORIGINAL_EXPECTED  bit default 0,
    SUBMISSION_TYPE    numeric(8)
        constraint RECEPTION_USERDOC_REQUEST_SUBMISSION_TYPE
            references EXT_RECEPTION.CF_SUBMISSION_TYPE,
    FILING_DATE        datetime,
    NOTES              varchar(500),
    RELATED_OBJECT_SEQ varchar(4)  not null,
    RELATED_OBJECT_TYP varchar(1)  not null,
    RELATED_OBJECT_SER numeric(4)  not null,
    RELATED_OBJECT_NBR numeric(15) not null
);

create table EXT_RECEPTION.USERDOC_CORRESPONDENT
(
    RECEPTION_USERDOC_REQUEST_ID numeric(8) not null
        constraint FK_USERDOC_CORRESPONDENT_REQUEST
            references EXT_RECEPTION.RECEPTION_USERDOC_REQUEST,
    PERSON_NBR numeric(18) not null,
    ADDRESS_NBR numeric(18) not null,
    TYPE numeric(8)
        constraint FK_USERDOC_CORRESPONDENT_TYPE
            references EXT_RECEPTION.CF_CORRESPONDENT_TYPE,
    constraint USERDOC_CORRESPONDENT_RECEPTION_REQUEST_ID_PERSON_NBR_ADDRESS_NBR_pk
        primary key (RECEPTION_USERDOC_REQUEST_ID, PERSON_NBR, ADDRESS_NBR)
);


