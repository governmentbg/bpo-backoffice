--liquibase formatted sql

--changeset raneva:216
create table EXT_CORE.CF_NICE_CLASS_LIST
(
    NICE_CLASS_CODE numeric(2)  not null,
    ALPHA_LIST text,
    HEADING text
    constraint CF_NICE_CLASS_LIST_PK
        primary key nonclustered (NICE_CLASS_CODE),

);