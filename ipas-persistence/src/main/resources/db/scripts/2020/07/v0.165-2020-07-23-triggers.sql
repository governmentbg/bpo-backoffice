--liquibase formatted sql

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
13 - locarno classes
14 - IpFileRelationships

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

--changeset vnikolov:165.1 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[ACTION_INDEX_AFTER]
    ON [IPASPROD].[IP_ACTION]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '5';
    SET @TriggerName = '[IPASPROD].[ACTION_INDEX_AFTER]';


    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[PROC_NBR] = i.[PROC_NBR]	AND  iq.[PROC_TYP] = i.[PROC_TYP] AND iq.[ACTION_NBR] = i.[ACTION_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[PROC_NBR] = d.[PROC_NBR]	AND  iq.[PROC_TYP] = d.[PROC_TYP] AND iq.[ACTION_NBR] = d.[ACTION_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.2 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[FILE_INDEX_AFTER]
   ON [IPASPROD].[IP_FILE]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @TriggerName  NVARCHAR (50)
	SET @TriggerName = '[IPASPROD].[FILE_INDEX_AFTER]';

	IF NOT EXISTS (SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ]
			    AND iq.[FILE_TYP] = i.[FILE_TYP]
			    AND iq.[FILE_SER] = i.[FILE_SER]
			    AND iq.[FILE_NBR] = i.[FILE_NBR]
			WHERE (iq.[TYPE] = '1' OR iq.[TYPE] = '2') AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ]
			    AND iq.[FILE_TYP] = d.[FILE_TYP]
			    AND iq.[FILE_SER] = d.[FILE_SER]
			    AND iq.[FILE_NBR] = d.[FILE_NBR]
			WHERE (iq.[TYPE] = '1' OR iq.[TYPE] = '2') AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
		AND EXISTS (
		    SELECT TOP (1) m.FILE_NBR
			    FROM [IPASPROD].[IP_MARK] m
			    JOIN inserted i ON m.[FILE_SEQ] = i.[FILE_SEQ]
			        AND m.[FILE_TYP] = i.[FILE_TYP]
			        AND m.[FILE_SER] = i.[FILE_SER]
			        AND m.[FILE_NBR] = i.[FILE_NBR]
			UNION
			SELECT TOP (1) p.FILE_NBR
			    FROM [IPASPROD].[IP_PATENT] p
			    JOIN inserted i ON p.[FILE_SEQ] = i.[FILE_SEQ]
			        AND p.[FILE_TYP] = i.[FILE_TYP]
			        AND p.[FILE_SER] = i.[FILE_SER]
			        AND p.[FILE_NBR] = i.[FILE_NBR]
			        )
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
			'1',
			ii.FILE_SEQ,
			ii.FILE_TYP,
			ii.FILE_SER,
			ii.FILE_NBR,
			GETDATE(),
			'I',
			@TriggerName
		FROM
			inserted ii
			JOIN [IPASPROD].[IP_PATENT] p ON p.[FILE_SEQ] = ii.[FILE_SEQ]
			AND p.[FILE_TYP] = ii.[FILE_TYP]
			AND p.[FILE_SER] = ii.[FILE_SER]
			AND p.[FILE_NBR] = ii.[FILE_NBR]
	    UNION
		SELECT
			'2',
			ii.FILE_SEQ,
			ii.FILE_TYP,
			ii.FILE_SER,
			ii.FILE_NBR,
			GETDATE(),
			'I',
			@TriggerName
		FROM
			inserted ii
			JOIN [IPASPROD].[IP_MARK] m ON m.[FILE_SEQ] = ii.[FILE_SEQ]
			AND m.[FILE_TYP] = ii.[FILE_TYP]
			AND m.[FILE_SER] = ii.[FILE_SER]
			AND m.[FILE_NBR] = ii.[FILE_NBR]
		UNION
		SELECT
			'1',
			dd.FILE_SEQ,
			dd.FILE_TYP,
			dd.FILE_SER,
			dd.FILE_NBR,
			GETDATE(),
			'D',
			@TriggerName
		FROM
			deleted dd
		    JOIN [IPASPROD].[IP_PATENT] p ON p.[FILE_SEQ] = dd.[FILE_SEQ]
		    AND p.[FILE_TYP] = dd.[FILE_TYP]
		    AND p.[FILE_SER] = dd.[FILE_SER]
		    AND p.[FILE_NBR] = dd.[FILE_NBR]
        UNION
		SELECT
			'2',
			dd.FILE_SEQ,
			dd.FILE_TYP,
			dd.FILE_SER,
			dd.FILE_NBR,
			GETDATE(),
			'D',
			@TriggerName
		FROM
			deleted dd
		  JOIN [IPASPROD].[IP_MARK] m ON m.[FILE_SEQ] = dd.[FILE_SEQ]
		  AND m.[FILE_TYP] = dd.[FILE_TYP]
		  AND m.[FILE_SER] = dd.[FILE_SER]
		  AND m.[FILE_NBR] = dd.[FILE_NBR];
	END
END

--changeset vnikolov:165.3 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[IP_DOC_FILES_INDEX_AFTER]
   ON [IPASPROD].[IP_DOC_FILES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	DECLARE @TriggerName  NVARCHAR (50)
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
        'I',
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
        'I',
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
        'I',
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
        'I',
		@TriggerName
          FROM deleted d
		  JOIN [IPASPROD].[IP_MARK] m ON m.[FILE_SEQ] = d.[FILE_SEQ] AND m.[FILE_TYP] = d.[FILE_TYP] AND m.[FILE_SER] = d.[FILE_SER] AND m.[FILE_NBR] = d.[FILE_NBR]
END

--changeset vnikolov:165.4 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[IP_DOC_INDEX_AFTER]
    ON [IPASPROD].[IP_DOC]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '10';
    SET @TriggerName = '[IPASPROD].[IP_DOC_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[DOC_ORI] = i.[DOC_ORI] AND iq.[DOC_LOG] = i.[DOC_LOG] AND iq.[DOC_SER] = i.[DOC_SER] AND iq.[DOC_NBR] = i.[DOC_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[DOC_ORI] = d.[DOC_ORI] AND iq.[DOC_LOG] = d.[DOC_LOG] AND iq.[DOC_SER] = d.[DOC_SER] AND iq.[DOC_NBR] = d.[DOC_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;

            /***** IP_USERDOC ******/
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
                'I',
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
                'I',
                @TriggerName
            FROM
                deleted d
                    JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = d.DOC_NBR AND ud.DOC_ORI = d.DOC_ORI AND ud.DOC_LOG = d.DOC_LOG AND ud.DOC_SER = d.DOC_SER;
        END
END

--changeset vnikolov:165.5 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[IP_FILE_RELATIONSHIP_INDEX_AFTER]
   ON [IPASPROD].[IP_FILE_RELATIONSHIP]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '14';
	SET @TriggerName = '[IPASPROD].[IP_FILE_RELATIONSHIP_INDEX_AFTER]';

	IF NOT EXISTS (SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ1]
			    AND iq.[FILE_TYP] = i.[FILE_TYP1]
			    AND iq.[FILE_SER] = i.[FILE_SER1]
			    AND iq.[FILE_NBR] = i.[FILE_NBR1]
			    AND iq.[FILE_SEQ2] = i.[FILE_SEQ2]
			    AND iq.[FILE_TYP2] = i.[FILE_TYP2]
			    AND iq.[FILE_SER2] = i.[FILE_SER2]
			    AND iq.[FILE_NBR2] = i.[FILE_NBR2]
			    AND iq.[RELATIONSHIP_TYP] = i.[RELATIONSHIP_TYP]
			    WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ1]
			    AND iq.[FILE_TYP] = d.[FILE_TYP1]
			    AND iq.[FILE_SER] = d.[FILE_SER1]
			    AND iq.[FILE_NBR] = d.[FILE_NBR1]
			    AND iq.[FILE_SEQ2] = d.[FILE_SEQ2]
			    AND iq.[FILE_TYP2] = d.[FILE_TYP2]
			    AND iq.[FILE_SER2] = d.[FILE_SER2]
			    AND iq.[FILE_NBR2] = d.[FILE_NBR2]
			    AND iq.[RELATIONSHIP_TYP] = d.[RELATIONSHIP_TYP]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			FILE_SEQ2,
			FILE_TYP2,
			FILE_SER2,
			FILE_NBR2,
			RELATIONSHIP_TYP,
			INSERTED_AT,
			OPERATION,
			TRIGGER_NAME
		)
		SELECT
			@Type,
			FILE_SEQ1,
			FILE_TYP1,
			FILE_SER1,
			FILE_NBR1,
			FILE_SEQ2,
			FILE_TYP2,
			FILE_SER2,
			FILE_NBR2,
			RELATIONSHIP_TYP,
			GETDATE(),
			'I',
			@TriggerName
		FROM
			inserted
		UNION
		SELECT
			@Type,
			FILE_SEQ1,
			FILE_TYP1,
			FILE_SER1,
			FILE_NBR1,
			FILE_SEQ2,
			FILE_TYP2,
			FILE_SER2,
			FILE_NBR2,
			RELATIONSHIP_TYP,
			GETDATE(),
			'D',
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:165.6 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[IP_PATENT_LOCARNO_CLASSES_INDEX_AFTER]
   ON [IPASPROD].[IP_PATENT_LOCARNO_CLASSES]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '13';
	SET @TriggerName = '[IPASPROD].[IP_PATENT_LOCARNO_CLASSES_INDEX_AFTER]';

	IF NOT EXISTS (SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ]
			    AND iq.[FILE_TYP] = i.[FILE_TYP]
			    AND iq.[FILE_SER] = i.[FILE_SER]
			    AND iq.[FILE_NBR] = i.[FILE_NBR]
			    AND iq.[LOCARNO_CLASS_CODE] = i.[LOCARNO_CLASS_CODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ]
			    AND iq.[FILE_TYP] = d.[FILE_TYP]
			    AND iq.[FILE_SER] = d.[FILE_SER]
			    AND iq.[FILE_NBR] = d.[FILE_NBR]
			    AND iq.[LOCARNO_CLASS_CODE] = d.[LOCARNO_CLASS_CODE]
			WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
	BEGIN
		INSERT INTO [EXT_CORE].[INDEX_QUEUE](
			TYPE,
			FILE_SEQ,
			FILE_TYP,
			FILE_SER,
			FILE_NBR,
			LOCARNO_CLASS_CODE,
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
			LOCARNO_CLASS_CODE,
			GETDATE(),
			'I',
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
			LOCARNO_CLASS_CODE,
			GETDATE(),
			'D',
			@TriggerName
		FROM
			deleted;
	END
END
;

--changeset vnikolov:165.7 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[IP_USERDOC_INDEX_AFTER]
    ON  [IPASPROD].[IP_USERDOC]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '11';
    SET @TriggerName = '[IPASPROD].[IP_USERDOC_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[DOC_ORI] = i.[DOC_ORI] AND iq.[DOC_LOG] = i.[DOC_LOG] AND iq.[DOC_SER] = i.[DOC_SER] AND iq.[DOC_NBR] = i.[DOC_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[DOC_ORI] = d.[DOC_ORI] AND iq.[DOC_LOG] = d.[DOC_LOG] AND iq.[DOC_SER] = d.[DOC_SER] AND iq.[DOC_NBR] = d.[DOC_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.8 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [EXT_CORE].[IP_USERDOC_PERSON_INDEX_AFTER]
   ON  [EXT_CORE].[IP_USERDOC_PERSON]
   AFTER INSERT, UPDATE, DELETE
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @Type  NVARCHAR (2)
	DECLARE @TriggerName  NVARCHAR (50)
	SET @Type = '12';
	SET @TriggerName = '[EXT_CORE].[IP_USERDOC_PERSON_INDEX_AFTER]';

	IF NOT EXISTS (SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN inserted i ON iq.[DOC_ORI] = i.[DOC_ORI]
			    AND iq.[DOC_LOG] = i.[DOC_LOG]
			    AND iq.[DOC_SER] = i.[DOC_SER]
			    AND iq.[DOC_NBR] = i.[DOC_NBR]
			    AND iq.[PERSON_NBR] = i.[PERSON_NBR]
			    AND iq.[ADDR_NBR] = i.[ADDR_NBR]
			    AND iq.[ROLE] = i.[ROLE]
			WHERE iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
		AND NOT EXISTS(SELECT *
			FROM [EXT_CORE].[INDEX_QUEUE] iq
			JOIN deleted d ON iq.[DOC_ORI] = d.[DOC_ORI]
                AND iq.[DOC_LOG] = d.[DOC_LOG]
                AND iq.[DOC_SER] = d.[DOC_SER]
                AND iq.[DOC_NBR] = d.[DOC_NBR]
			    AND iq.[PERSON_NBR] = d.[PERSON_NBR]
			    AND iq.[ADDR_NBR] = d.[ADDR_NBR]
			    AND iq.[ROLE] = d.[ROLE]
			WHERE iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
			'I',
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
			'D',
			@TriggerName
		FROM
			deleted;
	END

END

--changeset vnikolov:165.9 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[IP_USERDOC_TYPES_INDEX_AFTER]
    ON  [IPASPROD].[IP_USERDOC_TYPES]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '11';
    SET @TriggerName = '[IPASPROD].[IP_USERDOC_TYPES_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[DOC_ORI] = i.[DOC_ORI] AND iq.[DOC_LOG] = i.[DOC_LOG] AND iq.[DOC_SER] = i.[DOC_SER] AND iq.[DOC_NBR] = i.[DOC_NBR]
                   WHERE iq.[TYPE] = @Type AND (iq.[OPERATION] = 'I' OR iq.[OPERATION] = 'I' ) AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[DOC_ORI] = d.[DOC_ORI] AND iq.[DOC_LOG] = d.[DOC_LOG] AND iq.[DOC_SER] = d.[DOC_SER] AND iq.[DOC_NBR] = d.[DOC_NBR]
                       WHERE iq.[TYPE] = @Type AND (iq.[OPERATION] = 'I' OR iq.[OPERATION] = 'I' ) AND iq.[INDEXED_AT] is null)
        AND EXISTS(SELECT TOP (1) i.*
                   FROM inserted i
                            JOIN [IPASPROD].[IP_USERDOC] ud ON i.[DOC_ORI] = ud.[DOC_ORI] AND i.[DOC_LOG] = ud.[DOC_LOG] AND i.[DOC_SER] = ud.[DOC_SER] AND i.[DOC_NBR] = ud.[DOC_NBR]
                   UNION
                   SELECT TOP (1) d.*
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
                'I',
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
                'I',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.10 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [EXT_JOURNAL].[JOURNAL_ELEMENT_INDEX_AFTER]
    ON [EXT_JOURNAL].[JOURNAL_ELEMENT]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '5';
    SET @TriggerName = '[IPASPROD].[JOURNAL_ELEMENT_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[PROC_NBR] = i.ACTION_PROC_NBR	AND  iq.[PROC_TYP] = i.ACTION_PROC_TYPE AND iq.[ACTION_NBR] = i.ACTION_ACTION_NBR
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[PROC_NBR] = d.ACTION_PROC_NBR	AND  iq.[PROC_TYP] = d.ACTION_PROC_TYPE AND iq.[ACTION_NBR] = d.ACTION_ACTION_NBR
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END


    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[PROC_NBR] = i.ACTIONUDOC_PROC_NBR	AND  iq.[PROC_TYP] = i.ACTIONUDOC_PROC_TYPE AND iq.[ACTION_NBR] = i.ACTIONUDOC_ACTION_NBR
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[PROC_NBR] = d.ACTIONUDOC_PROC_NBR	AND  iq.[PROC_TYP] = d.ACTIONUDOC_PROC_TYPE AND iq.[ACTION_NBR] = d.ACTIONUDOC_ACTION_NBR
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.11 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[LOGO_VIENNA_CLASSES_INDEX_AFTER]
    ON [IPASPROD].[IP_LOGO_VIENNA_CLASSES]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '6';
    SET @TriggerName = '[IPASPROD].[LOGO_VIENNA_CLASSES_INDEX_AFTER]';

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
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
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
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.12 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_INDEX_AFTER]
    ON [IPASPROD].[IP_MARK]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '2';
    SET @TriggerName = '[IPASPROD].[MARK_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.13 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_LOGO_INDEX_AFTER]
    ON [IPASPROD].[IP_LOGO]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (1)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '2';
    SET @TriggerName = '[IPASPROD].[MARK_LOGO_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.14 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_NICE_CLASSES_INDEX_AFTER]
    ON [IPASPROD].[IP_MARK_NICE_CLASSES]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '7';
    SET @TriggerName = '[IPASPROD].[MARK_NICE_CLASSES_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON
                               iq.[FILE_SEQ] = i.[FILE_SEQ] AND
                               iq.[FILE_TYP] = i.[FILE_TYP] AND
                               iq.[FILE_SER] = i.[FILE_SER] AND
                               iq.[FILE_NBR] = i.[FILE_NBR] AND
                               iq.[NICE_CLASS_CODE] = i.[NICE_CLASS_CODE] AND
                               iq.[NICE_CLASS_STATUS_WCODE] = i.[NICE_CLASS_STATUS_WCODE]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON
                                   iq.[FILE_SEQ] = d.[FILE_SEQ] AND
                                   iq.[FILE_TYP] = d.[FILE_TYP] AND
                                   iq.[FILE_SER] = d.[FILE_SER] AND
                                   iq.[FILE_NBR] = d.[FILE_NBR] AND
                                   iq.[NICE_CLASS_CODE] = d.[NICE_CLASS_CODE] AND
                                   iq.[NICE_CLASS_STATUS_WCODE] = d.[NICE_CLASS_STATUS_WCODE]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.15 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_OWNER_INDEX_AFTER]
    ON [IPASPROD].[IP_MARK_OWNERS]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '2';
    SET @TriggerName = '[IPASPROD].[MARK_OWNER_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'I',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.16 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[MARK_REPRS_INDEX_AFTER]
    ON [IPASPROD].[IP_MARK_REPRS]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '2';
    SET @TriggerName = '[IPASPROD].[MARK_REPRS_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'I',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.17 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_INDEX_AFTER]
    ON [IPASPROD].[IP_PATENT]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '1';
    SET @TriggerName = '[IPASPROD].[PATENT_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.18 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_INVENTOR_INDEX_AFTER]
    ON [IPASPROD].[IP_PATENT_INVENTORS]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '1';
    SET @TriggerName = '[IPASPROD].[PATENT_INVENTOR_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND EXISTS(SELECT TOP (1) i.*
                        FROM inserted i
                        JOIN [IPASPROD].[IP_PATENT] p ON i.[FILE_SEQ] = p.[FILE_SEQ] AND i.[FILE_TYP] = p.[FILE_TYP] AND i.[FILE_SER] = p.[FILE_SER] AND i.[FILE_NBR] = p.[FILE_NBR]
                  UNION
                  SELECT TOP (1) d.*
                        FROM deleted d
                        JOIN [IPASPROD].[IP_PATENT] p ON d.[FILE_SEQ] = p.[FILE_SEQ] AND d.[FILE_TYP] = p.[FILE_TYP] AND d.[FILE_SER] = p.[FILE_SER] AND d.[FILE_NBR] = p.[FILE_NBR]
                  )
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
                'I',
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
                'I',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.19 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_IPC_CLASSES_INDEX_AFTER]
    ON [IPASPROD].[IP_PATENT_IPC_CLASSES]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '8';
    SET @TriggerName = '[IPASPROD].[PATENT_IPC_CLASSES_INDEX_AFTER]';

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
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
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
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.20 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_OWNER_INDEX_AFTER]
    ON [IPASPROD].[IP_PATENT_OWNERS]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (1)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '1';
    SET @TriggerName = '[IPASPROD].[PATENT_OWNER_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND EXISTS(SELECT TOP (1) i.*
                FROM inserted i
                JOIN [IPASPROD].[IP_PATENT] p ON i.[FILE_SEQ] = p.[FILE_SEQ] AND i.[FILE_TYP] = p.[FILE_TYP] AND i.[FILE_SER] = p.[FILE_SER] AND i.[FILE_NBR] = p.[FILE_NBR]
          UNION
          SELECT TOP (1) d.*
                FROM deleted d
                JOIN [IPASPROD].[IP_PATENT] p ON d.[FILE_SEQ] = p.[FILE_SEQ] AND d.[FILE_TYP] = p.[FILE_TYP] AND d.[FILE_SER] = p.[FILE_SER] AND d.[FILE_NBR] = p.[FILE_NBR]
          )
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
                'I',
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
                'I',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.21 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_REPR_INDEX_AFTER]
    ON [IPASPROD].[IP_PATENT_REPRS]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '1';
    SET @TriggerName = '[IPASPROD].[PATENT_REPR_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND EXISTS(SELECT TOP (1) i.*
                    FROM inserted i
                    JOIN [IPASPROD].[IP_PATENT] p ON i.[FILE_SEQ] = p.[FILE_SEQ] AND i.[FILE_TYP] = p.[FILE_TYP] AND i.[FILE_SER] = p.[FILE_SER] AND i.[FILE_NBR] = p.[FILE_NBR]
              UNION
              SELECT TOP (1) d.*
                    FROM deleted d
                    JOIN [IPASPROD].[IP_PATENT] p ON d.[FILE_SEQ] = p.[FILE_SEQ] AND d.[FILE_TYP] = p.[FILE_TYP] AND d.[FILE_SER] = p.[FILE_SER] AND d.[FILE_NBR] = p.[FILE_NBR]
              )
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
                'I',
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
                'I',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.22 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[PATENT_SUMMARY_INDEX_AFTER]
    ON [IPASPROD].[IP_PATENT_SUMMARY]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '9';
    SET @TriggerName = '[IPASPROD].[PATENT_SUMMARY_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON
                               iq.[FILE_SEQ] = i.[FILE_SEQ] AND
                               iq.[FILE_TYP] = i.[FILE_TYP] AND
                               iq.[FILE_SER] = i.[FILE_SER] AND
                               iq.[FILE_NBR] = i.[FILE_NBR] AND
                               iq.[LANGUAGE_CODE] = i.[LANGUAGE_CODE]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON
                                   iq.[FILE_SEQ] = d.[FILE_SEQ] AND
                                   iq.[FILE_TYP] = d.[FILE_TYP] AND
                                   iq.[FILE_SER] = d.[FILE_SER] AND
                                   iq.[FILE_NBR] = d.[FILE_NBR] AND
                                   iq.[LANGUAGE_CODE] = d.[LANGUAGE_CODE]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.23 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[PERSON_ADDRESS_INDEX_AFTER]
    ON [IPASPROD].[IP_PERSON_ADDRESSES]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '3';
    SET @TriggerName = '[IPASPROD].[PERSON_ADDRESS_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[PERSON_NBR] = i.[PERSON_NBR] AND iq.[ADDR_NBR] = i.[ADDR_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[PERSON_NBR] = d.[PERSON_NBR] AND iq.[ADDR_NBR] = d.[ADDR_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
                @TriggerName
            FROM
                inserted
            UNION
            SELECT
                @Type,
                [PERSON_NBR],
                [ADDR_NBR],
                GETDATE(),
                'D',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.24 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[PERSON_INDEX_AFTER]
    ON [IPASPROD].[IP_PERSON]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '3';
    SET @TriggerName = '[IPASPROD].[PERSON_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[PERSON_NBR] = i.[PERSON_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[PERSON_NBR] = d.[PERSON_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
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
                'D',
                @TriggerName
            FROM
                deleted d
                    JOIN [IPASPROD].[IP_PERSON_ADDRESSES] pa ON d.[PERSON_NBR] = pa.[PERSON_NBR];
        END



    IF NOT EXISTS (SELECT TOP (10) SERVICE_PERSON_NBR, SERVICE_ADDR_NBR, per.PERSON_NBR
                   FROM inserted per
                        JOIN [IPASPROD].[IP_MARK] m ON m.SERVICE_PERSON_NBR = per.PERSON_NBR
                )
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
                   'I',
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
                   'I',
                   @TriggerName
            FROM deleted per
                     JOIN [IPASPROD].[IP_PATENT] pt ON pt.SERVICE_PERSON_NBR = per.PERSON_NBR
        END



    IF NOT EXISTS(SELECT TOP (10) SERVICE_PERSON_NBR, SERVICE_ADDR_NBR, per.PERSON_NBR
                  FROM deleted per
                           JOIN [IPASPROD].[IP_MARK] m ON m.SERVICE_PERSON_NBR = per.PERSON_NBR
                )
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
                   'I',
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
                   'I',
                   @TriggerName
            FROM deleted per
                     JOIN [IPASPROD].[IP_MARK] m ON m.SERVICE_PERSON_NBR = per.PERSON_NBR
        END
