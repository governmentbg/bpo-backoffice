INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('structure.edit','Промяна на структура');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('structure.view','Преглед на структура');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('structure.group.edit','Промяна на група');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('structure.group.view','Преглед на група');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('reception.creator','Създаване на новопостъпили заявки');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('reception.working.date.change','Смяна на работната дата при новопостъпили заявки');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('reception.entry.date.change','Смяна на датата на входиране');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('mark.view.all','mark.view.all');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('mark.view.own','mark.view.own');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('mark.edit.all','mark.edit.all');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('mark.edit.own','mark.edit.own');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('patent.view.all','patent.view.all');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('patent.view.own','patent.view.own');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('patent.edit.all','patent.edit.all');
INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('patent.edit.own','patent.edit.own');
DELETE FROM EXT_USER.CF_SECURITY_ROLES WHERE ROLE_NAME = 'mark.update';
DELETE FROM EXT_USER.CF_SECURITY_ROLES WHERE ROLE_NAME = 'mark.edit';


