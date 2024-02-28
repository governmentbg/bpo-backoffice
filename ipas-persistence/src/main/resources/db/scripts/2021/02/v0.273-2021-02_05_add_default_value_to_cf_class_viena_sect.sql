--liquibase formatted sql

--changeset mmurlev:273.1
insert into ipasprod.CF_CLASS_VIENNA_SECT (row_version, vienna_category_code, vienna_division_code, vienna_section_code, vienna_section_name, xml_designer, vienna_edition_code)
select 1,VIENNA_CATEGORY_CODE,VIENNA_DIVISION_CODE,0,VIENNA_DIVISION_NAME,'<PARAMS></PARAMS>',0 from CF_CLASS_VIENNA_DIVIS

