--liquibase formatted sql

--changeset mmurlev:169
    INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-court-appeals-data','Вторични действия: Преглед на панел "Жалби до съда"');