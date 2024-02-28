--liquibase formatted sql

--changeset vnikolov:75.1 splitStatements:false
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
			ADDR_NBR,
			INSERTED_AT,
			OPERATION
		)
		SELECT
			@Type,
			pa.[PERSON_NBR],
			pa.[ADDR_NBR],
			GETDATE(),
			@Activity
		FROM
			inserted i
        JOIN [IPASPROD].[IP_PERSON_ADDRESSES] pa ON i.[PERSON_NBR] = pa.[PERSON_NBR]
		UNION
		SELECT
			@Type,
			pa.[PERSON_NBR],
			pa.[ADDR_NBR],
			GETDATE(),
			@Activity
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

--changeset vnikolov:75.2 splitStatements:false

/*************************************************** [IPASPROD].[PERSON_ADDRESS_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [IPASPROD].[PERSON_ADDRESS_INDEX_AFTER]
   ON [IPASPROD].[IP_PERSON_ADDRESSES]
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
			OPERATION
		)
		SELECT
			@Type,
			[PERSON_NBR],
			[ADDR_NBR],
			GETDATE(),
			@Activity
		FROM
			inserted
		UNION
		SELECT
			@Type,
			[PERSON_NBR],
			[ADDR_NBR],
			GETDATE(),
			@Activity
		FROM
			deleted;
	END

END;

ALTER TABLE [IPASPROD].[IP_PERSON_ADDRESSES] ENABLE TRIGGER [PERSON_ADDRESS_INDEX_AFTER];