END

--changeset vnikolov:165.25 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [EXT_CORE].[PLANT_INDEX_AFTER]
    ON [EXT_CORE].[PLANT]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '1';
    SET @TriggerName = '[EXT_CORE].[PLANT_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ] AND iq.[FILE_TYP] = i.[FILE_TYP] AND iq.[FILE_SER] = i.[FILE_SER] AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ] AND iq.[FILE_TYP] = d.[FILE_TYP] AND iq.[FILE_SER] = d.[FILE_SER] AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND EXISTS(
			  SELECT TOP (1) p.[FILE_SEQ]
                    FROM inserted ii
                    JOIN [IPASPROD].[IP_PATENT] p ON ii.[FILE_SEQ] = p.[FILE_SEQ] AND ii.[FILE_TYP] = p.[FILE_TYP] AND ii.[FILE_SER] = p.[FILE_SER] AND ii.[FILE_NBR] = p.[FILE_NBR]
              UNION
              SELECT TOP (1) p.[FILE_SEQ]
                    FROM deleted dd
                    JOIN [IPASPROD].[IP_PATENT] p ON dd.[FILE_SEQ] = p.[FILE_SEQ] AND dd.[FILE_TYP] = p.[FILE_TYP] AND dd.[FILE_SER] = p.[FILE_SER] AND dd.[FILE_NBR] = p.[FILE_NBR]
              )
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
                'I',
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
                'I',
                @TriggerName
            FROM
                deleted;
        END
