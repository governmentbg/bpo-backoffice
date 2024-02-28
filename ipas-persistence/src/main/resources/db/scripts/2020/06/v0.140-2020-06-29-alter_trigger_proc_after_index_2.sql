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
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] subp
		  JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
		  JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
		  FROM [IPASPROD].[IP_PROC] p
		  JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
		  WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
		UNION
		SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'U', @TriggerName
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

/*************************************************** Change DB version ********************************************************************/

;