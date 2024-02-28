--liquibase formatted sql

--changeset vnikolov:143.1
ALTER TABLE [EXT_CORE].[INDEX_QUEUE]
ADD ROLE varchar(50);
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
12 - userdocPerson

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

--changeset vnikolov:143.2
IF OBJECT_ID ('IP_DOC_FILES_INDEX_AFTER', 'TR') IS NOT NULL
   DROP TRIGGER [IPASPROD].[IP_DOC_FILES_INDEX_AFTER];

--changeset vnikolov:143.3  splitStatements:false
CREATE TRIGGER [IPASPROD].[IP_DOC_FILES_INDEX_AFTER]
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
        '1',
        i.FILE_SEQ,
        i.FILE_TYP,
        i.FILE_SER,
        i.FILE_NBR,
        GETDATE(),
        @Activity,
		@TriggerName
          FROM inserted i
		  JOIN [IPASPROD].[IP_PATENT] p ON p.[FILE_SEQ] = i.[FILE_SEQ] AND p.[FILE_TYP] = i.[FILE_TYP] AND p.[FILE_SER] = i.[FILE_SER] AND p.[FILE_NBR] = i.[FILE_NBR]
	UNION
	SELECT
        '2',
        i.FILE_SEQ,
        i.FILE_TYP,
        i.FILE_SER,
        i.FILE_NBR,
        GETDATE(),
        @Activity,
		@TriggerName
          FROM inserted i
		  JOIN [IPASPROD].[IP_MARK] m ON m.[FILE_SEQ] = i.[FILE_SEQ] AND m.[FILE_TYP] = i.[FILE_TYP] AND m.[FILE_SER] = i.[FILE_SER] AND m.[FILE_NBR] = i.[FILE_NBR]
    UNION
    SELECT
        '1',
        d.FILE_SEQ,
        d.FILE_TYP,
        d.FILE_SER,
        d.FILE_NBR,
        GETDATE(),
        @Activity,
		@TriggerName
          FROM deleted d
		  JOIN [IPASPROD].[IP_PATENT] p ON p.[FILE_SEQ] = d.[FILE_SEQ] AND p.[FILE_TYP] = d.[FILE_TYP] AND p.[FILE_SER] = d.[FILE_SER] AND p.[FILE_NBR] = d.[FILE_NBR]
    UNION
    SELECT
        '2',
        d.FILE_SEQ,
        d.FILE_TYP,
        d.FILE_SER,
        d.FILE_NBR,
        GETDATE(),
        @Activity,
		@TriggerName
          FROM deleted d
		  JOIN [IPASPROD].[IP_MARK] m ON m.[FILE_SEQ] = d.[FILE_SEQ] AND m.[FILE_TYP] = d.[FILE_TYP] AND m.[FILE_SER] = d.[FILE_SER] AND m.[FILE_NBR] = d.[FILE_NBR]
END
;
/*************************************************** [IPASPROD].[IP_DOC_INDEX_AFTER] ********************************************************************/
--changeset vnikolov:143.4 splitStatements:false
IF OBJECT_ID ('IP_USERDOC_PERSON_INDEX_AFTER', 'TR') IS NOT NULL
   DROP TRIGGER [EXT_CORE].[IP_USERDOC_PERSON_INDEX_AFTER];

--changeset vnikolov:143.5 splitStatements:false
/*************************************************** [IPASPROD].[IP_USERDOC_INDEX_AFTER] ********************************************************************/
CREATE TRIGGER [EXT_CORE].[IP_USERDOC_PERSON_INDEX_AFTER]
   ON  [EXT_CORE].[IP_USERDOC_PERSON]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

    DECLARE @Activity  NVARCHAR (1)
	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '12';
	SET @TriggerName = '[EXT_CORE].[IP_USERDOC_PERSON_INDEX_AFTER]';

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
			JOIN inserted i ON iq.[DOC_ORI] = i.[DOC_ORI]
			    AND iq.[DOC_LOG] = i.[DOC_LOG]
			    AND iq.[DOC_SER] = i.[DOC_SER]
			    AND iq.[DOC_NBR] = i.[DOC_NBR]
			    AND iq.[PERSON_NBR] = i.[PERSON_NBR]
			    AND iq.[ADDR_NBR] = i.[ADDR_NBR]
			    AND iq.[ROLE] = i.[ROLE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[DOC_ORI] = d.[DOC_ORI]
                AND iq.[DOC_LOG] = d.[DOC_LOG]
                AND iq.[DOC_SER] = d.[DOC_SER]
                AND iq.[DOC_NBR] = d.[DOC_NBR]
			    AND iq.[PERSON_NBR] = d.[PERSON_NBR]
			    AND iq.[ADDR_NBR] = d.[ADDR_NBR]
			    AND iq.[ROLE] = d.[ROLE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @Activity AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			DOC_ORI,
			DOC_LOG,
			DOC_SER,
			DOC_NBR,
			PERSON_NBR,
			ADDR_NBR,
			ROLE,
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
			PERSON_NBR,
			ADDR_NBR,
			ROLE,
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
			PERSON_NBR,
			ADDR_NBR,
			ROLE,
			GETDATE(),
			@Activity,
			@TriggerName
		FROM
			deleted;
	END

END
;