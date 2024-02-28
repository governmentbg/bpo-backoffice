create table EXT_CORE.SPC_EXTENDED
(
    FILE_SEQ                  varchar(2)  not null,
    FILE_TYP                  varchar(1)  not null,
    FILE_SER                  numeric(4)  not null,
    FILE_NBR                  numeric(10) not null,
    BG_PERMIT_NUMBER     varchar(100) ,
    BG_PERMIT_DATE DATETIME,
    EU_PERMIT_NUMBER varchar(100),
    EU_PERMIT_DATE DATETIME
    constraint PK_SPC_EXTENDED
        primary key (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
);



