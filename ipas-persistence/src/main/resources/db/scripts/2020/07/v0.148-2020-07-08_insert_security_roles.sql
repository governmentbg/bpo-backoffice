--liquibase formatted sql

--changeset dveizov:148
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('court.decision.view', 'Нарушения: Преглед');

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('dispute.view', 'Спорове: Преглед');
