--liquibase formatted sql

--changeset ggeorgiev:189.1
  IF EXISTS (SELECT *
  FROM sys.foreign_keys
   WHERE object_id = OBJECT_ID(N'IP_FILE_OWNER_CHANGES_IP_FILE_FK')
   AND parent_object_id = OBJECT_ID(N'ipasprod.IP_FILE_OWNER_CHANGES')
)
  ALTER TABLE IPASPROD.IP_FILE_OWNER_CHANGES DROP CONSTRAINT [IP_FILE_OWNER_CHANGES_IP_FILE_FK]




--changeset ggeorgiev:189.2
  IF NOT EXISTS (SELECT *
  FROM sys.foreign_keys
   WHERE object_id = OBJECT_ID(N'IP_FILE_OWNER_CHNG_IP_FILE_FK')
   AND parent_object_id = OBJECT_ID(N'ipasprod.IP_FILE_OWNER_CHANGES')
)
  alter table IPASPROD.IP_FILE_OWNER_CHANGES
add constraint IP_FILE_OWNER_CHNG_IP_FILE_FK
foreign key (FILE_NBR, FILE_SEQ, FILE_TYP, FILE_SER) references IPASPROD.IP_FILE
on update cascade on delete cascade;
