--liquibase formatted sql

--changeset dveizov:149
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Начална страница: Достъп до началната страница' where role_name='ipas.home.page';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Начална страница: Панел изчакване на срок' where role_name='ipobjects.waiting-term-list';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Начална страница: Панел новопостъпили заявки' where role_name='ipobjects.reception-list';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Начална страница: Панел новопостъпили вторични действия' where role_name='userdoc.view-last-added';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Начална страница: Панел последно извършени действия' where role_name='ipobjects.last-actions-list';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Начална страница: Достъп до чужди обекти в панелите' where role_name='ipobjects.search-foreign-objects-home-page';

update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Вторични действия: Преглед на собствени вторични действия' where role_name='userdoc.view.own';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Вторични действия: Преглед на всички вторични действия (собствени и чужди)' where role_name='userdoc.view.all';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Вторични действия: Промяна на собствени вторични действия' where role_name='userdoc.edit.own';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Вторични действия: Промяна на всички вторични действия' where role_name='userdoc.edit.all';

update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Офис документи: Преглед на собствени офис документи' where role_name='offidoc.view.own';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Офис документи: Преглед на всички офис документи (собствени и чужди)' where role_name='offidoc.view.all';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Офис документи: Промяна на собствени офис документи' where role_name='offidoc.edit.own';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Офис документи: Промяна на всички офис документи' where role_name='offidoc.edit.all';

update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Работен процес: Изпълняване на автоматични действия' where role_name='process.automatic.action.execute';

update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Админ: Потребители и права' where role_name='users.roles.view';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Админ: Преглед на групи' where role_name='structure.group.view';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Админ: Преглед на структура' where role_name='structure.view';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Админ: Промяна на група' where role_name='structure.group.edit';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Админ: Промяна на структура' where role_name='structure.edit';

update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Регистратура: Създаване на новопостъпили заявки' where role_name='reception.creator';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Регистратура: Смяна на датата на входиране' where role_name='reception.entry.date.change';
update EXT_USER.CF_SECURITY_ROLES set DESCRIPTION='Регистратура: Промяна на системната работна дата' where role_name='ipas.change.working.date';
