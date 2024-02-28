--liquibase formatted sql

--changeset vnikolov:109.1
ALTER TABLE [EXT_CORE].[INDEX_QUEUE]
ADD TRIGGER_NAME varchar(50);
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

--changeset vnikolov:109.2 splitStatements:false
/*************************************************** [IPASPROD].[ACTION_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[ACTION_INDEX_AFTER]
   ON [IPASPROD].[IP_ACTION]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '5';
	SET @TriggerName = '[IPASPROD].[ACTION_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			PROC_TYP,
			PROC_NBR,
			ACTION_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			PROC_TYP,
			PROC_NBR,
			ACTION_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.3 splitStatements:false
/*************************************************** [IPASPROD].[IP_DOC_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[IP_DOC_INDEX_AFTER]
   ON [IPASPROD].[IP_DOC]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '10';
	SET @TriggerName = '[IPASPROD].[IP_DOC_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;

		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			'11',
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			GETDATE(),
			'U',
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			'11',
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			GETDATE(),
			'U',
			@TriggerName
		FROM
			deleted;
	END
END
;
--changeset vnikolov:109.4 splitStatements:false
/*************************************************** [IPASPROD].[IP_DOC_FILES_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[IP_DOC_FILES_INDEX_AFTER]
   ON [IPASPROD].[IP_DOC_FILES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	DECLARE @Activity  NVARCHAR (1)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Activity = 'U'
	SET @TriggerName = '[IPASPROD].[IP_DOC_FILES_INDEX_AFTER]';

    INSERT INTO [EXT_CORE].[INDEX_QUEUE](
        TYPE,
        FILE_SEQ,
        FILE_TYP,
        FILE_SER,
        FILE_NBR,
        INSERTED_AT,
        OPERATION,
		TRIGGER_NAME
    )
    SELECT
        CASE
            WHEN EXISTS (SELECT *
                FROM [IPASPROD].[IP_PATENT] p
                JOIN inserted i ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR]) THEN '1'
            WHEN EXISTS (SELECT *
                FROM [IPASPROD].[IP_MARK] m
                JOIN inserted i ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]) THEN '2'
            ELSE '99'
        END,
        FILE_SEQ,
        FILE_TYP,
        FILE_SER,
        FILE_NBR,
        GETDATE(),
        @Activity,
		@TriggerName
          FROM inserted
    UNION
    SELECT
        CASE
            WHEN EXISTS (SELECT *
                FROM [IPASPROD].[IP_PATENT] p
                JOIN deleted i ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR]) THEN '1'
            WHEN EXISTS (SELECT *
                FROM [IPASPROD].[IP_MARK] m
                JOIN deleted i ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]) THEN '2'
            ELSE '98'
        END,
        FILE_SEQ,
        FILE_TYP,
        FILE_SER,
        FILE_NBR,
        GETDATE(),
        @Activity,
		@TriggerName
          FROM deleted
END
;
--changeset vnikolov:109.5 splitStatements:false
/*************************************************** [IPASPROD].[FILE_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[FILE_INDEX_AFTER]
   ON [IPASPROD].[IP_FILE]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @TriggerName = '[IPASPROD].[FILE_INDEX_AFTER]';

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
			WHERE (iq.[TYPE] = '1' OR iq.[TYPE] = '2') AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
			WHERE (iq.[TYPE] = '1' OR iq.[TYPE] = '2') AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND EXISTS ( SELECT m.FILE_NBR
			FROM [IPASPROD].[IP_MARK] m
			JOIN inserted i ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]
			UNION SELECT  p.FILE_NBR
			FROM [IPASPROD].[IP_PATENT] p
			JOIN inserted i ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR])
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			CASE
				WHEN EXISTS (SELECT *
					FROM [IPASPROD].[IP_PATENT] p
					JOIN inserted i ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR]
					WHERE p.FILE_SEQ = ii.FILE_SEQ and p.FILE_TYP = ii.FILE_TYP and p.FILE_SER = ii.FILE_SER and p.FILE_NBR = ii.FILE_NBR) THEN '1'
				WHEN EXISTS (SELECT *
					FROM [IPASPROD].[IP_MARK] m
					JOIN inserted i ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]
					WHERE m.FILE_SEQ = ii.FILE_SEQ and m.FILE_TYP = ii.FILE_TYP and m.FILE_SER = ii.FILE_SER and m.FILE_NBR = ii.FILE_NBR) THEN '2'
				ELSE '96'
			END,
			ii.FILE_SEQ,
			ii.FILE_TYP,
			ii.FILE_SER,
			ii.FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted ii
		UNION
		SELECT
			CASE
				WHEN EXISTS (SELECT *
					FROM [IPASPROD].[IP_PATENT] p
					JOIN deleted d ON p.[FILE_SEQ] = d.[FILE_SEQ] AND p.[FILE_TYP] = d.[FILE_TYP] AND p.[FILE_SER] = d.[FILE_SER] AND p.[FILE_NBR] = d.[FILE_NBR]
					WHERE p.FILE_SEQ = dd.FILE_SEQ and p.FILE_TYP = dd.FILE_TYP and p.FILE_SER = dd.FILE_SER and p.FILE_NBR = dd.FILE_NBR) THEN '1'
				WHEN EXISTS (SELECT *
					FROM [IPASPROD].[IP_MARK] m
					JOIN deleted d ON m.[FILE_SEQ] = d.[FILE_SEQ] AND m.[FILE_TYP] = d.[FILE_TYP] AND m.[FILE_SER] = d.[FILE_SER] AND m.[FILE_NBR] = d.[FILE_NBR]
					WHERE m.FILE_SEQ = dd.FILE_SEQ and m.FILE_TYP = dd.FILE_TYP and m.FILE_SER = dd.FILE_SER and m.FILE_NBR = dd.FILE_NBR) THEN '2'
				ELSE '95'
			END,
			dd.FILE_SEQ,
			dd.FILE_TYP,
			dd.FILE_SER,
			dd.FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted dd;
	END
END
;
--changeset vnikolov:109.6 splitStatements:false
/*************************************************** [EXT_JOURNAL].[JOURNAL_ELEMENT_INDEX_AFTER]  ********************************************************************/
ALTER TRIGGER [EXT_JOURNAL].[JOURNAL_ELEMENT_INDEX_AFTER]
   ON [EXT_JOURNAL].[JOURNAL_ELEMENT]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '5';
	SET @TriggerName = '[IPASPROD].[JOURNAL_ELEMENT_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			ACTION_PROC_TYPE,
			ACTION_PROC_NBR,
			ACTION_ACTION_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			ACTION_PROC_TYPE,
			ACTION_PROC_NBR,
			ACTION_ACTION_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			ACTIONUDOC_PROC_TYPE,
			ACTIONUDOC_PROC_NBR,
			ACTIONUDOC_ACTION_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			ACTIONUDOC_PROC_TYPE,
			ACTIONUDOC_PROC_NBR,
			ACTIONUDOC_ACTION_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;
