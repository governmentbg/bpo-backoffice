--liquibase formatted sql

--changeset mmurlev:317.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('acp.edit.own', 'АНП: Промяна на собствени производства');

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES('acp.edit.all','АНП: Промяна на всички производства (собствени и чужди)"');

