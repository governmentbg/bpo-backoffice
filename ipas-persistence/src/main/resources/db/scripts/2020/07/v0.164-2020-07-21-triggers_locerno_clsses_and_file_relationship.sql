--liquibase formatted sql

--changeset vnikolov:164.1
ALTER TABLE [EXT_CORE].[INDEX_QUEUE]
ADD
	[FILE_SEQ2] [varchar](2) NULL,
	[FILE_TYP2] [varchar](1) NULL,
	[FILE_SER2] [numeric](4, 0) NULL,
	[FILE_NBR2] [numeric](10, 0) NULL,
	[RELATIONSHIP_TYP] [varchar](3) NULL,
	[LOCARNO_CLASS_CODE] [varchar](5) NULL
;


/*
0 - not present
1 - patent
2 - mark
3 - person address
4 - proc
5 - action
6 - vienna classes
7 - nice classes
8 - ipc classes
9 - patent summary
10 - doc
11 - userdoc
12 - userdocPerson
13 - locarno classes
14 - IpFileRelationships

all triggers
SELECT
    name,
    is_instead_of_trigger
FROM
    sys.triggers
WHERE
    type = 'TR' and name like '%INDEX_AFTER'
order by name
*/

--changeset vnikolov:164.2 splitStatements:false
/***********************************************************************************************************************/
CREATE NONCLUSTERED INDEX [IP_PATENT_LOCARNO_CLASSES_INDEX] ON [EXT_CORE].[INDEX_QUEUE]
(
    [FILE_SEQ] ASC,
	[FILE_TYP] ASC,
	[FILE_SER] ASC,
	[FILE_NBR] ASC,
	[LOCARNO_CLASS_CODE] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

--changeset vnikolov:164.3 splitStatements:false
/***********************************************************************************************************************/
CREATE NONCLUSTERED INDEX [IP_FILE_RELATIONSHIP_INDEX_AFTER] ON [EXT_CORE].[INDEX_QUEUE]
(
    [FILE_SEQ] ASC,
	[FILE_TYP] ASC,
	[FILE_SER] ASC,
	[FILE_NBR] ASC,
    [FILE_SEQ2] ASC,
	[FILE_TYP2] ASC,
	[FILE_SER2] ASC,
	[FILE_NBR2] ASC,
	[RELATIONSHIP_TYP] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

--changeset vnikolov:169.4 splitStatements:false
/*************************************************** [IPASPROD].[ACTION_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[IP_PATENT_LOCARNO_CLASSES_INDEX_AFTER]
   ON [IPASPROD].[IP_PATENT_LOCARNO_CLASSES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '13';
	SET @TriggerName = '[IPASPROD].[IP_PATENT_LOCARNO_CLASSES_INDEX_AFTER]';

	-- update
	IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
	BEGIN
		SET @Activity = 'U'
	END

	-- insert
	IF EXISTS (SELECT * FROM inserted) AND NOT EXISTS(SELECT * FROM deleted)
	BEGIN
		SET @Activity = 'I'
	END

	-- delete
	IF EXISTS (SELECT * FROM deleted) AND NOT EXISTS(SELECT * FROM inserted)
	BEGIN
		SET @Activity = 'D';
	END

	IF NOT EXISTS (SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ]
			    AND iq.[FILE_TYP] = i.[FILE_TYP]
			    AND iq.[FILE_SER] = i.[FILE_SER]
			    AND iq.[FILE_NBR] = i.[FILE_NBR]
			    AND iq.[LOCARNO_CLASS_CODE] = i.[LOCARNO_CLASS_CODE]
			    WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ]
			    AND iq.[FILE_TYP] = d.[FILE_TYP]
			    AND iq.[FILE_SER] = d.[FILE_SER]
			    AND iq.[FILE_NBR] = d.[FILE_NBR]
			    AND iq.[LOCARNO_CLASS_CODE] = d.[LOCARNO_CLASS_CODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			LOCARNO_CLASS_CODE,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			LOCARNO_CLASS_CODE,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			'D',
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			LOCARNO_CLASS_CODE,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:164.5 splitStatements:false
/*************************************************** [IPASPROD].[IP_DOC_INDEX_AFTER] ********************************************************************/
ALTER TABLE [IPASPROD].[IP_PATENT_LOCARNO_CLASSES] ENABLE TRIGGER [IP_PATENT_LOCARNO_CLASSES_INDEX_AFTER];

--changeset vnikolov:164.6 splitStatements:false
/*************************************************** [IPASPROD].[IP_DOC_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[IP_FILE_RELATIONSHIP_INDEX_AFTER]
   ON [IPASPROD].[IP_FILE_RELATIONSHIP]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '14';
	SET @TriggerName = '[IPASPROD].[IP_FILE_RELATIONSHIP_INDEX_AFTER]';

	-- update
	IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
	BEGIN
		SET @Activity = 'U'
	END

	-- insert
	IF EXISTS (SELECT * FROM inserted) AND NOT EXISTS(SELECT * FROM deleted)
	BEGIN
		SET @Activity = 'I'
	END

	-- delete
	IF EXISTS (SELECT * FROM deleted) AND NOT EXISTS(SELECT * FROM inserted)
	BEGIN
		SET @Activity = 'D';
	END

	IF NOT EXISTS (SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ1]
			    AND iq.[FILE_TYP] = i.[FILE_TYP1]
			    AND iq.[FILE_SER] = i.[FILE_SER1]
			    AND iq.[FILE_NBR] = i.[FILE_NBR1]
			    AND iq.[FILE_SEQ2] = i.[FILE_SEQ2]
			    AND iq.[FILE_TYP2] = i.[FILE_TYP2]
			    AND iq.[FILE_SER2] = i.[FILE_SER2]
			    AND iq.[FILE_NBR2] = i.[FILE_NBR2]
			    AND iq.[RELATIONSHIP_TYP] = i.[RELATIONSHIP_TYP]
			    WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ1]
			    AND iq.[FILE_TYP] = d.[FILE_TYP1]
			    AND iq.[FILE_SER] = d.[FILE_SER1]
			    AND iq.[FILE_NBR] = d.[FILE_NBR1]
			    AND iq.[FILE_SEQ2] = d.[FILE_SEQ2]
			    AND iq.[FILE_TYP2] = d.[FILE_TYP2]
			    AND iq.[FILE_SER2] = d.[FILE_SER2]
			    AND iq.[FILE_NBR2] = d.[FILE_NBR2]
			    AND iq.[RELATIONSHIP_TYP] = d.[RELATIONSHIP_TYP]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			FILE_SEQ2,
			FILE_TYP2,
			FILE_SER2,
			FILE_NBR2,
			RELATIONSHIP_TYP,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ1,
			FILE_TYP1,
			FILE_SER1,
			FILE_NBR1,
			FILE_SEQ2,
			FILE_TYP2,
			FILE_SER2,
			FILE_NBR2,
			RELATIONSHIP_TYP,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			FILE_SEQ1,
			FILE_TYP1,
			FILE_SER1,
			FILE_NBR1,
			FILE_SEQ2,
			FILE_TYP2,
			FILE_SER2,
			FILE_NBR2,
			RELATIONSHIP_TYP,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:164.7 splitStatements:false
/*************************************************** [IPASPROD].[IP_DOC_INDEX_AFTER] ********************************************************************/
ALTER TABLE [IPASPROD].[IP_FILE_RELATIONSHIP] ENABLE TRIGGER [IP_FILE_RELATIONSHIP_INDEX_AFTER];
