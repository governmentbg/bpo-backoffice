--liquibase formatted sql

--changeset dveizov:220
CREATE TABLE EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT
(
    OFFIDOC_ORI                varchar(4) NOT NULL,
    OFFIDOC_SER                numeric(4) NOT NULL,
    OFFIDOC_NBR                numeric(7) NOT NULL,
    ACSTRE_DOCUMENT_ID         varchar(255),
    ABDOCS_DOCUMENT_ID         numeric(10),
    REGISTRATION_NUMBER        varchar(255),
    CLOSEST_MAIN_PARENT_OBJECT_ID      varchar(255),
    CLOSEST_MAIN_PARENT_OBJECT_TYPE    varchar(255),
    DIRECT_PARENT_OBJECT_ID    varchar(255),
    DIRECT_PARENT_OBJECT_TYPE  varchar(255),
    NOTIFICATION_FINISHED_DATE datetime
        CONSTRAINT PK_IP_OFFIDOC_ABDOCS_DOCUMENT PRIMARY KEY (OFFIDOC_ORI, OFFIDOC_SER, OFFIDOC_NBR)
);

alter table EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT
	add constraint EXT_CORE_IP_OFFIDOC_ABDOCS_DOCUMENT_IP_OFFIDOC_OFFIDOC_fk
		foreign key (OFFIDOC_ORI, OFFIDOC_SER, OFFIDOC_NBR) references IPASPROD.IP_OFFIDOC
			on delete cascade ;

