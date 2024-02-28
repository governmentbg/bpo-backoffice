--liquibase formatted sql

--changeset ggeorgiev:328.1
create table EXT_CORE.CF_LANG_PROPERTIES
(
    lang varchar(3) not null,
    [key] varchar(255) not null,
    value nvarchar(2000) not null
);

--changeset ggeorgiev:328.2
ALTER TABLE EXT_CORE.CF_LANG_PROPERTIES ADD  CONSTRAINT [CF_LANG_PROPERTIES_PK] PRIMARY KEY NONCLUSTERED
(
    lang ASC,
    [key] ASC
);


