create table EXT_CORE.IP_USERDOC_PERSON
(
    ROW_VERSION numeric(9)  not null,
    DOC_ORI     varchar(4)  not null,
    DOC_LOG     varchar(1)  not null,
    DOC_SER     numeric(4)  not null,
    DOC_NBR     numeric(15) not null,
    PERSON_NBR  numeric(8)  not null,
    ADDR_NBR    numeric(4)  not null,
    ROLE        varchar(50)         not null
        constraint IP_USERDOC_PERSON_CF_USERDOC_PERSON_ROLE_ROLE_fk
            references EXT_CORE.CF_USERDOC_PERSON_ROLE,
    NOTES       varchar(2000),
    constraint IP_USERDOC_PERSON_PK
        primary key nonclustered (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER, PERSON_NBR, ADDR_NBR, ROLE),
    constraint IP_USERDOC_PERSON_IP_PERSON_ADDRESSES_FK
        foreign key (PERSON_NBR, ADDR_NBR) references IP_PERSON_ADDRESSES,
    constraint IP_USERDOC_PERSON_IP_USERDOC_FK
        foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER) references IP_USERDOC
            on delete cascade
);


