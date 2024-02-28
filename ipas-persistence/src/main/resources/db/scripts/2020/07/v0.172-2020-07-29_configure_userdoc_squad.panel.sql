--liquibase formatted sql

--changeset mmurlev:172.1
insert into  ext_Core.CF_USERDOC_UI_PANEL(PANEL, IND_RECORDAL, NAME, NAME_EN)
values ('ReviewerSquad','N','Състав','Reviewer squad')

--changeset mmurlev:172.2
insert into  ext_Core.CF_USERDOC_TYPE_TO_UI_PANEL(USERDOC_TYP, PANEL)
values ('ОПО','ReviewerSquad')
