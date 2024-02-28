--liquibase formatted sql

--changeset dveizov:135
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('process.change-responsible-user', 'Работен процес: Промяна на отговорен потребител');

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('process.change-manual-due-date', 'Работен процес: Промяна на срок');