END

--changeset vnikolov:165.26 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [EXT_CORE].[PLANT_TAXON_NOMENCLATURE_INDEX_AFTER]
    ON [EXT_CORE].[PLANT_TAXON_NOMENCLATURE]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '1';
    SET @TriggerName = '[IPASPROD].[PLANT_TAXON_NOMENCLATURE_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN [EXT_CORE].[PLANT] p ON iq.[FILE_SEQ] = p.[FILE_SEQ] AND iq.[FILE_TYP] = p.[FILE_TYP] AND iq.[FILE_SER] = p.[FILE_SER] AND iq.[FILE_NBR] = p.[FILE_NBR]
                            JOIN inserted i ON p.[PLANT_NUMENCLATURE_ID] = i.[ID]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN [EXT_CORE].[PLANT] p ON iq.[FILE_SEQ] = p.[FILE_SEQ] AND iq.[FILE_TYP] = p.[FILE_TYP] AND iq.[FILE_SER] = p.[FILE_SER] AND iq.[FILE_NBR] = p.[FILE_NBR]
                                JOIN deleted d ON p.[PLANT_NUMENCLATURE_ID] = d.[ID]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND EXISTS(SELECT TOP (1) i.*
                    FROM inserted i
                    JOIN [EXT_CORE].[PLANT] p ON p.[PLANT_NUMENCLATURE_ID] = i.[ID]
                    JOIN [IPASPROD].[IP_PATENT] pt ON pt.[FILE_SEQ] = p.[FILE_SEQ] AND pt.[FILE_TYP] = p.[FILE_TYP] AND pt.[FILE_SER] = p.[FILE_SER] AND pt.[FILE_NBR] = p.[FILE_NBR]
              UNION
              SELECT TOP (1) d.*
                    FROM deleted d
                    JOIN [EXT_CORE].[PLANT] p ON p.[PLANT_NUMENCLATURE_ID] = d.[ID]
                    JOIN [IPASPROD].[IP_PATENT] pt ON pt.[FILE_SEQ] = p.[FILE_SEQ] AND pt.[FILE_TYP] = p.[FILE_TYP] AND pt.[FILE_SER] = p.[FILE_SER] AND pt.[FILE_NBR] = p.[FILE_NBR]
              )
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
                'I',
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
                'I',
                @TriggerName
            FROM
                deleted d
                    JOIN [EXT_CORE].[PLANT] p ON p.[PLANT_NUMENCLATURE_ID] = d.[ID] ;
        END
