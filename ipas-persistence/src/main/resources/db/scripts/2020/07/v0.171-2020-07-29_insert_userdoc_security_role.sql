--liquibase formatted sql

--changeset mmurlev:171
    INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-reviewer-squad-data','Вторични действия: Преглед на панел "Състав"');