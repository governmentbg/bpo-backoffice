--liquibase formatted sql

--changeset vnikolov:235.1 splitStatements:false
ALTER TRIGGER [IPASPROD].[MARK_LOGO_INDEX_AFTER]
    ON [IPASPROD].[IP_LOGO]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Type  NVARCHAR (1);
    DECLARE @TriggerName  NVARCHAR (50);
    SET @Type = '2';
    SET @TriggerName = '[IPASPROD].[MARK_LOGO_INDEX_AFTER]';


    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[FILE_SEQ] = i.[FILE_SEQ]
                                AND iq.[FILE_TYP] = i.[FILE_TYP]
                                AND iq.[FILE_SER] = i.[FILE_SER]
                                AND iq.[FILE_NBR] = i.[FILE_NBR]
                   WHERE iq.[TYPE] = @Type
                        AND iq.[OPERATION] = 'I'
                        AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON iq.[FILE_SEQ] = d.[FILE_SEQ]
                                    AND iq.[FILE_TYP] = d.[FILE_TYP]
                                    AND iq.[FILE_SER] = d.[FILE_SER]
                                    AND iq.[FILE_NBR] = d.[FILE_NBR]
                       WHERE iq.[TYPE] = @Type
                            AND iq.[OPERATION] = 'I'
                            AND iq.[INDEXED_AT] is null)
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
                'I' ,
                @TriggerName
            FROM
                deleted;
        END
END