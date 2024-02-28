--liquibase formatted sql

--changeset dveizov:133
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('process.execute-actions-for-foreign-object', 'Работен процес: Изпълняване на действия на чужд обект');
