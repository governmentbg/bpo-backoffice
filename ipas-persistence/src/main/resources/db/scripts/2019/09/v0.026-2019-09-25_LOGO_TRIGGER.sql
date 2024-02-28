--liquibase formatted sql

--changeset vnikolov:26.1 splitStatements:false
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



/*************************************************** [IPASPROD].[[MARK_LOGO_INDEX_AFTER]] ********************************************************************/
CREATE TRIGGER [IPASPROD].[MARK_LOGO_INDEX_AFTER]
   ON [IPASPROD].[IP_LOGO]
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
END
;

ALTER TABLE [IPASPROD].[IP_LOGO] ENABLE TRIGGER [MARK_LOGO_INDEX_AFTER]
;


