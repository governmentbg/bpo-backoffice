--liquibase formatted sql

--changeset mmurlev:268.1
alter table ext_core.CF_ATTACHMENT_TYPE add ATTACHMENT_NAME_SUFFIX VARCHAR(10)

--changeset mmurlev:268.2
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'A1' where id = 4
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'B1' where id = 5
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'B2' where id = 6
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'U1' where id = 7
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'U2' where id = 8
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'T1' where id = 9
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'T3' where id = 10
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'T4' where id = 11
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'T5' where id = 12
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'T6' where id = 13
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'B8' where id = 14
update  ext_core.CF_ATTACHMENT_TYPE set  ATTACHMENT_NAME_SUFFIX = 'U8' where id = 15

--changeset mmurlev:268.3
update  at
set at.ATTACHMENT_NAME = concat('BG',COALESCE(p.REGISTRATION_NBR,p.FILE_NBR),t.ATTACHMENT_NAME_SUFFIX,'_',at.ID,'.pdf')
from EXT_CORE.IP_PATENT_ATTACHMENT at
INNER JOIN IP_PATENT p on p.FILE_NBR = at.FILE_NBR and p.FILE_SER = at.FILE_SER and p.FILE_TYP = at.FILE_TYP and p.FILE_SEQ = at.FILE_SEQ
INNER JOIN EXT_CORE.CF_ATTACHMENT_TYPE t on at.ATTACHMENT_TYPE_ID = t.id
where t.ATTACHMENT_NAME_SUFFIX is not null
