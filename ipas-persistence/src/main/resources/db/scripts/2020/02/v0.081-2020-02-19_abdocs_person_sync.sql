create table EXT_CORE.IP_PERSON_ABDOCS_SYNC
(
    ID             [int] IDENTITY
        CONSTRAINT IP_PERSON_ABDOCS_SYNC_PK PRIMARY KEY NONCLUSTERED,
    [PERSON_NBR]   [numeric](8) NOT NULL,
    [ADDR_NBR]     [numeric](4) NOT NULL,
    [INSERTED_AT]  [datetime]   NOT NULL,
    [PROCESSED_AT] [datetime]   NULL,
    [IND_SYNC]     [varchar](1) NULL,
);

