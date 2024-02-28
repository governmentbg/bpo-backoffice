--liquibase formatted sql

--changeset mmurlev:325.1
create table EXT_CORE.CF_ACP_ADMINISTRATIVE_PENALTY_TYPE
(
	ID int not null,
	DESCRIPTION text,
constraint CF_ACP_ADMINISTRATIVE_PENALTY_TYPE_PK
		primary key nonclustered (id)
)


--changeset mmurlev:325.2
insert into ext_Core.CF_ACP_ADMINISTRATIVE_PENALTY_TYPE (ID, DESCRIPTION)
values(1,'Глоба')

insert into ext_Core.CF_ACP_ADMINISTRATIVE_PENALTY_TYPE (ID, DESCRIPTION)
values(2,'Имуществена санкция')

insert into ext_Core.CF_ACP_ADMINISTRATIVE_PENALTY_TYPE (ID, DESCRIPTION)
values(3,'Маловажен случай')

insert into ext_Core.CF_ACP_ADMINISTRATIVE_PENALTY_TYPE (ID, DESCRIPTION)
values(4,'Други')



--changeset mmurlev:325.3
create table EXT_CORE.CF_ACP_ADMINISTRATIVE_PENALTY_PAYMENT_STATUS
(
	ID int not null,
	DESCRIPTION text,
constraint CF_ACP_ADMINISTRATIVE_PENALTY_PAYMENT_STATUS_PK
		primary key nonclustered (id)
)

--changeset mmurlev:325.4
INSERT INTO EXT_CORE.CF_ACP_ADMINISTRATIVE_PENALTY_PAYMENT_STATUS (ID, DESCRIPTION)
VALUES (1,'Платено')

INSERT INTO EXT_CORE.CF_ACP_ADMINISTRATIVE_PENALTY_PAYMENT_STATUS (ID, DESCRIPTION)
VALUES (2,'Частично платено')

INSERT INTO EXT_CORE.CF_ACP_ADMINISTRATIVE_PENALTY_PAYMENT_STATUS (ID, DESCRIPTION)
VALUES (3,'Неплатено')


--changeset mmurlev:325.5
create table EXT_CORE.ACP_ADMINISTRATIVE_PENALTY
(
    FILE_SEQ varchar(2)  not null,
    FILE_TYP varchar(1)  not null,
    FILE_SER numeric(4)  not null,
    FILE_NBR numeric(10) not null,
    ADMINISTRATIVE_PENALTY_TYPE_ID int,
    PAYMENTS_STATUS_ID int,
    AMOUNT NUMERIC(10,2),
    PARTIALLY_PAID_AMOUNT NUMERIC(10,2),
    NOTIFICATION_DATE datetime,
    OTHER_TYPE_DESCRIPTION text,
    constraint ACP_ADMINISTRATIVE_PENALTY_PK
        primary key nonclustered (FILE_SEQ, FILE_TYP, FILE_SER, FILE_NBR)
)

--changeset mmurlev:325.6
ALTER TABLE  EXT_CORE.ACP_ADMINISTRATIVE_PENALTY
WITH CHECK ADD  CONSTRAINT [ACP_ADMINISTRATIVE_PENALTY_TYPE_FK] FOREIGN KEY(ADMINISTRATIVE_PENALTY_TYPE_ID)
REFERENCES EXT_CORE.CF_ACP_ADMINISTRATIVE_PENALTY_TYPE (id)

--changeset mmurlev:325.7
ALTER TABLE  EXT_CORE.ACP_ADMINISTRATIVE_PENALTY
WITH CHECK ADD  CONSTRAINT [PAYMENT_STATUS_ID_FK] FOREIGN KEY(PAYMENTS_STATUS_ID)
REFERENCES EXT_CORE.CF_ACP_ADMINISTRATIVE_PENALTY_PAYMENT_STATUS (id)


--changeset mmurlev:325.8
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('acp.acp-administrative-penalty-data','АНП: Преглед на панел "Административни наказания"');