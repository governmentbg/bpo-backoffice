INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN)
VALUES ('Change_representative', 'S', 'Промяна на представител', 'Change representative');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL)
VALUES ('Плн', 'Change_representative');

INSERT INTO EXT_CORE.CF_USERDOC_PERSON_ROLE (ROLE, IND_TAKE_FROM_OWNER, NAME, NAME_EN)
VALUES ('OLD_REPRESENTATIVE', null, 'Стар представител', 'Old representative');

INSERT INTO EXT_CORE.CF_USERDOC_PERSON_ROLE (ROLE, IND_TAKE_FROM_OWNER, NAME, NAME_EN)
VALUES ('NEW_REPRESENTATIVE', null, 'Нов представител', 'New representative');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE (USERDOC_TYP, ROLE)
VALUES ('Плн', 'OLD_REPRESENTATIVE');

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_PERSON_ROLE (USERDOC_TYP, ROLE)
VALUES ('Плн', 'NEW_REPRESENTATIVE');

INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('userdoc.userdoc-change-representative-data','Вторични действия: Преглед на панел "Промяна на представител"');

