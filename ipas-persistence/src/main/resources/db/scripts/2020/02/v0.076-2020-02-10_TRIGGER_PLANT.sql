--liquibase formatted sql

--changeset vnikolov:76.1 splitStatements:false
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
*/

/*************************************************** [EXT_CORE].[PLANT] ********************************************************************/
CREATE TRIGGER [EXT_CORE].[PLANT_INDEX_AFTER]
   ON [EXT_CORE].[PLANT]
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

ALTER TABLE [EXT_CORE].[PLANT] ENABLE TRIGGER [PLANT_INDEX_AFTER];

--changeset vnikolov:76.2 splitStatements:false
/*************************************************** [EXT_CORE].[PLANT_TAXON_NOMENCLATURE] ********************************************************************/
CREATE TRIGGER [EXT_CORE].[PLANT_TAXON_NOMENCLATURE_INDEX_AFTER]
   ON [EXT_CORE].[PLANT_TAXON_NOMENCLATURE]
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
			OPERATION
		)
		SELECT
			@Type,
			p.FILE_SEQ,
			p.FILE_TYP,
			p.FILE_SER,
			p.FILE_NBR,
			GETDATE(),
			@Activity
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
			@Activity
		FROM
			deleted d
			JOIN [EXT_CORE].[PLANT] p ON p.[PLANT_NUMENCLATURE_ID] = d.[ID] ;
	END
END;

ALTER TABLE [EXT_CORE].[PLANT_TAXON_NOMENCLATURE] ENABLE TRIGGER [PLANT_TAXON_NOMENCLATURE_INDEX_AFTER];

