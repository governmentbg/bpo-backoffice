--liquibase formatted sql

--changeset ggeorgiev:312
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('ipobjects.not-linked-payments-list', 'Начална страница: Необвързани плащания');


insert into ext_user.CF_GROUP_SECURITY_ROLE (GROUP_ID, ROLE_NAME, DATE_CREATED, USER_CREATED)
VALUES (1, 'ipobjects.not-linked-payments-list',getdate(), 4);