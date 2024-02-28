--changeset vnikolov:166.1 splitStatements:false
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
                            JOIN inserted i ON iq.[PROC_NBR] = i.ACTION_PROC_NBR AND iq.[PROC_TYP] = i.ACTION_PROC_TYPE AND iq.[ACTION_NBR] = i.ACTION_ACTION_NBR
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'I' AND iq.[INDEXED_AT] is null )
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN deleted d ON iq.[PROC_NBR] = d.ACTION_PROC_NBR	AND iq.[PROC_TYP] = d.ACTION_PROC_TYPE AND iq.[ACTION_NBR] = d.ACTION_ACTION_NBR
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = 'D' AND iq.[INDEXED_AT] is null )
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
			WHERE ACTION_PROC_NBR IS NOT NULL AND ACTION_PROC_TYPE IS NOT NULL AND ACTION_ACTION_NBR IS NOT NULL
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
                deleted
			WHERE ACTION_PROC_NBR IS NOT NULL AND ACTION_PROC_TYPE IS NOT NULL AND ACTION_ACTION_NBR IS NOT NULL;
        END

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[PROC_NBR] = i.ACTIONUDOC_PROC_NBR AND  iq.[PROC_TYP] = i.ACTIONUDOC_PROC_TYPE AND iq.[ACTION_NBR] = i.ACTIONUDOC_ACTION_NBR
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
			WHERE ACTIONUDOC_PROC_NBR IS NOT NULL AND ACTIONUDOC_PROC_TYPE IS NOT NULL AND ACTIONUDOC_ACTION_NBR IS NOT NULL
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
                deleted
			WHERE ACTIONUDOC_PROC_NBR IS NOT NULL AND ACTIONUDOC_PROC_TYPE IS NOT NULL AND ACTIONUDOC_ACTION_NBR IS NOT NULL;
        END
END
