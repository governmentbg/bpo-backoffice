drop table EXT_CORE.IP_USERDOC_GROUND_NICE_CLASSES

create table EXT_CORE.IP_USERDOC_GROUND_NICE_CLASSES
(
    ROW_VERSION numeric(9) not null,
    ROOT_GROUND_ID int not null,
    DOC_ORI     varchar(4)  not null,
    DOC_LOG     varchar(1)  not null,
    DOC_SER     numeric(4)  not null,
    DOC_NBR     numeric(15) not null,
    NICE_CLASS_CODE numeric(2) not null,
    NICE_CLASS_DESCRIPTION text

    constraint IP_USERDOC_GROUND_NICE_CLASSES_PK
        primary key nonclustered (ROOT_GROUND_ID, DOC_ORI,DOC_SER,DOC_LOG,DOC_NBR, NICE_CLASS_CODE),
    constraint IP_USERDOC_GROUND_NICE_CLASSES_IP_USERDOC_GROUND_FK
        foreign key (ROOT_GROUND_ID, DOC_ORI,DOC_SER,DOC_LOG,DOC_NBR) references EXT_CORE.IP_USERDOC_ROOT_GROUNDS
            on update cascade on delete cascade
)


