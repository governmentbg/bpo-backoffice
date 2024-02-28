--liquibase formatted sql

--changeset mmurlev:275.1
create table EXT_CORE.IP_SINGLE_DESIGN_GROUND_DATA
(
    ROOT_GROUND_ID       int         not null,
    DOC_ORI              varchar(4)  not null,
    DOC_LOG              varchar(1)  not null,
    DOC_SER              numeric(4)  not null,
    DOC_NBR              numeric(15) not null,
    FILE_SEQ VARCHAR(2) NOT NULL,
    FILE_TYP VARCHAR(1) NOT NULL,
    FILE_SER NUMERIC(4) NOT NULL,
    FILE_NBR NUMERIC(10) NOT NULL,
    primary key (ROOT_GROUND_ID, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR,FILE_SEQ,FILE_TYP,FILE_SER,FILE_NBR),
    constraint SINGLE_DESIGN_ROOT_GROUNT_FK
        foreign key (ROOT_GROUND_ID, DOC_ORI, DOC_SER, DOC_LOG, DOC_NBR) references EXT_CORE.IP_USERDOC_ROOT_GROUNDS,
    constraint SINGLE_DESIGN_FK
        foreign key (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) references IP_PATENT
)

--changeset mmurlev:275.2
ALTER TABLE ext_core.IP_PATENT_GROUND_DATA DROP COLUMN DESIGN_FILING_NUMBER;
