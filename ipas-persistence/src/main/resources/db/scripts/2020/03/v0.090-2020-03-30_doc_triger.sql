--liquibase formatted sql

--changeset vnikolov:90.1
/************************************************ TYPES ********************************************************/
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
*/

/*************************************************** [EXT_CORE].[PLANT] ********************************************************************/
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD DOC_ORI varchar(4);
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD DOC_LOG varchar(1);
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD DOC_SER numeric(4, 0);
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD DOC_NBR numeric(15, 0);


CREATE NONCLUSTERED INDEX [DOC_INDEX] ON [EXT_CORE].[INDEX_QUEUE]
(
	[DOC_ORI] ASC,
	[DOC_LOG] ASC,
	[DOC_SER] ASC,
	[DOC_NBR] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];

--changeset vnikolov:90.2
IF OBJECT_ID ('IP_DOC_INDEX_AFTER', 'TR') IS NOT NULL
   DROP TRIGGER [IPASPROD].[IP_DOC_INDEX_AFTER];

--changeset vnikolov:90.3 splitStatements:false
CREATE TRIGGER [IPASPROD].[IP_DOC_INDEX_AFTER]
   ON [IPASPROD].[IP_DOC]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	SET @Type = '10';

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
			JOIN inserted i ON iq.[DOC_ORI] = i.[DOC_ORI] AND iq.[DOC_LOG] = i.[DOC_LOG] AND iq.[DOC_SER] = i.[DOC_SER] AND iq.[DOC_NBR] = i.[DOC_NBR]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[DOC_ORI] = d.[DOC_ORI] AND iq.[DOC_LOG] = d.[DOC_LOG] AND iq.[DOC_SER] = d.[DOC_SER] AND iq.[DOC_NBR] = d.[DOC_NBR]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			GETDATE(),
			@Activity
		FROM
			inserted
		UNION
		SELECT
			@Type,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			GETDATE(),
			@Activity
		FROM
			deleted;
	END
END;
ALTER TABLE [IPASPROD].[IP_DOC] ENABLE TRIGGER [IP_DOC_INDEX_AFTER];

/*************************************************** [IPASPROD].[IP_DOC_FILES_INDEX_AFTER] ********************************************************************/
--changeset vnikolov:90.4
IF OBJECT_ID ('IP_DOC_FILES_INDEX_AFTER', 'TR') IS NOT NULL
   DROP TRIGGER [IPASPROD].[IP_DOC_FILES_INDEX_AFTER];

--changeset vnikolov:90.5 splitStatements:false

CREATE TRIGGER [IPASPROD].[IP_DOC_FILES_INDEX_AFTER]
   ON [IPASPROD].[IP_DOC_FILES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
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
          FROM inserted
    UNION
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
          FROM deleted

END;

ALTER TABLE [IPASPROD].[IP_DOC_FILES] ENABLE TRIGGER [IP_DOC_FILES_INDEX_AFTER];

/*************************************************** [IPASPROD].[PROC_INDEX_AFTER] ********************************************************************/
--changeset vnikolov:90.6
IF OBJECT_ID ('PROC_INDEX_AFTER', 'TR') IS NOT NULL
   DROP TRIGGER [IPASPROD].[PROC_INDEX_AFTER];

--changeset vnikolov:90.7 splitStatements:false
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
			'U'
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
			'D'
		FROM
			deleted;
	END

END;

ALTER TABLE [IPASPROD].[IP_PROC] ENABLE TRIGGER [PROC_INDEX_AFTER];

