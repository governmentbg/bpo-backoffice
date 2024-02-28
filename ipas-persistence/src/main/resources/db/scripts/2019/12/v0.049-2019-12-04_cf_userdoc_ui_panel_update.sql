--liquibase formatted sql

--changeset dveizov:49.1
alter table EXT_CORE.CF_USERDOC_UI_PANEL
    add IND_RECORDAL character varying(1) default 'N'
;

alter table EXT_CORE.CF_USERDOC_UI_PANEL
    add NAME character varying(255)
;

alter table EXT_CORE.CF_USERDOC_UI_PANEL
    add NAME_EN character varying(255)
;
--changeset dveizov:49.2
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Вид вторично действие', NAME_EN = null WHERE PANEL = 'UserdocTypeData';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Данни за вторично действие', NAME_EN = null WHERE PANEL = 'UserdocMainData';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Работен процес', NAME_EN = null WHERE PANEL = 'Process';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'N', NAME = 'Лица и контакти', NAME_EN = null WHERE PANEL = 'Persons';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'S', NAME = 'Лицензия', NAME_EN = null WHERE PANEL = 'Licenses';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'S', NAME = 'Особен залог', NAME_EN = null WHERE PANEL = 'Pledge';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'S', NAME = 'Прехвърляне', NAME_EN = null WHERE PANEL = 'Transfer';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'S', NAME = 'Подновяване', NAME_EN = null WHERE PANEL = 'Renewal';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'S', NAME = 'Промяна на име/адрес', NAME_EN = null WHERE PANEL = 'Change';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'S', NAME = 'Отказ от право', NAME_EN = null WHERE PANEL = 'Withdrawal';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'S', NAME = 'Несъстоятелност', NAME_EN = null WHERE PANEL = 'Bankruptcy';
UPDATE EXT_CORE.CF_USERDOC_UI_PANEL SET IND_RECORDAL = 'S', NAME = 'Обезпечение/запор', NAME_EN = null WHERE PANEL = 'Security_measure';


