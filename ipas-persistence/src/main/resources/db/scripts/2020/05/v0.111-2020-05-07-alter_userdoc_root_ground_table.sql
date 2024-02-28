alter table ext_core.IP_USERDOC_ROOT_GROUNDS add CATEGORY_CODE VARCHAR(5);
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add constraint CATEGORY_CODE_FK
foreign key (CATEGORY_CODE) references ext_core.CF_LEGAL_GROUND_CATEGORIES

