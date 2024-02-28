--liquibase formatted sql

--changeset mmurlev:354.1
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES (N'userdoc.userdoc-spc-annulment-request-data', N'Вторични действия: Преглед на панел "SPC - Искане за обявяване на недействителност"');

--changeset mmurlev:354.2
update EXT_CORE.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'spc.invalidityGround.469.15.1a' where id = 128;
update EXT_CORE.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'spc.invalidityGround.469.15.1b' where id = 129;
update EXT_CORE.CF_LEGAL_GROUND_TYPES set legal_ground_code = 'spc.invalidityGround.469.15.1v' where id = 130;

