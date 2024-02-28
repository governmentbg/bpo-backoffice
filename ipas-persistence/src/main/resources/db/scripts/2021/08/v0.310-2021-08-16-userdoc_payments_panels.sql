--liquibase formatted sql

--changeset ggeorgiev:310.1

INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL)
select userdoc_typ, 'Payments'
from ipasprod.CF_USERDOC_TYPE;