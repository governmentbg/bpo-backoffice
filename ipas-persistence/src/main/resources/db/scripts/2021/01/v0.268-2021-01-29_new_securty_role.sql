--liquibase formatted sql

--changeset dveizov:264
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('process.register-old-userdocs','Работен процес: Регистриране на стари вторични действия');

