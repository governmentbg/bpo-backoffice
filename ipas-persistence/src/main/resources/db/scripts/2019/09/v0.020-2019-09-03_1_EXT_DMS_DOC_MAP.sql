CREATE TABLE IPASPROD.EXT_DMS_DOC_MAP
(
    OFFIDOC_ORI                varchar(4)    NOT NULL,
    OFFIDOC_SER                numeric(4, 0) NOT NULL,
    OFFIDOC_NBR                numeric(6, 0) NOT NULL,
    ACSTRE_DOCUMENT_ID         varchar(100),
    PARENT_DOCUMENT_ID         varchar(100)  NULL,
    ABDOCS_DOC_ID              integer,
    ABDOCS_REGISTRATION_NUMBER varchar(20),
    CONSTRAINT PK_EXT_DMS_DOC_MAP PRIMARY KEY (OFFIDOC_ORI, OFFIDOC_SER, OFFIDOC_NBR)
);


