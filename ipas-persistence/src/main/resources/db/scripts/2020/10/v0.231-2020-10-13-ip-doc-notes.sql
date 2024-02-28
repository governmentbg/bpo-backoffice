--liquibase formatted sql

--changeset ggeorgiev:231
alter table ipasprod.IP_DOC ALTER column NOTES varchar(4000);


ALTER TABLE IPASPROD.IP_DOC DISABLE TRIGGER IP_DOC_INDEX_AFTER;
update d
set d.notes = ud.notes
from ipasprod.ip_doc d
join ipasprod.ip_userdoc ud on d.DOC_NBR = ud.DOC_NBR and d.DOC_ORI = ud.DOC_ORI and d.DOC_LOG = ud.DOC_LOG and d.DOC_SER = ud.DOC_SER;

update d
set d.notes = cast ((cast(ud.notes as varchar) + '\n' + cast(ud.notes2 as varchar)) as varchar(3000))
from ipasprod.ip_doc d
join EXT_RECEPTION.RECEPTION_USERDOC_REQUEST ud on d.DOC_NBR = ud.DOC_NBR and d.DOC_ORI = ud.DOC_ORI and d.DOC_LOG = ud.DOC_LOG and d.DOC_SER = ud.DOC_SER
where ud.notes is not null and ud.notes2 is not null and cast(ud.notes as varchar) != '' and cast(ud.notes2 as varchar) != '';

update d
set d.notes = m.notes
from ipasprod.ip_doc d
join ipasprod.ip_mark m on m.FILE_SEQ = d.FILE_SEQ  and m.FILE_TYP = d.FILE_TYP and m.FILE_SER = d.FILE_SER and m.FILE_NBR = d.FILE_NBR
where m.notes is not null and cast (m.notes as varchar) != '';

update d
set d.notes = m.notes
from ipasprod.ip_doc d
join ipasprod.ip_patent_notes m on m.FILE_SEQ = d.FILE_SEQ  and m.FILE_TYP = d.FILE_TYP and m.FILE_SER = d.FILE_SER and m.FILE_NBR = d.FILE_NBR
where m.notes is not null and cast (m.notes as varchar) != '';

ALTER TABLE IPASPROD.IP_DOC ENABLE TRIGGER IP_DOC_INDEX_AFTER;

alter table EXT_RECEPTION.RECEPTION_USERDOC_REQUEST drop column notes2;