--liquibase formatted sql

--changeset mmurlev:269.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
values('ipas.admin.edit.person','Админ: Редактиране на лица')

--changeset mmurlev:269.2
insert into ext_user.CF_GROUP_SECURITY_ROLE (GROUP_ID, ROLE_NAME, DATE_CREATED, USER_CREATED)
VALUES (1, 'ipas.admin.edit.person',getdate(), 4);

