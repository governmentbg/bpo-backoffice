--liquibase formatted sql

--changeset mmurlev:173
create table EXT_CORE.IP_USERDOC_REVIEWERS
(
    DOC_ORI varchar(4) not null,
    DOC_LOG varchar(1) not null,
    DOC_SER numeric(4) not null,
    DOC_NBR numeric(15) not null,
    USER_ID numeric(5),
    primary key (DOC_ORI,DOC_SER,DOC_LOG,DOC_NBR,USER_ID),
    constraint IP_USERDOC_REVIEWER_SQUAD_USER_ID_FK
        foreign key (USER_ID) references IP_USER(USER_ID),
);


