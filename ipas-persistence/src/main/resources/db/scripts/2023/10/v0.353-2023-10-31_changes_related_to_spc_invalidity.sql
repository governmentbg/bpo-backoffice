--liquibase formatted sql

--changeset mmurlev:353.1
INSERT INTO EXT_CORE.CF_USERDOC_UI_PANEL (PANEL, IND_RECORDAL, NAME, NAME_EN) VALUES (N'SpcAnnulmentRequest', N'N', N'SPC - Искане за обявяване на недействителност', N'SPC invalidity');

--changeset mmurlev:353.2
INSERT INTO EXT_CORE.CF_USERDOC_TYPE_TO_UI_PANEL (USERDOC_TYP, PANEL) VALUES (N'SPCИОН', N'SpcAnnulmentRequest');

--changeset mmurlev:353.3
INSERT INTO IpasProd.EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code) VALUES (128, N'ABS', N'2019', N'Регламент 469/2009 Чл.15, т.1а ', N'Сертификатът е издаден в противоречие с разпоредбите на чл.3', null);
INSERT INTO IpasProd.EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code) VALUES (129, N'ABS', N'2019', N'Регламент 469/200 Чл.15, т.1б ', N'Основният патент е прекратен преди изтичане на законния му срок', null);
INSERT INTO IpasProd.EXT_CORE.CF_LEGAL_GROUND_TYPES (id, legal_ground_category, version, title, descr, legal_ground_code) VALUES (130, N'ABS', N'2019', N'Регламент 469/200 Чл.15, т.1в ', N'Основният патент е обявен за недействителен или ограничен', null);

--changeset mmurlev:353.4
insert into ext_Core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values(128,'SpcAnnulmentRequest');
insert into ext_Core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values(129,'SpcAnnulmentRequest');
insert into ext_Core.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL (legal_ground_type_id, panel) values(130,'SpcAnnulmentRequest');

--changeset mmurlev:353.5
update ext_Core.CF_USERDOC_UI_PANEL set name = 'Основания за недействителност на патент' where panel = 'PatentAnnulmentRequest'

