--liquibase formatted sql

--changeset mmihova:329.1
create table EXT_CORE.IP_MARK_OLD_INTL_REGISTRATION
(
  ID                      integer identity(1,1) primary key,
  WIPO_REFERENCE          numeric(15),
  BG_REFERENCE            varchar(50),
  RECEIVED                datetime,
  FILE_NBR                numeric(10),
  REGISTRATION_NBR        numeric(10),
  REGISTRATION_DUP        varchar(2),
  INTERNATIONAL_REG_NBR   varchar(30),
  INTERNATIONAL_REG_DATE  datetime,
  HOLDER_NAME             varchar(1000),
  HOLDER_ADDRESS          varchar(1000),
  PROCESSED_DATE          datetime
)