--liquibase formatted sql

--changeset mmurlev:297.1
CREATE TABLE EXT_CORE.IP_OFFIDOC_PUBLISHED_DECISION
(
    OFFIDOC_ORI varchar(4) not null,
	OFFIDOC_SER numeric(4) not null,
	OFFIDOC_NBR numeric(7) not null,
    ATTACHMENT_CONTENT varbinary(max),
    ATTACHMENT_NAME VARCHAR(255)
	constraint IP_OFFIDOC_PUBLISHED_DECISION_PK
		primary key (OFFIDOC_ORI, OFFIDOC_SER, OFFIDOC_NBR),
)

--changeset mmurlev:297.2
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('offidoc.offidoc-published-decision','Офис документи: Преглед на панел "Решение за публикуване"');


