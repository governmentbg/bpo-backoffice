INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL) VALUES ('Claim');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ЖО', 'Claim');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ЖПП', 'Claim');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ЖРО', 'Claim');
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES ('ОАА', 'Claim');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL) VALUES ('Opposition');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL) VALUES ('Invalidity');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL) VALUES ('Revocation');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL) VALUES ('Goods');
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL) VALUES ('Objection');

UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Жалба', NAME_EN =  'Appeal' WHERE PANEL = 'Claim';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Основания за опозиция', NAME_EN = 'Opposition' WHERE PANEL = 'Opposition';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Основания за заличаване', NAME_EN = 'Invalidity claim' WHERE PANEL = 'Invalidity';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Основания за отмяна', NAME_EN = 'Revocation claim' WHERE PANEL = 'Revocation';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Засегнати стоки и услуги', NAME_EN = 'Affected goods and services' WHERE PANEL = 'Goods';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Основания за възражение', NAME_EN = 'Objection' WHERE PANEL = 'Objection';

