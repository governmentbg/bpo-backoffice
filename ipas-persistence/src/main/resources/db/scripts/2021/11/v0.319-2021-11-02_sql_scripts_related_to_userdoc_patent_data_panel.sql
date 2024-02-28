--liquibase formatted sql

--changeset mmurlev:319.1
create table EXT_CORE.IP_USERDOC_PATENT_DATA
(
    DOC_ORI  varchar(4)  not null,
    DOC_LOG  varchar(1)  not null,
    DOC_SER  numeric(4)  not null,
    DOC_NBR  numeric(15) not null,
    TITLE_BG nvarchar(4000),
    DESCRIPTION_PAGES_COUNT integer,
    CLAIMS_COUNT integer,
    DRAWINGS_COUNT integer
    constraint IP_USERDOC_PATENT_DATA_PK
        primary key nonclustered (DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR)
)

--changeset mmurlev:319.2
insert into ext_Core.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) values('UserdocPatentData','N','Данни за патента',null)

--changeset mmurlev:319.3
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-patent-data','Вторични действия: Преглед на панел "Данни за патента"');