END

--changeset vnikolov:165.27 splitStatements:false
/***********************************************************************************************************************/
ALTER TRIGGER [IPASPROD].[PROC_INDEX_AFTER]
    ON [IPASPROD].[IP_PROC]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (2)
    DECLARE @TriggerName  NVARCHAR (50)
    SET @Type = '4';
    SET @TriggerName = '[IPASPROD].[PROC_INDEX_AFTER]';

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[PROC_NBR] = i.[PROC_NBR]	AND  iq.[PROC_TYP] = i.[PROC_TYP]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[PROC_NBR] = d.[PROC_NBR]	AND  iq.[PROC_TYP] = d.[PROC_TYP]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null)
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
                'I',
                @TriggerName
            FROM
                inserted
            UNION
            SELECT
                @Type,
                PROC_TYP,
                PROC_NBR,
                GETDATE(),
                'D',
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
            SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'I', @TriggerName
            FROM [IPASPROD].[IP_PROC] p
                     JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
                     JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
            WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
            UNION
            SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'I', @TriggerName
            FROM [IPASPROD].[IP_PROC] subp
                     JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
                     JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
                     JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
            WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
            UNION
            SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'I', @TriggerName
            FROM [IPASPROD].[IP_PROC] p
                     JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
                     JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
            WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
            UNION
            SELECT '11', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'I', @TriggerName
            FROM [IPASPROD].[IP_PROC] subp
                     JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
                     JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
                     JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
            WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
            UNION
            SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'I', @TriggerName
            FROM [IPASPROD].[IP_PROC] p
                     JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
                     JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
            WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
            UNION
            SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'I', @TriggerName
            FROM [IPASPROD].[IP_PROC] subp
                     JOIN [IPASPROD].[IP_PROC] p ON p.FILE_PROC_NBR = subp.PROC_NBR AND p.FILE_PROC_TYP = subp.PROC_TYP
                     JOIN inserted i ON i.[PROC_NBR] = p.[PROC_NBR] AND i.[PROC_TYP] = p.[PROC_TYP]
                     JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
            WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
            UNION
            SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'I', @TriggerName
            FROM [IPASPROD].[IP_PROC] p
                     JOIN deleted d ON d.[PROC_NBR] = p.[PROC_NBR] AND d.[PROC_TYP] = p.[PROC_TYP]
                     JOIN [IPASPROD].[IP_USERDOC] ud ON ud.DOC_NBR = p.DOC_NBR AND ud.DOC_ORI = p.DOC_ORI AND ud.DOC_LOG = p.DOC_LOG AND ud.DOC_SER = p.DOC_SER
            WHERE p.DOC_NBR is not null AND p.DOC_ORI is not null AND p.DOC_LOG is not null AND p.DOC_SER is not null
            UNION
            SELECT '10', p.DOC_NBR, p.DOC_ORI, p.DOC_LOG, p.DOC_SER, GETDATE(), 'I', @TriggerName
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
                        'I',
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
                        'I',
                        @TriggerName
                    FROM
                        deleted;
                END
        END
END