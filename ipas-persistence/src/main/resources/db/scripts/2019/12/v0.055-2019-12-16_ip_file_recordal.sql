create table EXT_CORE.IP_FILE_RECORDAL
(
    FILE_SEQ             varchar(2)  not null,
    FILE_TYP             varchar(1)  not null,
    FILE_SER             numeric(4)  not null,
    FILE_NBR             numeric(10) not null,
    DOC_ORI              varchar(4)  not null,
    DOC_LOG              varchar(1)  not null,
    DOC_SER              numeric(4)  not null,
    DOC_NBR              numeric(15) not null,
    PROC_TYP             varchar(4)  not null,
    PROC_NBR             numeric(8)  not null,
    ACTION_NBR           numeric(10) not null,
    DATE                 datetime    not null,
    TYPE                 varchar(50) not null
        constraint IP_FILE_RECORDAL_CF_USERDOC_UI_PANEL_FK
            references EXT_CORE.CF_USERDOC_UI_PANEL,
    INVALIDATION_DOC_ORI varchar(4),
    INVALIDATION_DOC_LOG varchar(1),
    INVALIDATION_DOC_SER numeric(4),
    INVALIDATION_DOC_NBR numeric(15),
    INVALIDATION_DATE    datetime,
    constraint IP_FILE_RECORDAL_PK
        primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR),
    constraint IP_FILE_RECORDAL_IP_FILE_FK
        foreign key (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) references IP_FILE
            on delete cascade,
    constraint IP_FILE_RECORDAL_IP_USERDOC_FK
        foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER) references IP_USERDOC
            on delete cascade,
    constraint IP_FILE_RECORDAL_IP_ACTION_FK
        foreign key (PROC_NBR, PROC_TYP, ACTION_NBR) references IP_ACTION
            on delete cascade,
    constraint IP_FILE_RECORDAL_IP_USERDOC_INVALIDATION_FK
        foreign key (INVALIDATION_DOC_NBR, INVALIDATION_DOC_ORI, INVALIDATION_DOC_LOG,
                     INVALIDATION_DOC_SER) references IP_USERDOC
)
;

create index IP_FILE_RECORDAL_FILE_SEQ_FILE_TYP_FILE_SER_FILE_NBR_index
    on EXT_CORE.IP_FILE_RECORDAL (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
;

create index IP_FILE_RECORDAL_DOC_ORI_DOC_LOG_DOC_SER_DOC_NBR_index
    on EXT_CORE.IP_FILE_RECORDAL (DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR)
;

create index IP_FILE_RECORDAL_INVALIDATION_DOC_ORI_INVALIDATION_DOC_LOG_INVALIDATION_DOC_SER_INVALIDATION_DOC_NBR_index
    on EXT_CORE.IP_FILE_RECORDAL (INVALIDATION_DOC_ORI, INVALIDATION_DOC_LOG, INVALIDATION_DOC_SER,
                                  INVALIDATION_DOC_NBR)
;


