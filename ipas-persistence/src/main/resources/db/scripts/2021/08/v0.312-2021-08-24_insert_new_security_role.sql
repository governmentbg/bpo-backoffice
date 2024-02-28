--liquibase formatted sql

--changeset mmurlev:312.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.international-mark-receptions-list-zm-zmr', 'Новопостъпили входящи документи - МР и ЗМР');

