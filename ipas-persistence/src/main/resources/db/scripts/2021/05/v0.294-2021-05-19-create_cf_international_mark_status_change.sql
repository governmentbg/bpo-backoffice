--liquibase formatted sql

--changeset mmihova:294.1
CREATE TABLE EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (
  id integer identity(1,1) primary key,
  status_code varchar(4) not null,
  status_message varchar(350) not null unique
);

--changeset mmihova:294.2
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'приета');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'оттеглен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'приета/');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'приета/ограничение');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'приета/ частично заличаване от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'приета / част. заличена от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'приета/част. залич. от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'отказ/приета');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'приета със съд.решение');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'опозиция / приета');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'опозиция/ приета');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'предв. отказ/приета');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'отказ/ приета');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('158', 'окончателен частичен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'заличена СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'заличена от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'заличена');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'ок.п.отказ / заличена СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'приета/ заличена от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'ок.п. отказ / заличена СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'отказ / заличена от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'отказ? / заличена от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'приета/заличена от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'отказ / заличена СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'ок.п.отказ/заличена от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'оттеглен отказ/заличена СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'ок.ч.отказ / заличена СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'ок.ч. отказ / заличена СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'ок.част.отказ/заличена СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'ок.пълен отказ / заличена');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'приета / заличена от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'ок.част.отказ / заличена соис');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('891', 'отказ?/ заличена от СОИС');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'окончателен пълен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'окончателен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'окончат. пълен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'опозиция/ ок. пълен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'отказ  / окончат. пълен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'опозиция / пълен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'отказ/ окончателен пълен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'отказ/ оконч. пълен отк.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'отказ / окончат. пълен отказ');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('166', 'предв. отказ/ оконч. пълен отк.');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('1221', 'отказ от закрила');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('1221', 'отказ? / отказ от закрила');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('1221', 'отказ / отказ от закрила');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('1221', 'отказ от закрила за България');
INSERT INTO EXT_CORE.CF_INTERNATIONAL_MARK_STATUS_CHANGE (status_code, status_message) values ('1221', 'приета/отказ от закрила за България');