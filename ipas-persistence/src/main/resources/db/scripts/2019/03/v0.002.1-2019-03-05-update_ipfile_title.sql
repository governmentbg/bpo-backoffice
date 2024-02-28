alter table ipasprod.ip_file alter column title nvarchar(4000);
alter table ipasprod.ip_file alter column title_lang2 nvarchar(4000);

alter table ipasprod.ip_name alter column mark_name nvarchar(4000);
alter table ipasprod.ip_name alter column mark_name_lang2 nvarchar(4000);

alter table ipasprod.ip_patent alter column title nvarchar(4000) not null;
alter table ipasprod.ip_patent alter column english_title nvarchar(4000);


update ip_file
set ip_file.title = p.title
from ip_file f
join ip_patent p on p.file_nbr = f.file_nbr and p.file_seq = f.file_Seq and p.file_typ = f.file_typ and p.file_ser = f.file_ser;


update ip_file
set ip_file.TITLE_LANG2 = p.ENGLISH_TITLE
from ip_file f
join ip_patent p on p.file_nbr = f.file_nbr and p.file_seq = f.file_Seq and p.file_typ = f.file_typ and p.file_ser = f.file_ser;
	   
	   
update ip_file
set ip_file.TITLE_LANG2 = n.MARK_NAME_LANG2
from ip_file f
join ip_mark m on m.file_nbr = f.file_nbr and m.file_seq = f.file_Seq and m.file_typ = f.file_typ and m.file_ser = f.file_ser
join IP_NAME n on m.MARK_CODE = n.MARK_CODE;


update ip_file
set ip_file.TITLE = n.MARK_NAME
from ip_file f
join ip_mark m on m.file_nbr = f.file_nbr and m.file_seq = f.file_Seq and m.file_typ = f.file_typ and m.file_ser = f.file_ser
join IP_NAME n on m.MARK_CODE = n.MARK_CODE;



DROP TRIGGER [IPASPROD].[SOLICITUD_UPDATE_AFTER_INSERT];
DROP TRIGGER [IPASPROD].[SOLICITUD_UPDATE_AFTER_UPDATE];