--changeset vnikolov:109.7 splitStatements:false
/*************************************************** [EXT_JOURNAL].[LOGO_VIENNA_CLASSES_INDEX_AFTER]  ********************************************************************/
ALTER TRIGGER [IPASPROD].[LOGO_VIENNA_CLASSES_INDEX_AFTER]
   ON [IPASPROD].[IP_LOGO_VIENNA_CLASSES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '6';
	SET @TriggerName = '[IPASPROD].[LOGO_VIENNA_CLASSES_INDEX_AFTER]';

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
			JOIN inserted i ON
				iq.[FILE_SEQ] = i.[FILE_SEQ] AND
				iq.[FILE_TYP] = i.[FILE_TYP] AND
				iq.[FILE_SER] = i.[FILE_SER] AND
				iq.[FILE_NBR] = i.[FILE_NBR] AND
				iq.[VIENNA_CLASS_CODE] = i.[VIENNA_CLASS_CODE] AND
				iq.[VIENNA_GROUP_CODE] = i.[VIENNA_GROUP_CODE] AND
				iq.[VIENNA_ELEM_CODE] = i.[VIENNA_ELEM_CODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON
				iq.[FILE_SEQ] = d.[FILE_SEQ] AND
				iq.[FILE_TYP] = d.[FILE_TYP] AND
				iq.[FILE_SER] = d.[FILE_SER] AND
				iq.[FILE_NBR] = d.[FILE_NBR] AND
				iq.[VIENNA_CLASS_CODE] = d.[VIENNA_CLASS_CODE] AND
				iq.[VIENNA_GROUP_CODE] = d.[VIENNA_GROUP_CODE] AND
				iq.[VIENNA_ELEM_CODE] = d.[VIENNA_ELEM_CODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			[VIENNA_CLASS_CODE],
			[VIENNA_GROUP_CODE],
			[VIENNA_ELEM_CODE],
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
			[VIENNA_CLASS_CODE],
			[VIENNA_GROUP_CODE],
			[VIENNA_ELEM_CODE],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			[VIENNA_CLASS_CODE],
			[VIENNA_GROUP_CODE],
			[VIENNA_ELEM_CODE],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;
--changeset vnikolov:109.8 splitStatements:false
/*************************************************** [IPASPROD].[MARK_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_INDEX_AFTER]
   ON [IPASPROD].[IP_MARK]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '2';
	SET @TriggerName = '[IPASPROD].[MARK_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.9 splitStatements:false
/*************************************************** [IPASPROD].[MARK_LOGO_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_LOGO_INDEX_AFTER]
   ON [IPASPROD].[IP_LOGO]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '2';
	SET @TriggerName = '[IPASPROD].[MARK_LOGO_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.10 splitStatements:false
/*************************************************** [IPASPROD].[MARK_NICE_CLASSES_INDEX_AFTER]  ********************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_NICE_CLASSES_INDEX_AFTER]
   ON [IPASPROD].[IP_MARK_NICE_CLASSES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '7';
	SET @TriggerName = '[IPASPROD].[MARK_NICE_CLASSES_INDEX_AFTER]';

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
			JOIN inserted i ON
				iq.[FILE_SEQ] = i.[FILE_SEQ] AND
				iq.[FILE_TYP] = i.[FILE_TYP] AND
				iq.[FILE_SER] = i.[FILE_SER] AND
				iq.[FILE_NBR] = i.[FILE_NBR] AND
				iq.[NICE_CLASS_CODE] = i.[NICE_CLASS_CODE] AND
				iq.[NICE_CLASS_STATUS_WCODE] = i.[NICE_CLASS_STATUS_WCODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON
				iq.[FILE_SEQ] = d.[FILE_SEQ] AND
				iq.[FILE_TYP] = d.[FILE_TYP] AND
				iq.[FILE_SER] = d.[FILE_SER] AND
				iq.[FILE_NBR] = d.[FILE_NBR] AND
				iq.[NICE_CLASS_CODE] = d.[NICE_CLASS_CODE] AND
				iq.[NICE_CLASS_STATUS_WCODE] = d.[NICE_CLASS_STATUS_WCODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			[NICE_CLASS_CODE],
			[NICE_CLASS_STATUS_WCODE],
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
			[NICE_CLASS_CODE],
			[NICE_CLASS_STATUS_WCODE],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			[NICE_CLASS_CODE],
			[NICE_CLASS_STATUS_WCODE],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.11 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_OWNER_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_OWNER_INDEX_AFTER]
   ON [IPASPROD].[IP_MARK_OWNERS]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '2';
	SET @Activity = 'U';
	SET @TriggerName = '[IPASPROD].[MARK_OWNER_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.12 splitStatements:false
/*************************************************** [IPASPROD].[MARK_REPRS_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_REPRS_INDEX_AFTER]
   ON [IPASPROD].[IP_MARK_REPRS]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Activity = 'U';
	SET @Type = '2';
	SET @TriggerName = '[IPASPROD].[MARK_REPRS_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.13 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_INDEX_AFTER]
   ON [IPASPROD].[IP_PATENT]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '1';
	SET @TriggerName = '[IPASPROD].[PATENT_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.14 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_INVENTOR_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_INVENTOR_INDEX_AFTER]
   ON [IPASPROD].[IP_PATENT_INVENTORS]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Activity = 'U'
	SET @Type = '1';
	SET @TriggerName = '[IPASPROD].[PATENT_INVENTOR_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.15 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_IPC_CLASSES_INDEX_AFTER]  ********************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_IPC_CLASSES_INDEX_AFTER]
   ON [IPASPROD].[IP_PATENT_IPC_CLASSES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '8';
	SET @TriggerName = '[IPASPROD].[PATENT_IPC_CLASSES_INDEX_AFTER]';

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
			JOIN inserted i ON
				iq.[FILE_SEQ] = i.[FILE_SEQ] AND
				iq.[FILE_TYP] = i.[FILE_TYP] AND
				iq.[FILE_SER] = i.[FILE_SER] AND
				iq.[FILE_NBR] = i.[FILE_NBR] AND
				iq.[IPC_EDITION_CODE] = i.[IPC_EDITION_CODE] AND
				iq.[IPC_SECTION_CODE] = i.[IPC_SECTION_CODE] AND
				iq.[IPC_CLASS_CODE] = i.[IPC_CLASS_CODE] AND
				iq.[IPC_SUBCLASS_CODE] = i.[IPC_SUBCLASS_CODE] AND
				iq.[IPC_GROUP_CODE] = i.[IPC_GROUP_CODE] AND
				iq.[IPC_SUBGROUP_CODE] = i.[IPC_SUBGROUP_CODE] AND
				iq.[IPC_QUALIFICATION_CODE] = i.[IPC_QUALIFICATION_CODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON
				iq.[FILE_SEQ] = d.[FILE_SEQ] AND
				iq.[FILE_TYP] = d.[FILE_TYP] AND
				iq.[FILE_SER] = d.[FILE_SER] AND
				iq.[FILE_NBR] = d.[FILE_NBR] AND
				iq.[IPC_EDITION_CODE] = d.[IPC_EDITION_CODE] AND
				iq.[IPC_SECTION_CODE] = d.[IPC_SECTION_CODE] AND
				iq.[IPC_CLASS_CODE] = d.[IPC_CLASS_CODE] AND
				iq.[IPC_SUBCLASS_CODE] = d.[IPC_SUBCLASS_CODE] AND
				iq.[IPC_GROUP_CODE] = d.[IPC_GROUP_CODE] AND
				iq.[IPC_SUBGROUP_CODE] = d.[IPC_SUBGROUP_CODE] AND
				iq.[IPC_QUALIFICATION_CODE] = d.[IPC_QUALIFICATION_CODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			[IPC_EDITION_CODE],
			[IPC_SECTION_CODE],
			[IPC_CLASS_CODE],
			[IPC_SUBCLASS_CODE],
			[IPC_GROUP_CODE],
			[IPC_SUBGROUP_CODE],
			[IPC_QUALIFICATION_CODE],
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
			[IPC_EDITION_CODE],
			[IPC_SECTION_CODE],
			[IPC_CLASS_CODE],
			[IPC_SUBCLASS_CODE],
			[IPC_GROUP_CODE],
			[IPC_SUBGROUP_CODE],
			[IPC_QUALIFICATION_CODE],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			[IPC_EDITION_CODE],
			[IPC_SECTION_CODE],
			[IPC_CLASS_CODE],
			[IPC_SUBCLASS_CODE],
			[IPC_GROUP_CODE],
			[IPC_SUBGROUP_CODE],
			[IPC_QUALIFICATION_CODE],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.16 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_OWNER_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_OWNER_INDEX_AFTER]
   ON [IPASPROD].[IP_PATENT_OWNERS]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (1)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Activity = 'U'
	SET @Type = '1';
	SET @TriggerName = '[IPASPROD].[PATENT_OWNER_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.17 splitStatements:false
/*************************************************** [IPASPROD].[PATENT_REPR_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_REPR_INDEX_AFTER]
   ON [IPASPROD].[IP_PATENT_REPRS]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Activity = 'U'
	SET @Type = '1';
	SET @TriggerName = '[IPASPROD].[PATENT_REPR_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.18 splitStatements:false
/*************************************************** [EXT_JOURNAL].[PATENT_SUMMARY_INDEX_AFTER]  ********************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_SUMMARY_INDEX_AFTER]
   ON [IPASPROD].[IP_PATENT_SUMMARY]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '9';
	SET @TriggerName = '[IPASPROD].[PATENT_SUMMARY_INDEX_AFTER]';

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
			JOIN inserted i ON
				iq.[FILE_SEQ] = i.[FILE_SEQ] AND
				iq.[FILE_TYP] = i.[FILE_TYP] AND
				iq.[FILE_SER] = i.[FILE_SER] AND
				iq.[FILE_NBR] = i.[FILE_NBR] AND
				iq.[LANGUAGE_CODE] = i.[LANGUAGE_CODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON
				iq.[FILE_SEQ] = d.[FILE_SEQ] AND
				iq.[FILE_TYP] = d.[FILE_TYP] AND
				iq.[FILE_SER] = d.[FILE_SER] AND
				iq.[FILE_NBR] = d.[FILE_NBR] AND
				iq.[LANGUAGE_CODE] = d.[LANGUAGE_CODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			LANGUAGE_CODE,
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
			LANGUAGE_CODE,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			LANGUAGE_CODE,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.19 splitStatements:false
/*************************************************** [IPASPROD].[PERSON_ADDRESS_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[PERSON_ADDRESS_INDEX_AFTER]
   ON [IPASPROD].[IP_PERSON_ADDRESSES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '3';
	SET @TriggerName = '[IPASPROD].[PERSON_ADDRESS_INDEX_AFTER]';

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
			JOIN inserted i ON iq.[PERSON_NBR] = i.[PERSON_NBR] AND iq.[ADDR_NBR] = i.[ADDR_NBR]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[PERSON_NBR] = d.[PERSON_NBR] AND iq.[ADDR_NBR] = d.[ADDR_NBR]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			PERSON_NBR,
			ADDR_NBR,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			[PERSON_NBR],
			[ADDR_NBR],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			[PERSON_NBR],
			[ADDR_NBR],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.20 splitStatements:false
/*************************************************** [IPASPROD].[PERSON_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[PERSON_INDEX_AFTER]
   ON [IPASPROD].[IP_PERSON]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '3';
	SET @TriggerName = '[IPASPROD].[PERSON_INDEX_AFTER]';

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
			ADDR_NBR,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			pa.[PERSON_NBR],
			pa.[ADDR_NBR],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted i
        JOIN [IPASPROD].[IP_PERSON_ADDRESSES] pa ON i.[PERSON_NBR] = pa.[PERSON_NBR]
		UNION
		SELECT
			@Type,
			pa.[PERSON_NBR],
			pa.[ADDR_NBR],
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted d
        JOIN [IPASPROD].[IP_PERSON_ADDRESSES] pa ON d.[PERSON_NBR] = pa.[PERSON_NBR];
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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT '1',
			pt.FILE_SEQ,
			pt.FILE_TYP,
			pt.FILE_SER,
			pt.FILE_NBR,
			GETDATE(),
			'U',
			@TriggerName
			  FROM inserted per
			  JOIN [IPASPROD].[IP_PATENT] pt ON pt.SERVICE_PERSON_NBR = per.PERSON_NBR
		UNION
		SELECT '1',
			pt.FILE_SEQ,
			pt.FILE_TYP,
			pt.FILE_SER,
			pt.FILE_NBR,
			GETDATE(),
			'U',
			@TriggerName
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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT '2',
			m.FILE_SEQ,
			m.FILE_TYP,
			m.FILE_SER,
			m.FILE_NBR,
			GETDATE(),
			'U',
			@TriggerName
			  FROM inserted per
			  JOIN [IPASPROD].[IP_MARK] m ON m.SERVICE_PERSON_NBR = per.PERSON_NBR
		UNION
		SELECT '2',
			m.FILE_SEQ,
			m.FILE_TYP,
			m.FILE_SER,
			m.FILE_NBR,
			GETDATE(),
			'U',
			@TriggerName
			  FROM deleted per
			  JOIN [IPASPROD].[IP_MARK] m ON m.SERVICE_PERSON_NBR = per.PERSON_NBR
	END
END
;

--changeset vnikolov:109.21 splitStatements:false
/*************************************************** [EXT_CORE].[PLANT_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [EXT_CORE].[PLANT_INDEX_AFTER]
   ON [EXT_CORE].[PLANT]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Activity = 'U'
	SET @Type = '1';
	SET @TriggerName = '[EXT_CORE].[PLANT_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:109.22 splitStatements:false
/*************************************************** [EXT_CORE].[PLANT_TAXON_NOMENCLATURE_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [EXT_CORE].[PLANT_TAXON_NOMENCLATURE_INDEX_AFTER]
   ON [EXT_CORE].[PLANT_TAXON_NOMENCLATURE]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Activity = 'U'
	SET @Type = '1';
	SET @TriggerName = '[IPASPROD].[PLANT_TAXON_NOMENCLATURE_INDEX_AFTER]';

	IF NOT EXISTS (SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN [EXT_CORE].[PLANT] p ON iq.[FILE_SEQ] = p.[FILE_SEQ] AND iq.[FILE_TYP] = p.[FILE_TYP] AND iq.[FILE_SER] = p.[FILE_SER] AND iq.[FILE_NBR] = p.[FILE_NBR]
			JOIN inserted i ON p.[PLANT_NUMENCLATURE_ID] = i.[ID]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN [EXT_CORE].[PLANT] p ON iq.[FILE_SEQ] = p.[FILE_SEQ] AND iq.[FILE_TYP] = p.[FILE_TYP] AND iq.[FILE_SER] = p.[FILE_SER] AND iq.[FILE_NBR] = p.[FILE_NBR]
			JOIN deleted d ON p.[PLANT_NUMENCLATURE_ID] = d.[ID]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			p.FILE_SEQ,
			p.FILE_TYP,
			p.FILE_SER,
			p.FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted i
			JOIN [EXT_CORE].[PLANT] p ON p.[PLANT_NUMENCLATURE_ID] = i.[ID]
		UNION
		SELECT
			@Type,
			p.FILE_SEQ,
			p.FILE_TYP,
			p.FILE_SER,
			p.FILE_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted d
			JOIN [EXT_CORE].[PLANT] p ON p.[PLANT_NUMENCLATURE_ID] = d.[ID] ;
	END
END
;

--changeset vnikolov:109.23 splitStatements:false
/*************************************************** [IPASPROD].[PROC_INDEX_AFTER] ********************************************************************/
ALTER TRIGGER [IPASPROD].[PROC_INDEX_AFTER]
   ON [IPASPROD].[IP_PROC]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '4';
	SET @TriggerName = '[IPASPROD].[PROC_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			PROC_TYP,
			PROC_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			PROC_TYP,
			PROC_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;

		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			PROC_TYP,
			PROC_NBR,
			ACTION_NBR,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null


		IF EXISTS(SELECT i.FILE_SEQ, i.FILE_TYP, i.FILE_SER, i.FILE_NBR
				FROM [IPASPROD].[IP_PATENT] p
				JOIN inserted i ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR]
			UNION
			SELECT i.FILE_SEQ, i.FILE_TYP, i.FILE_SER, i.FILE_NBR
				FROM [IPASPROD].[IP_MARK] m
				JOIN inserted i ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]
			UNION
			SELECT d.FILE_SEQ, d.FILE_TYP, d.FILE_SER, d.FILE_NBR
				FROM [IPASPROD].[IP_PATENT] p
				JOIN deleted d ON p.[FILE_SEQ] = d.[FILE_SEQ] AND p.[FILE_TYP] = d.[FILE_TYP] AND p.[FILE_SER] = d.[FILE_SER] AND p.[FILE_NBR] = d.[FILE_NBR]
			UNION
			SELECT d.FILE_SEQ, d.FILE_TYP, d.FILE_SER, d.FILE_NBR
				FROM [IPASPROD].[IP_MARK] m
				JOIN deleted d ON m.[FILE_SEQ] = d.[FILE_SEQ] AND m.[FILE_TYP] = d.[FILE_TYP] AND m.[FILE_SER] = d.[FILE_SER] AND m.[FILE_NBR] = d.[FILE_NBR])
		BEGIN
			INSERT INTO [EXT_CORE].[INDEX_QUEUE](
				TYPE,
				FILE_SEQ,
				FILE_TYP,
				FILE_SER,
				FILE_NBR,
				INSERTED_AT,
				OPERATION,
				TRIGGER_NAME
			)
			SELECT
				CASE
					WHEN EXISTS (SELECT *
						FROM [IPASPROD].[IP_PATENT] p
						JOIN inserted i ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR]) THEN '1'
					WHEN EXISTS (SELECT *
						FROM [IPASPROD].[IP_MARK] m
						JOIN inserted i ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]) THEN '2'
					ELSE '94'
				END,
				FILE_SEQ,
				FILE_TYP,
				FILE_SER,
				FILE_NBR,
				GETDATE(),
				'U',
				@TriggerName
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
					ELSE '93'
				END,
				FILE_SEQ,
				FILE_TYP,
				FILE_SER,
				FILE_NBR,
				GETDATE(),
				'U',
				@TriggerName
			FROM
				deleted;
		END
	END