INSERT INTO EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT
SELECT d1.OFFIDOC_ORI,
       d1.OFFIDOC_SER,
       d1.OFFIDOC_NBR,
       d1.ACSTRE_DOCUMENT_ID,
       null,
       null,
       (CASE
            WHEN pt.RELATED_TO_WCODE = 1 AND p.FILE_SEQ IS NOT NULL AND p.FILE_TYP IS NOT NULL AND p.FILE_SER IS NOT NULL AND p.FILE_NBR IS NOT NULL
                THEN CONCAT(p.FILE_SEQ, '/', p.FILE_TYP, '/', cast(p.FILE_SER as VARCHAR), '/', cast(p.FILE_NBR as VARCHAR))
            WHEN pt.RELATED_TO_WCODE = 2 AND p.DOC_ORI IS NOT NULL AND p.DOC_LOG IS NOT NULL AND p.DOC_SER IS NOT NULL AND p.DOC_NBR IS NOT NULL
                THEN CONCAT(p.DOC_ORI, '/', p.DOC_LOG, '/', cast(p.DOC_SER as VARCHAR), '/', cast(p.DOC_NBR as VARCHAR))
            WHEN pt.RELATED_TO_WCODE = 3 OR pt.RELATED_TO_WCODE = 4
                THEN CASE
                         WHEN up.FILE_SEQ IS NOT NULL AND up.FILE_TYP IS NOT NULL AND up.FILE_SER IS NOT NULL AND up.FILE_NBR IS NOT NULL
                             THEN CONCAT(up.FILE_SEQ, '/', up.FILE_TYP, '/', cast(up.FILE_SER as VARCHAR), '/', cast(up.FILE_NBR as VARCHAR))
                         WHEN up.DOC_ORI IS NOT NULL AND up.DOC_LOG IS NOT NULL AND up.DOC_SER IS NOT NULL AND up.DOC_NBR IS NOT NULL
                             THEN CONCAT(up.DOC_ORI, '/', up.DOC_LOG, '/', cast(up.DOC_SER as VARCHAR), '/', cast(up.DOC_NBR as VARCHAR))
                         ELSE NULL
                END
           END),
       (CASE
            WHEN pt.RELATED_TO_WCODE = 1 THEN 'IPOBJECT'
            WHEN pt.RELATED_TO_WCODE = 2 THEN 'USERDOC'
            WHEN pt.RELATED_TO_WCODE = 3 OR pt.RELATED_TO_WCODE = 4
               THEN CASE
                         WHEN up.FILE_SEQ IS NOT NULL AND up.FILE_TYP IS NOT NULL AND up.FILE_SER IS NOT NULL AND up.FILE_NBR IS NOT NULL
                             THEN 'IPOBJECT'
                         WHEN up.DOC_ORI IS NOT NULL AND up.DOC_LOG IS NOT NULL AND up.DOC_SER IS NOT NULL AND up.DOC_NBR IS NOT NULL
                             THEN 'USERDOC'
                         ELSE NULL
                END
           END),
        (CASE
            WHEN pt.RELATED_TO_WCODE = 1 AND p.FILE_SEQ IS NOT NULL AND p.FILE_TYP IS NOT NULL AND p.FILE_SER IS NOT NULL AND p.FILE_NBR IS NOT NULL
                THEN CONCAT(p.FILE_SEQ, '/', p.FILE_TYP, '/', cast(p.FILE_SER as VARCHAR), '/', cast(p.FILE_NBR as VARCHAR))
            WHEN pt.RELATED_TO_WCODE = 2 AND p.DOC_ORI IS NOT NULL AND p.DOC_LOG IS NOT NULL AND p.DOC_SER IS NOT NULL AND p.DOC_NBR IS NOT NULL
                THEN CONCAT(p.DOC_ORI, '/', p.DOC_LOG, '/', cast(p.DOC_SER as VARCHAR), '/', cast(p.DOC_NBR as VARCHAR))
            WHEN pt.RELATED_TO_WCODE = 3 AND p.OFFIDOC_ORI IS NOT NULL AND p.OFFIDOC_SER IS NOT NULL AND p.OFFIDOC_NBR IS NOT NULL
                THEN CONCAT(p.OFFIDOC_ORI, '/', cast(p.OFFIDOC_SER as VARCHAR), '/', cast(p.OFFIDOC_NBR as VARCHAR))
            WHEN pt.RELATED_TO_WCODE = 4 THEN NULL
           END),
        (CASE
            WHEN pt.RELATED_TO_WCODE = 1 THEN 'IPOBJECT'
            WHEN pt.RELATED_TO_WCODE = 2 THEN 'USERDOC'
            WHEN pt.RELATED_TO_WCODE = 3 THEN 'OFFIDOC'
            WHEN pt.RELATED_TO_WCODE = 4 THEN 'MANUAL_SUB_PROCESS'
           END),
       d1.DATE_TRANSFERRED
FROM [IPASPROD].[EXT_ACSTRE_DOCUMENTS] d1
         JOIN IPASPROD.IP_OFFIDOC o
              ON d1.OFFIDOC_ORI = o.OFFIDOC_ORI AND d1.OFFIDOC_SER = o.OFFIDOC_SER AND d1.OFFIDOC_NBR = o.OFFIDOC_NBR
         JOIN IPASPROD.IP_PROC p on o.PROC_NBR = p.PROC_NBR and o.PROC_TYP = p.PROC_TYP
         JOIN IPASPROD.CF_PROCESS_TYPE pt on p.PROC_TYP = pt.PROC_TYP AND pt.RELATED_TO_WCODE is not null
         LEFT JOIN IPASPROD.IP_PROC up on p.UPPER_PROC_TYP = up.PROC_TYP AND p.UPPER_PROC_NBR = up.PROC_NBR
where d1.offidoc_nbr is not null
  and d1.FLAG <> 'D'
  and d1.DATE_TRANSFERRED = (select max(date_transferred)
                             from [IPASPROD].[EXT_ACSTRE_DOCUMENTS] d2
                             where d1.OFFIDOC_ORI = d2.OFFIDOC_ORI
                               and d1.OFFIDOC_SER = d2.OFFIDOC_SER
                               and d1.OFFIDOC_NBR = d2.OFFIDOC_NBR)
;

DROP TABLE IPASPROD.EXT_DMS_DOC_MAP;

