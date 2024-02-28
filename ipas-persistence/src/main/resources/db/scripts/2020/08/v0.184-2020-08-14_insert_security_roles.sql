--liquibase formatted sql

--changeset dveizov:177
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('msprocess.msprocess-main-data', 'Ръчен под-процес: Преглед на панел "Данни за ръчен под-процес"');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('msprocess.object-process-data', 'Ръчен под-процес: Преглед на панел "Работен процес"');

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('msprocess.edit.all', 'Ръчен под-процес: Промяна на всички ръчни под-процеси (собствени и чужди)');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('msprocess.edit.own', 'Ръчен под-процес: Промяна на собствени ръчни под-процеси');


