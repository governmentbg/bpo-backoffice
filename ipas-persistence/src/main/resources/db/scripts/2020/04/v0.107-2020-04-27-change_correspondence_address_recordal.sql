INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN)
VALUES ('Change_correspondence_address', 'S', 'Промяна на адрес за кореспонденция', 'Change correspondence address');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL)
VALUES ('ИПАК', 'Change_correspondence_address');

INSERT INTO EXT_CORE.CF_USERDOC_PERSON_ROLE (ROLE, IND_TAKE_FROM_OWNER, NAME, NAME_EN)
VALUES ('OLD_CORRESPONDENCE_ADDRESS', null, 'Стар адрес за кореспонденция', 'Old correspondence address');

INSERT INTO EXT_CORE.CF_USERDOC_PERSON_ROLE (ROLE, IND_TAKE_FROM_OWNER, NAME, NAME_EN)
VALUES ('NEW_CORRESPONDENCE_ADDRESS', null, 'Нов адрес за кореспонденция', 'New correspondence address');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE (USERDOC_TYP, ROLE)
VALUES ('ИПАК', 'OLD_CORRESPONDENCE_ADDRESS');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE (USERDOC_TYP, ROLE)
VALUES ('ИПАК', 'NEW_CORRESPONDENCE_ADDRESS');

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION)
VALUES ('userdoc.userdoc-change-correspondence-address-data', 'Вторични действия: Преглед на панел "Промяна на адрес за кореспонденция"');


