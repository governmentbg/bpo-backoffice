--liquibase formatted sql

--changeset vnikolov:23.1
/************************************************ TYPES ********************************************************/
/*
0 - not present
1 - patent
2 - mark
3 - person
4 - proc
5 - action
6 - vienna classes
7 - nice classes
8 - ipc classes
9 - patent summary
*/

SET ANSI_NULLS ON;

SET QUOTED_IDENTIFIER ON;

/*************************************************** [EXT_CORE].[INDEX_QUEUE] ********************************************************************/
CREATE TABLE [EXT_CORE].[INDEX_QUEUE](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TYPE] [varchar](1) NOT NULL,
	[FILE_SEQ] [varchar](2) NULL,
	[FILE_TYP] [varchar](1) NULL,
	[FILE_SER] [numeric](4, 0) NULL,
	[FILE_NBR] [numeric](10, 0) NULL,
	[PERSON_NBR] [numeric](8, 0) NULL,
	[PROC_TYP] [varchar](4) NULL,
	[PROC_NBR] [numeric](8, 0) NULL,
	[ACTION_NBR] [numeric](10, 0) NULL,
	[VIENNA_CLASS_CODE] [numeric](2, 0) NULL,
	[VIENNA_GROUP_CODE] [numeric](2, 0) NULL,
	[VIENNA_ELEM_CODE] [numeric](2, 0) NULL,
	[NICE_CLASS_CODE] [numeric](2, 0) NULL,
	[NICE_CLASS_STATUS_WCODE] [varchar](1) NULL,
	[IPC_EDITION_CODE] [varchar](20) NULL,
	[IPC_SECTION_CODE] [varchar](1) NULL,
	[IPC_CLASS_CODE] [varchar](2) NULL,
	[IPC_SUBCLASS_CODE] [varchar](2) NULL,
	[IPC_GROUP_CODE] [varchar](10) NULL,
	[IPC_SUBGROUP_CODE] [varchar](10) NULL,
	[IPC_QUALIFICATION_CODE] [varchar](2) NULL,
	[LANGUAGE_CODE] [varchar](2) NULL,
	[INSERTED_AT] [datetime] NOT NULL,
	[OPERATION] [varchar](1) NOT NULL,
	[INDEXED_AT] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY];

ALTER TABLE [EXT_CORE].[INDEX_QUEUE]  WITH CHECK ADD CHECK  (([OPERATION]='I' OR [OPERATION]='D' OR [OPERATION]='U'));



/*************************************************** [IPASPROD].[FILE_INDEX_AFTER] ********************************************************************/
--changeset vnikolov:23.2 splitStatements:false
CREATE TRIGGER [IPASPROD].[FILE_INDEX_AFTER]
   ON [IPASPROD].[IP_FILE]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	DECLARE @Activity  NVARCHAR (1)

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE (iq.[TYPE] = '1' AND iq.[TYPE] = '2') AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE (iq.[TYPE] = '1' AND iq.[TYPE] = '2') AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT 
			CASE   
				WHEN EXISTS (SELECT * 
					FROM [IPASPROD].[IP_PATENT] p
					JOIN inserted i ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR]) THEN '1'
				WHEN EXISTS (SELECT * 
					FROM [IPASPROD].[IP_MARK] m
					JOIN inserted i ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]) THEN '2'  
				ELSE '0'  
			END,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT 
			CASE   
				WHEN EXISTS (SELECT * 
					FROM [IPASPROD].[IP_PATENT] p
					JOIN deleted d ON p.[FILE_SEQ] = d.[FILE_SEQ] AND p.[FILE_TYP] = d.[FILE_TYP] AND p.[FILE_SER] = d.[FILE_SER] AND p.[FILE_NBR] = d.[FILE_NBR]) THEN '1'
				WHEN EXISTS (SELECT * 
					FROM [IPASPROD].[IP_MARK] m
					JOIN deleted d ON m.[FILE_SEQ] = d.[FILE_SEQ] AND m.[FILE_TYP] = d.[FILE_TYP] AND m.[FILE_SER] = d.[FILE_SER] AND m.[FILE_NBR] = d.[FILE_NBR]) THEN '2'  
				ELSE '0'  
			END,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END;


