--liquibase formatted sql

--changeset mmihova:326.1
CREATE TABLE EXT_CORE.IP_USERDOC_OLD_DOCUMENT (
 ID int identity primary key,
 FILE_SEQ varchar(2) not null ,
 FILE_TYP varchar(1) not null ,
 FILE_SER numeric(4) not null ,
 FILE_NBR numeric(10) not null ,
 EXTERNAL_SYSTEM_ID varchar(50) not null ,
 FILING_DATE datetime,
 NEW_USERDOC_TYPE varchar(7)
)