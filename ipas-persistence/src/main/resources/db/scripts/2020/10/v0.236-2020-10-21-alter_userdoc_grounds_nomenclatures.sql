--liquibase formatted sql

--changeset mmurlev:236.1
update ext_core.CF_GROUND_MARK_TYPE set name = 'Марка на ЕС' where id = 3

--changeset mmurlev:236.2
update ext_core.CF_LEGAL_GROUND_TYPES set
descr = 'марка на ЕС с по-ранна дата на подаване на заявката или с по-ранен приоритет, или с по-ранно старшинство за територията на Република България, признато по реда на Регламент (ЕО) № 207/2009 на Съвета от 26 февруари 2009 г. относно марката на Общността (ОВ, L 78/1 от 24 март 2009 г.), наричан по-нататък "Регламент (ЕО) № 207/2009";'
where id = 68

--changeset mmurlev:236.3
update ext_core.CF_LEGAL_GROUND_TYPES set
descr = 'заявена марка на ЕС с по-ранна дата на подаване на заявката или с по-ранен приоритет, или с по-ранно старшинство за територията на Република България, признато по реда на Регламент (ЕО) № 207/2009, ако бъде регистрирана по реда на този регламент;'
where id = 69
