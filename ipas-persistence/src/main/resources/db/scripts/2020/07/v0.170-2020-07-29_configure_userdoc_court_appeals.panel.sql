--liquibase formatted sql

--changeset mmurlev:170.1
insert into  ext_Core.CF_USERDOC_UI_PANEL(PANEL, IND_RECORDAL, NAME, NAME_EN)
values ('CourtAppeals','N','Жалби до съда','Court appeals')

--changeset mmurlev:170.2
insert into  ext_Core.CF_USERDOC_TYPE_TO_UI_PANEL(USERDOC_TYP, PANEL)
values ('ОАА','CourtAppeals'),
('ЖО','CourtAppeals'),
('ЖПП','CourtAppeals'),
('ЖРО','CourtAppeals')
