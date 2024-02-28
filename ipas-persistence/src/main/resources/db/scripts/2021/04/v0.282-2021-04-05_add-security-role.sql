--liquibase formatted sql

--changeset mmurlev:282.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('userdoc.view-newly-allocated-userdocs', 'Вторични действия: Преглед на панел "Новоразпределени входящи документи"');
