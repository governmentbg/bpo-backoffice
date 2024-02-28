--liquibase formatted sql

--changeset mmurlev:188.1
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Начална страница:  Панел Изчакване на срокове по входящи документи' WHERE ROLE_NAME = 'ipobjects.userdoc-waiting-terms'

--changeset mmurlev:188.2
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Начална страница:  Панел Изтекли срокове по входящи документи' WHERE ROLE_NAME = 'ipobjects.userdoc-expired-terms'
