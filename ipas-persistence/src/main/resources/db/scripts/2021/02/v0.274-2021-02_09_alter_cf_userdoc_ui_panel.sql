--liquibase formatted sql

--changeset mmurlev:274.1
update ext_core.CF_USERDOC_UI_PANEL set name = 'Основания за заличаване ТМ' where panel  ='Invalidity'
update ext_core.CF_USERDOC_UI_PANEL set name = 'Основания за отмяна ТМ' where panel  ='Revocation'
update ext_core.CF_USERDOC_UI_PANEL set name = 'Основания за недействителност' where panel  ='PatentAnnulmentRequest'
update ext_core.CF_USERDOC_UI_PANEL set name = 'Основания за заличаване ПД' where panel  ='DesignAnnulmentRequest'