ALTER TABLE [IPASPROD].[IP_FILE] ENABLE TRIGGER [FILE_INDEX_AFTER];


--changeset vnikolov:23.3 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[PATENT_INDEX_AFTER] 
   ON [IPASPROD].[IP_PATENT]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '1';

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END;

ALTER TABLE [IPASPROD].[IP_PATENT] ENABLE TRIGGER [PATENT_INDEX_AFTER];


--changeset vnikolov:23.4 splitStatements:false
/*************************************************** [IPASPROD].[MARK_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[MARK_INDEX_AFTER] 
   ON [IPASPROD].[IP_MARK]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '2';

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END;

ALTER TABLE [IPASPROD].[IP_MARK] ENABLE TRIGGER [MARK_INDEX_AFTER];


--changeset vnikolov:23.5 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_INVENTOR_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[PATENT_INVENTOR_INDEX_AFTER] 
   ON [IPASPROD].[IP_PATENT_INVENTORS]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '1';

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END;

ALTER TABLE [IPASPROD].[IP_PATENT_INVENTORS] ENABLE TRIGGER [PATENT_INVENTOR_INDEX_AFTER];


--changeset vnikolov:23.6 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_OWNER_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[PATENT_OWNER_INDEX_AFTER] 
   ON [IPASPROD].[IP_PATENT_OWNERS]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '1';

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END;

ALTER TABLE [IPASPROD].[IP_PATENT_OWNERS] ENABLE TRIGGER [PATENT_OWNER_INDEX_AFTER];


--changeset vnikolov:23.7 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_REPR_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[PATENT_REPR_INDEX_AFTER] 
   ON [IPASPROD].[IP_PATENT_REPRS]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '1';

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END;

ALTER TABLE [IPASPROD].[IP_PATENT_REPRS] ENABLE TRIGGER [PATENT_REPR_INDEX_AFTER];


--changeset vnikolov:23.8 splitStatements:false
/*************************************************** [IPASPROD].[PERSON_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[PERSON_INDEX_AFTER] 
   ON [IPASPROD].[IP_PERSON]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '3';

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
			JOIN inserted i ON iq.[PERSON_NBR] = i.[PERSON_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[PERSON_NBR] = d.[PERSON_NBR]	
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			PERSON_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			PERSON_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			PERSON_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END


	
	IF NOT EXISTS (SELECT TOP (10) SERVICE_PERSON_NBR, SERVICE_ADDR_NBR, per.PERSON_NBR
			  FROM inserted per 
			  JOIN [IPASPROD].[IP_MARK] m ON m.SERVICE_PERSON_NBR = per.PERSON_NBR)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT '1',
			pt.FILE_SEQ,
			pt.FILE_TYP,
			pt.FILE_SER,
			pt.FILE_NBR,
			GETDATE(),
			'U'
			  FROM inserted per 
			  JOIN [IPASPROD].[IP_PATENT] pt ON pt.SERVICE_PERSON_NBR = per.PERSON_NBR
		UNION 
		SELECT '1',
			pt.FILE_SEQ,
			pt.FILE_TYP,
			pt.FILE_SER,
			pt.FILE_NBR,
			GETDATE(),
			'U'
			  FROM deleted per 
			  JOIN [IPASPROD].[IP_PATENT] pt ON pt.SERVICE_PERSON_NBR = per.PERSON_NBR
	END

	
	
	IF NOT EXISTS(SELECT TOP (10) SERVICE_PERSON_NBR, SERVICE_ADDR_NBR, per.PERSON_NBR
			  FROM deleted per 
			  JOIN [IPASPROD].[IP_MARK] m ON m.SERVICE_PERSON_NBR = per.PERSON_NBR)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT '2',
			m.FILE_SEQ,
			m.FILE_TYP,
			m.FILE_SER,
			m.FILE_NBR,
			GETDATE(),
			'U'
			  FROM inserted per 
			  JOIN [IPASPROD].[IP_MARK] m ON m.SERVICE_PERSON_NBR = per.PERSON_NBR
		UNION 
		SELECT '2',
			m.FILE_SEQ,
			m.FILE_TYP,
			m.FILE_SER,
			m.FILE_NBR,
			GETDATE(),
			'U'
			  FROM deleted per 
			  JOIN [IPASPROD].[IP_MARK] m ON m.SERVICE_PERSON_NBR = per.PERSON_NBR
	END

END;

ALTER TABLE [IPASPROD].[IP_PERSON] ENABLE TRIGGER [PERSON_INDEX_AFTER];


--changeset vnikolov:23.9 splitStatements:false
/*************************************************** [IPASPROD].[PROC_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[PROC_INDEX_AFTER] 
   ON [IPASPROD].[IP_PROC]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '4';

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
			JOIN inserted i ON iq.[PROC_NBR] = i.[PROC_NBR]	AND  iq.[PROC_TYP] = i.[PROC_TYP]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[PROC_NBR] = d.[PROC_NBR]	AND  iq.[PROC_TYP] = d.[PROC_TYP]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			PROC_TYP,
			PROC_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			PROC_TYP,
			PROC_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			PROC_TYP,
			PROC_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END


END;

ALTER TABLE [IPASPROD].[IP_PROC] ENABLE TRIGGER [PROC_INDEX_AFTER];



--changeset vnikolov:23.10 splitStatements:false
/*************************************************** [IPASPROD].[ACTION_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[ACTION_INDEX_AFTER] 
   ON [IPASPROD].[IP_ACTION]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '5';

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
			JOIN inserted i ON iq.[PROC_NBR] = i.[PROC_NBR]	AND  iq.[PROC_TYP] = i.[PROC_TYP] AND iq.[ACTION_NBR] = i.[ACTION_NBR] 
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[PROC_NBR] = d.[PROC_NBR]	AND  iq.[PROC_TYP] = d.[PROC_TYP] AND iq.[ACTION_NBR] = d.[ACTION_NBR] 
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			PROC_TYP,
			PROC_NBR,
			ACTION_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			PROC_TYP,
			PROC_NBR,
			ACTION_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			PROC_TYP,
			PROC_NBR,
			ACTION_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END


END
;

ALTER TABLE [IPASPROD].[IP_ACTION] ENABLE TRIGGER [ACTION_INDEX_AFTER]
;



--changeset vnikolov:23.11 splitStatements:false
/*************************************************** [EXT_JOURNAL].[JOURNAL_ELEMENT_INDEX_AFTER]  ********************************************************************/
CREATE TRIGGER [EXT_JOURNAL].[JOURNAL_ELEMENT_INDEX_AFTER] 
   ON [EXT_JOURNAL].[JOURNAL_ELEMENT]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '5';

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
	
	/************* ACTION_* fields  ************************/
	IF NOT EXISTS (SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[PROC_NBR] = i.ACTION_PROC_NBR	AND  iq.[PROC_TYP] = i.ACTION_PROC_TYPE AND iq.[ACTION_NBR] = i.ACTION_ACTION_NBR 
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[PROC_NBR] = d.ACTION_PROC_NBR	AND  iq.[PROC_TYP] = d.ACTION_PROC_TYPE AND iq.[ACTION_NBR] = d.ACTION_ACTION_NBR 
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			PROC_TYP,
			PROC_NBR,
			ACTION_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			ACTION_PROC_TYPE,
			ACTION_PROC_NBR,
			ACTION_ACTION_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			ACTION_PROC_TYPE,
			ACTION_PROC_NBR,
			ACTION_ACTION_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
	
	/************* ACTIONUDOC_* fields  ************************/
	IF NOT EXISTS (SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[PROC_NBR] = i.ACTIONUDOC_PROC_NBR	AND  iq.[PROC_TYP] = i.ACTIONUDOC_PROC_TYPE AND iq.[ACTION_NBR] = i.ACTIONUDOC_ACTION_NBR 
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[PROC_NBR] = d.ACTIONUDOC_PROC_NBR	AND  iq.[PROC_TYP] = d.ACTIONUDOC_PROC_TYPE AND iq.[ACTION_NBR] = d.ACTIONUDOC_ACTION_NBR 
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			PROC_TYP,
			PROC_NBR,
			ACTION_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			ACTIONUDOC_PROC_TYPE,
			ACTIONUDOC_PROC_NBR,
			ACTIONUDOC_ACTION_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			ACTIONUDOC_PROC_TYPE,
			ACTIONUDOC_PROC_NBR,
			ACTIONUDOC_ACTION_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END


END
;

ALTER TABLE [EXT_JOURNAL].[JOURNAL_ELEMENT] ENABLE TRIGGER [JOURNAL_ELEMENT_INDEX_AFTER]
;


--changeset vnikolov:23.12 splitStatements:false
/*************************************************** [EXT_JOURNAL].[LOGO_VIENNA_CLASSES_INDEX_AFTER]  ********************************************************************/
CREATE TRIGGER [IPASPROD].[LOGO_VIENNA_CLASSES_INDEX_AFTER] 
   ON [IPASPROD].[IP_LOGO_VIENNA_CLASSES]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '6';

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END
;

ALTER TABLE [IPASPROD].[IP_LOGO_VIENNA_CLASSES] ENABLE TRIGGER [LOGO_VIENNA_CLASSES_INDEX_AFTER]
;


--changeset vnikolov:23.13 splitStatements:false
/*************************************************** [EXT_JOURNAL].[MARK_NICE_CLASSES_INDEX_AFTER]  ********************************************************************/
CREATE TRIGGER [IPASPROD].[MARK_NICE_CLASSES_INDEX_AFTER] 
   ON [IPASPROD].[IP_MARK_NICE_CLASSES]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '7';

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END
;

ALTER TABLE [IPASPROD].[IP_MARK_NICE_CLASSES] ENABLE TRIGGER [MARK_NICE_CLASSES_INDEX_AFTER]
;


--changeset vnikolov:23.14 splitStatements:false
/*************************************************** [EXT_JOURNAL].[PATENT_IPC_CLASSES_INDEX_AFTER]  ********************************************************************/
CREATE TRIGGER [IPASPROD].[PATENT_IPC_CLASSES_INDEX_AFTER] 
   ON [IPASPROD].[IP_PATENT_IPC_CLASSES]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '8';

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END
;

ALTER TABLE [IPASPROD].[IP_PATENT_IPC_CLASSES] ENABLE TRIGGER [PATENT_IPC_CLASSES_INDEX_AFTER]
;


--changeset vnikolov:23.15 splitStatements:false
/*************************************************** [EXT_JOURNAL].[PATENT_SUMMARY_INDEX_AFTER]  ********************************************************************/
CREATE TRIGGER [IPASPROD].[PATENT_SUMMARY_INDEX_AFTER] 
   ON [IPASPROD].[IP_PATENT_SUMMARY]
   AFTER INSERT, UPDATE, DELETE
AS 
BEGIN
	SET NOCOUNT ON;
	
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '9';

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
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null) 
		AND NOT EXISTS(SELECT * 
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]		
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted 
		UNION 
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END
;

ALTER TABLE [IPASPROD].[IP_PATENT_SUMMARY] ENABLE TRIGGER [PATENT_SUMMARY_INDEX_AFTER]
;


