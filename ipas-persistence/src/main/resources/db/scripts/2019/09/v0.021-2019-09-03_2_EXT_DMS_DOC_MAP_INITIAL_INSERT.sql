INSERT INTO IPASPROD.EXT_DMS_DOC_MAP
SELECT OFFIDOC_ORI
     , OFFIDOC_SER
     , OFFIDOC_NBR
     , ACSTRE_DOCUMENT_ID
     , PARENT_DOCUMENT_ID
     , null
     , null
FROM [IPASPROD].[EXT_ACSTRE_DOCUMENTS] d1
where offidoc_nbr is not null
  and flag <> 'D'
  and DATE_TRANSFERRED = (select max(date_transferred)
                          from [IPASPROD].[EXT_ACSTRE_DOCUMENTS] d2
                          where d1.OFFIDOC_ORI = d2.OFFIDOC_ORI
                            and d1.OFFIDOC_SER =
                                d2.OFFIDOC_SER
                            and d1.OFFIDOC_NBR = d2.OFFIDOC_NBR)
group by OFFIDOC_ORI
       , OFFIDOC_SER
       , OFFIDOC_NBR
       , ACSTRE_DOCUMENT_ID
       , PARENT_DOCUMENT_ID;


