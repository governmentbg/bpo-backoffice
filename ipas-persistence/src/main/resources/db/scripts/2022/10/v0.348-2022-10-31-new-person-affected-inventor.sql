--liquibase formatted sql

--changeset akehayov:348.1
INSERT INTO IpasProd.EXT_CORE.CF_USERDOC_PERSON_ROLE (ROLE, IND_TAKE_FROM_OWNER, NAME, NAME_EN, IND_TAKE_FROM_REPRESENTATIVE, IND_ADDITIONAL_OFFIDOC_CORRESPONDENT) VALUES ('AFFECTED_INVENTOR', null, 'Изобретатели, субекти на искането', 'Inventors, subjects of the reques', null, null);
INSERT INTO IpasProd.EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE (USERDOC_TYP, ROLE) VALUES ('ОИПП', 'AFFECTED_INVENTOR');