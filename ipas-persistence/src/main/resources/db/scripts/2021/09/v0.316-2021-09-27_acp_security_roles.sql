--liquibase formatted sql

--changeset mmurlev:316.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('acp.object-identity-data', 'АНП: Преглед на панел "Идентификация"');

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES('acp.object-process-data','АНП: Преглед на панел "Работен процес"');

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES('acp.object-person-data','АНП: Преглед на панел "Лица и контакти"');

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('reception.creator.from.existing.document','Ново заявление по съществуващ документ в AbDocs');