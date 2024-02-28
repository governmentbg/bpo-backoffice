--liquibase formatted sql

--changeset mnakova:355
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES (N'patent.citations-data', N'Патенти: Преглед на панел "Цитирания"');