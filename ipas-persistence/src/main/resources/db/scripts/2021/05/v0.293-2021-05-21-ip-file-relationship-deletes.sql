--liquibase formatted sql

--changeset ggeorgiev:293.1
CREATE TABLE [EXT_CORE].[IP_FILE_RELATIONSHIP_DELETES] (
	[FILE_SEQ1] [varchar](2) NOT NULL,
	[FILE_TYP1] [varchar](1) NOT NULL,
	[FILE_SER1] [numeric](4, 0) NOT NULL,
	[FILE_NBR1] [numeric](10, 0) NOT NULL,
	[FILE_SEQ2] [varchar](2) NOT NULL,
	[FILE_TYP2] [varchar](1) NOT NULL,
	[FILE_SER2] [numeric](4, 0) NOT NULL,
	[FILE_NBR2] [numeric](10, 0) NOT NULL,
	[RELATIONSHIP_TYP] [varchar](3) NOT NULL,
	[DATE_DELETED] datetime
 CONSTRAINT [IP_FILE_RELATIONSHIP_DELETES_PK] PRIMARY KEY NONCLUSTERED
(
	[FILE_NBR1] ASC,
	[FILE_SEQ1] ASC,
	[FILE_TYP1] ASC,
	[FILE_SER1] ASC,
	[FILE_SEQ2] ASC,
	[FILE_TYP2] ASC,
	[FILE_SER2] ASC,
	[FILE_NBR2] ASC,
	[RELATIONSHIP_TYP] ASC,
	DATE_DELETED ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY];


--changeset ggeorgiev:293.2 splitStatements:false
CREATE TRIGGER [IPASPROD].[IP_FILE_RELATIONSHIP_AFTER]
   ON [IPASPROD].[IP_FILE_RELATIONSHIP]
   AFTER UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	-- update
	IF EXISTS (SELECT * FROM deleted)
	BEGIN
		INSERT INTO EXT_CORE.IP_FILE_RELATIONSHIP_DELETES (FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1,
		FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP, DATE_DELETED)
		SELECT
			FILE_SEQ1, FILE_TYP1, FILE_SER1, FILE_NBR1, FILE_SEQ2, FILE_TYP2, FILE_SER2, FILE_NBR2, RELATIONSHIP_TYP, getdate() FROM deleted;
	END
END;

--changeset ggeorgiev:293.3
ALTER TABLE [IPASPROD].[IP_FILE_RELATIONSHIP] ENABLE TRIGGER [IP_FILE_RELATIONSHIP_AFTER];