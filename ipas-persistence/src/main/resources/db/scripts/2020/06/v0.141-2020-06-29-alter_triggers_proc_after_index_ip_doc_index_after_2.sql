--liquibase formatted sql

--changeset vnikolov:114.1 splitStatements:false
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
			DOC_NBR,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
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
END;

--changeset vnikolov:114.2
/*************************************************** [IPASPROD].[IP_DOC_INDEX_AFTER] ********************************************************************/
IF OBJECT_ID ('IP_DOC_INDEX_AFTER', 'TR') IS NOT NULL
   DROP TRIGGER [IPASPROD].[IP_DOC_INDEX_AFTER];

--changeset vnikolov:114.3 splitStatements:false
CREATE TRIGGER [IPASPROD].[IP_DOC_INDEX_AFTER]
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
			i.DOC_ORI,
			i.DOC_LOG,
			i.DOC_SER,
			i.DOC_NBR,
			GETDATE(),
			'U',
			@TriggerName
		FROM
			inserted i
			JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = i.DOC_NBR AND ud.DOC_ORI = i.DOC_ORI AND ud.DOC_LOG = i.DOC_LOG AND ud.DOC_SER = i.DOC_SER
		UNION
		SELECT
			'11',
			d.DOC_ORI,
			d.DOC_LOG,
			d.DOC_SER,
			d.DOC_NBR,
			GETDATE(),
			'U',
			@TriggerName
		FROM
			deleted d
			JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = d.DOC_NBR AND ud.DOC_ORI = d.DOC_ORI AND ud.DOC_LOG = d.DOC_LOG AND ud.DOC_SER = d.DOC_SER;
	END
END;

ALTER TABLE [IPASPROD].[IP_DOC] ENABLE TRIGGER [IP_DOC_INDEX_AFTER];

/*************************************************** Change DB version ********************************************************************/
