

ALTER TRIGGER [IPASPROD].[PROC_INDEX_AFTER]
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
	END
END


