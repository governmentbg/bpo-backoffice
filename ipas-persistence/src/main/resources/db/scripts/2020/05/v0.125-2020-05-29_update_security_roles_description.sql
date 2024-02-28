UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Работна дата: Промяна' WHERE ROLE_NAME = 'ipas.change.working.date';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Начална страница: Достъп' WHERE ROLE_NAME = 'ipas.home.page';

UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Промяна на собствени марки' WHERE ROLE_NAME = 'mark.edit.own';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Промяна на всички марки (собствени и чужди)' WHERE ROLE_NAME = 'mark.edit.all';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на всички марки (собствени и чужди)' WHERE ROLE_NAME = 'mark.view.all';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на собствени марки' WHERE ROLE_NAME = 'mark.view.own';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на панел "Предходни права"' WHERE ROLE_NAME = 'mark.mark-claims-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на панел "Данни за марката"' WHERE ROLE_NAME = 'mark.mark-main-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на панел "Стоки и услуги"' WHERE ROLE_NAME = 'mark.mark-nice-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на панел "Публикации"' WHERE ROLE_NAME = 'mark.mark-publication-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на панел "Идентификация"' WHERE ROLE_NAME = 'mark.object-identity-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на панел "Плащания"' WHERE ROLE_NAME = 'mark.object-payments-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на панел "Лица и контакти"' WHERE ROLE_NAME = 'mark.object-person-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Марки: Преглед на панел "Работен процес"' WHERE ROLE_NAME = 'mark.object-process-data';

UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Промяна на всички патенти (собствени и чужди)' WHERE ROLE_NAME = 'patent.edit.all';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Промяна на собствени патенти' WHERE ROLE_NAME = 'patent.edit.own';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на всички патенти (собствени и чужди)' WHERE ROLE_NAME = 'patent.view.all';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на собствени патенти' WHERE ROLE_NAME = 'patent.view.own';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на панел "Идентификация"' WHERE ROLE_NAME = 'patent.object-identity-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на панел "Плащания"' WHERE ROLE_NAME = 'patent.object-payments-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на панел "Лица и контакти"' WHERE ROLE_NAME = 'patent.object-person-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на панел "Работен процес"' WHERE ROLE_NAME = 'patent.object-process-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на панел "Класификация по МПК"' WHERE ROLE_NAME = 'patent.patent-ipc-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на панел "Данни за патента"' WHERE ROLE_NAME = 'patent.patent-main-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на панел "Публикации"' WHERE ROLE_NAME = 'patent.patent-publication-data';
UPDATE EXT_USER.CF_SECURITY_ROLES SET DESCRIPTION = 'Патенти: Преглед на панел "Предходни права"' WHERE ROLE_NAME = 'patent.patent-rights-data';

DELETE FROM EXT_USER.CF_GROUP_SECURITY_ROLE where ROLE_NAME = 'mark.mark-vienna-data'
DELETE FROM EXT_USER.CF_SECURITY_ROLES where ROLE_NAME = 'mark.mark-vienna-data'
DELETE FROM EXT_USER.CF_GROUP_SECURITY_ROLE where ROLE_NAME = 'reception.working.date.change'
DELETE FROM EXT_USER.CF_SECURITY_ROLES where ROLE_NAME = 'reception.working.date.change'



