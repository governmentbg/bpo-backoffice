--liquibase formatted sql

--changeset mmurlev:278.1
update ext_Core.CF_LEGAL_GROUND_TYPES
set descr = 'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон:  по-ранно право върху марка регистрирана по национален ред;'
where id = 109

update ext_Core.CF_LEGAL_GROUND_TYPES
set descr = 'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон:  по-ранно право върху марка регистрирана по национален ред;'
where id = 110

update ext_Core.CF_LEGAL_GROUND_TYPES
set descr = 'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон: по-ранно право върху марка на Европейския съюз;'
where id = 111


update ext_Core.CF_LEGAL_GROUND_TYPES
set descr = 'Регистрацията се заличава по искане на лице с правен интерес, когато използването на дизайна може да бъде забранено на основание на: т. 2 по-ранно право на индустриална собственост на това лице, което се ползва от закрилата по друг закон: по-ранно право върху марка на Европейския съюз;'
where id = 112