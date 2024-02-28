--liquibase formatted sql

--changeset mmurlev:248.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
values('patent.secret.data','Достъп до секретни данни на патент')

--changeset mmurlev:248.2
insert into ext_user.CF_GROUP_SECURITY_ROLE (GROUP_ID, ROLE_NAME, DATE_CREATED, USER_CREATED)
VALUES (1, 'patent.secret.data',getdate(), 4);

