--liquibase formatted sql

--changeset dveizov:206
create table EXT_CORE.IP_PERSON_REPLACE_LOG
(
	ID numeric(8) identity
		constraint PK_IP_PERSON_REPLACE_LOG
			primary key,
	OLD_PERSON_NBR numeric(8) not null,
	NEW_PERSON_NBR numeric(8) not null,
	OLD_ADDRESS_NBR numeric(4) not null,
	NEW_ADDRESS_NBR numeric(4) not null,
	OLD_PERSON_NAME nvarchar(700),
	NEW_PERSON_NAME nvarchar(700),
    OLD_ADDR_STREET nvarchar(2000),
    NEW_ADDR_STREET nvarchar(2000),
    OLD_CITY_NAME nvarchar(254),
    NEW_CITY_NAME nvarchar(254),
    OLD_NATIONALITY_COUNTRY_CODE varchar(2),
    NEW_NATIONALITY_COUNTRY_CODE varchar(2),
    OLD_RESIDENCE_COUNTRY_CODE varchar(2),
    NEW_RESIDENCE_COUNTRY_CODE varchar(2),
    OLD_PERSON_WCODE varchar(1),
    NEW_PERSON_WCODE varchar(1),
    OLD_AGENT_CODE numeric(5),
    NEW_AGENT_CODE numeric(5),
    OLD_TELEPHONE varchar(200),
    NEW_TELEPHONE varchar(200),
    OLD_EMAIL varchar(200),
    NEW_EMAIL varchar(200),
    OLD_GRAL_PERSON_ID_TYP varchar(4),
    NEW_GRAL_PERSON_ID_TYP varchar(4),
    OLD_GRAL_PERSON_ID_NBR numeric(15),
    NEW_GRAL_PERSON_ID_NBR numeric(15),
    OLD_ADDR_ZONE nvarchar(254),
    NEW_ADDR_ZONE nvarchar(254),
    OLD_CITY_CODE varchar(6),
    NEW_CITY_CODE varchar(6),
    OLD_STATE_CODE varchar(6),
    NEW_STATE_CODE varchar(6),
	OLD_STATE_NAME varchar(254),
	NEW_STATE_NAME varchar(254),
    OLD_ZIPCODE varchar(16),
    NEW_ZIPCODE varchar(16),
    REPLACE_INFO varchar(4000),
	DATE datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
);