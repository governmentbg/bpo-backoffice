--liquibase formatted sql

--changeset vnikolov:234.1 splitStatements:false
ALTER TRIGGER [IPASPROD].[PERSON_INDEX_AFTER]
    ON [IPASPROD].[IP_PERSON]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

	DECLARE @IndexActivity  NVARCHAR (1);
	DECLARE @DeletActivity  NVARCHAR (1);
    DECLARE @Type  NVARCHAR (2);
    DECLARE @TriggerName  NVARCHAR (50);

    SET @Type = '3';
    SET @TriggerName = '[IPASPROD].[PERSON_INDEX_AFTER]';


	IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
		AND (SELECT count(*) FROM (
		        SELECT [PERSON_NBR]
                    FROM inserted
                UNION
                SELECT [PERSON_NBR]
                    FROM deleted) u ) > 1
	BEGIN
		SET @IndexActivity = 'I';
		SET @DeletActivity = 'D';
	END


	IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
		AND (SELECT count(*) FROM (
		        SELECT [PERSON_NBR]
                    FROM inserted
                UNION
                SELECT [PERSON_NBR]
                    FROM deleted) u ) = 1
	BEGIN
		SET @IndexActivity = 'I';
		SET @DeletActivity = 'I';
	END


	IF EXISTS (SELECT * FROM inserted) AND NOT EXISTS(SELECT * FROM deleted)
	BEGIN
		SET @IndexActivity = 'I';
		SET @DeletActivity = 'I';
	END


	IF EXISTS (SELECT * FROM deleted) AND NOT EXISTS(SELECT * FROM inserted)
	BEGIN
		SET @IndexActivity = 'D';
		SET @DeletActivity = 'D';
	END

    IF NOT EXISTS (SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN inserted i ON iq.[PERSON_NBR] = i.[PERSON_NBR]
                   WHERE iq.[TYPE] = @Type
                        AND iq.[OPERATION] = @IndexActivity
                        AND iq.[INDEXED_AT] is null)
        AND
        NOT EXISTS(SELECT *
                   FROM [EXT_CORE].[INDEX_QUEUE] iq
                            JOIN deleted d ON iq.[PERSON_NBR] = d.[PERSON_NBR]
                   WHERE iq.[TYPE] = @Type
                        AND iq.[OPERATION] = @DeletActivity
                        AND iq.[INDEXED_AT] is null)
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
                @IndexActivity,
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
                @DeletActivity,
                @TriggerName
            FROM
                deleted d
                    JOIN [IPASPROD].[IP_PERSON_ADDRESSES] pa ON d.[PERSON_NBR] = pa.[PERSON_NBR];
        END



    IF EXISTS (SELECT TOP (10) i.PERSON_NBR
                   FROM inserted i
                        JOIN [IPASPROD].[IP_PATENT] pt ON pt.MAIN_OWNER_PERSON_NBR = i.PERSON_NBR
					UNION
					SELECT TOP (10) d.PERSON_NBR
                   FROM deleted d
                        JOIN [IPASPROD].[IP_PATENT] pt ON pt.MAIN_OWNER_PERSON_NBR = d.PERSON_NBR
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
                     JOIN [IPASPROD].[IP_PATENT] pt ON pt.MAIN_OWNER_PERSON_NBR = per.PERSON_NBR
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
                     JOIN [IPASPROD].[IP_PATENT] pt ON pt.MAIN_OWNER_PERSON_NBR = per.PERSON_NBR
        END



    IF EXISTS(SELECT TOP (10) i.PERSON_NBR
                   FROM inserted i
                        JOIN [IPASPROD].[IP_MARK] m ON m.MAIN_OWNER_PERSON_NBR = i.PERSON_NBR
					UNION
				  SELECT TOP (10) d.PERSON_NBR
                   FROM deleted d
                        JOIN [IPASPROD].[IP_MARK] m ON m.MAIN_OWNER_PERSON_NBR = d.PERSON_NBR
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
            FROM inserted i
                JOIN [IPASPROD].[IP_MARK] m ON m.MAIN_OWNER_PERSON_NBR = i.PERSON_NBR
            UNION
            SELECT '2',
                   m.FILE_SEQ,
                   m.FILE_TYP,
                   m.FILE_SER,
                   m.FILE_NBR,
                   GETDATE(),
                   'I',
                   @TriggerName
            FROM deleted d
                JOIN [IPASPROD].[IP_MARK] m ON m.MAIN_OWNER_PERSON_NBR = d.PERSON_NBR
        END
END