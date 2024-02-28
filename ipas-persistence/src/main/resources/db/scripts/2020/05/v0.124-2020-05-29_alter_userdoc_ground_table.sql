

alter table ext_core.IP_USERDOC_ROOT_GROUNDS add MARK_SIGN_TYPE VARCHAR(10);
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add GI_APPL_TYP VARCHAR(3);
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add GI_APPL_SUBTYP VARCHAR(2);
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add constraint MARK_SIGN_TYPE_FK
foreign key (MARK_SIGN_TYPE) references EXT_CF_SIGN_TYPE
alter table ext_core.IP_USERDOC_ROOT_GROUNDS add constraint GI_APPL_SUBTYP_FK
foreign key (GI_APPL_TYP,GI_APPL_SUBTYP) references CF_APPLICATION_SUBTYPE


