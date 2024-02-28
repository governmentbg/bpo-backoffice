--liquibase formatted sql

--changeset vnikolov:92.1
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ALTER COLUMN TYPE varchar(2);

--changeset vnikolov:92.2 splitStatements:false
ALTER TRIGGER [IPASPROD].[IP_DOC_INDEX_AFTER]
   ON [IPASPROD].[IP_DOC]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
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
END

ALTER TABLE [IPASPROD].[IP_DOC] ENABLE TRIGGER [IP_DOC_INDEX_AFTER];

