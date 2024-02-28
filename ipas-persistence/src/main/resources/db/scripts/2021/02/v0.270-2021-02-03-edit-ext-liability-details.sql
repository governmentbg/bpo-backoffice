--liquibase formatted sql

--changeset mmurlev:269.1
alter table IPASPROD.ext_liability_details
	add PROCESSED BIT NOT NULL DEFAULT 0



