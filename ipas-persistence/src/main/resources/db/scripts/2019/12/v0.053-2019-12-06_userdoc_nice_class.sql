create table EXT_CORE.IP_USERDOC_NICE_CLASSES
(
    ROW_VERSION numeric(9) not null,
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

    constraint IP_USERDOC_NICE_CLASSES_PK
        primary key nonclustered (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE),
    constraint IP_USERDOC_NICE_CLASSES_CF_CLASS_NICE_FK
        foreign key (NICE_CLASS_EDITION, NICE_CLASS_VERSION, NICE_CLASS_CODE) references IPASPROD.CF_CLASS_NICE (NICE_CLASS_EDITION, NICE_CLASS_VERSION, NICE_CLASS_CODE),
    constraint IP_USERDOC_NICE_CLASSES_IP_USERDOC_FK
        foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER) references IPASPROD.IP_USERDOC
            on update cascade on delete cascade
)
;


