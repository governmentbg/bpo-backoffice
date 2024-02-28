--liquibase formatted sql

--changeset raneva:203.1
create table EXT_CORE.IP_USERDOC_APPROVED_NICE_CLASSES
(
	ROW_VERSION numeric(9) not null,
	DOC_ORI varchar(4) not null,
	DOC_LOG varchar(1) not null,
	DOC_SER numeric(4) not null,
	DOC_NBR numeric(15) not null,
	NICE_CLASS_CODE numeric(2) not null,
	NICE_CLASS_STATUS_WCODE varchar(1) not null,
	NICE_CLASS_DESCRIPTION text,
	NICE_CLASS_EDITION numeric(2) not null,
	NICE_CLASS_DESCR_LANG2 text,
	NICE_CLASS_VERSION varchar(10) not null,
	ALL_TERMS_DECLARATION varchar(1),
	constraint IP_USERDOC_APPROVED_NICE_CLASSES_PK
		primary key nonclustered (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER, NICE_CLASS_CODE, NICE_CLASS_STATUS_WCODE),
	constraint IP_USERDOC_APPROVED_NICE_CLASSES_CF_CLASS_NICE_FK
		foreign key (NICE_CLASS_EDITION, NICE_CLASS_VERSION, NICE_CLASS_CODE) references CF_CLASS_NICE (NICE_CLASS_EDITION, NICE_CLASS_VERSION, NICE_CLASS_CODE),
	constraint IP_USERDOC_APPROVED_NICE_CLASSES_IP_USERDOC_FK
		foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER) references IP_USERDOC
			on update cascade on delete cascade
)

--changeset raneva:203.2
create table EXT_CORE.IP_USERDOC_APPROVED_DATA(
    DOC_ORI varchar(4) not null,
	DOC_LOG varchar(1) not null,
	DOC_SER numeric(4) not null,
	DOC_NBR numeric(15) not null,
	APPROVED_ALL_NICE varchar(1)
	constraint IP_USERDOC_APPROVED_DATA_PK
		primary key nonclustered (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER),
	constraint IP_USERDOC_APPROVED_DATA_IP_USERDOC_FK
		foreign key (DOC_NBR, DOC_ORI, DOC_LOG, DOC_SER) references IP_USERDOC
			on update cascade on delete cascade
)

--changeset raneva:203.3
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-approved-data','Вторични действия: Преглед на панел "Одобрени данни"');
INSERT INTO EXT_USER.CF_GROUP_SECURITY_ROLE (GROUP_ID, ROLE_NAME, DATE_CREATED, USER_CREATED ) VALUES (1, 'userdoc.userdoc-approved-data','2020-09-07', 4);

--changeset raneva:203.4
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) VALUES ('Approved', 'N', 'Одобрени данни', 'Approved data');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ОПО', 'Approved');