END
;
--changeset vnikolov:109.24.1 splitStatements:false
IF OBJECT_ID ('IP_USERDOC_INDEX_AFTER', 'TR') IS NOT NULL
   DROP TRIGGER [IPASPROD].[IP_USERDOC_INDEX_AFTER];

--changeset vnikolov:109.24.2 splitStatements:false
/*************************************************** [IPASPROD].[IP_USERDOC_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[IP_USERDOC_INDEX_AFTER]
   ON  [IPASPROD].[IP_USERDOC]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

    DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '11';
	SET @TriggerName = '[IPASPROD].[IP_USERDOC_INDEX_AFTER]';

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
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END

END
;

--changeset vnikolov:109.25.1 splitStatements:false
IF OBJECT_ID ('IP_USERDOC_TYPES_INDEX_AFTER', 'TR') IS NOT NULL
   DROP TRIGGER [IPASPROD].[IP_USERDOC_TYPES_INDEX_AFTER];

--changeset vnikolov:109.25.2 splitStatements:false
/*************************************************** [IPASPROD].[IP_USERDOC_TYPES_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[IP_USERDOC_TYPES_INDEX_AFTER]
   ON  [IPASPROD].[IP_USERDOC_TYPES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

    DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Activity = 'U'
	SET @Type = '11';
	SET @TriggerName = '[IPASPROD].[IP_USERDOC_TYPES_INDEX_AFTER]';

	IF NOT EXISTS (SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[DOC_ORI] = i.[DOC_ORI] AND iq.[DOC_LOG] = i.[DOC_LOG] AND iq.[DOC_SER] = i.[DOC_SER] AND iq.[DOC_NBR] = i.[DOC_NBR]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[DOC_ORI] = d.[DOC_ORI] AND iq.[DOC_LOG] = d.[DOC_LOG] AND iq.[DOC_SER] = d.[DOC_SER] AND iq.[DOC_NBR] = d.[DOC_NBR]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND EXISTS(SELECT count(*)
			FROM inserted i
			JOIN [IPASPROD].[IP_USERDOC] ud ON i.[DOC_ORI] = ud.[DOC_ORI] AND i.[DOC_LOG] = ud.[DOC_LOG] AND i.[DOC_SER] = ud.[DOC_SER] AND i.[DOC_NBR] = ud.[DOC_NBR]
			UNION
			SELECT count(*)
			FROM deleted d
			JOIN [IPASPROD].[IP_USERDOC] ud ON d.[DOC_ORI] = ud.[DOC_ORI] AND d.[DOC_LOG] = ud.[DOC_LOG] AND d.[DOC_SER] = ud.[DOC_SER] AND d.[DOC_NBR] = ud.[DOC_NBR])
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			GETDATE(),
			@Activity,
			@TriggerName
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
			@Activity,
			@TriggerName
		FROM
			deleted;
	END
END
;
