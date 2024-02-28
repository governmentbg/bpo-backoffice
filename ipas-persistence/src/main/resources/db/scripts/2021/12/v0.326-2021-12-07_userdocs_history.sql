--liquibase formatted sql

--changeset ggeorgiev:326.1
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN)
VALUES ('History', 'N', 'История', 'History');

--changeset ggeorgiev:326.2
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL)
select userdoc_typ, 'History'
from ipasprod.CF_USERDOC_TYPE;

