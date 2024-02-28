

ALTER TRIGGER [IPASPROD].[IP_DOC_FILES_INDEX_AFTER]
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
            ELSE '99'
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
        @Activity
          FROM deleted

END


