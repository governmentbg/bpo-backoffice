DISABLE TRIGGER [IPASPROD].[PATENT_INDEX_AFTER]  ON [IPASPROD].[IP_PATENT];
DISABLE TRIGGER [IPASPROD].[FILE_INDEX_AFTER]  ON [IPASPROD].[IP_FILE];

update ipasprod.ip_patent set PCT_PHASE = null, PCT_APPL_ID = null, PCT_PUBL_COUNTRY = null, PCT_PUBL_ID = null, PCT_PUBL_TYP = null
where PCT_PHASE is not null
  and (PCT_APPL_ID is null or PCT_APPL_ID = '')
  and (PCT_APPL_DATE is null or PCT_APPL_DATE = '')
  and (PCT_PUBL_COUNTRY is null or PCT_PUBL_COUNTRY = '')
  and (PCT_PUBL_ID is null or PCT_PUBL_ID = '')
  and (PCT_PUBL_TYP is null or PCT_PUBL_TYP = '')
  and (PCT_PUBL_DATE is null or PCT_PUBL_DATE = '');

ENABLE TRIGGER [IPASPROD].[PATENT_INDEX_AFTER]  ON [IPASPROD].[IP_PATENT];
ENABLE TRIGGER [IPASPROD].[FILE_INDEX_AFTER]  ON [IPASPROD].[IP_FILE];
