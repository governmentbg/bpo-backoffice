create table EXT_CORE.SINGLE_DESIGN_EXTENDED
(
    FILE_SEQ                  varchar(2)  not null,
    FILE_TYP                  varchar(1)  not null,
    FILE_SER                  numeric(4)  not null,
    FILE_NBR                  numeric(10) not null,
    DRAWING_NBR               numeric(15) not null,
    IMAGE_REFUSED             bit default 0,
    IMAGE_PUBLISHED           bit default 0,
    VIEW_TYPE_ID              numeric(3)  not null
    constraint PK_SINGLE_DESIGN_EXTENDED
        primary key (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR,DRAWING_NBR)
)
;

ALTER TABLE EXT_CORE.SINGLE_DESIGN_EXTENDED
ADD CONSTRAINT SINGLE_DESIGN_EXTENDED_FK
FOREIGN KEY (VIEW_TYPE_ID) REFERENCES EXT_CORE.CF_IMAGE_VIEW_TYPE (VIEW_TYPE_ID)


