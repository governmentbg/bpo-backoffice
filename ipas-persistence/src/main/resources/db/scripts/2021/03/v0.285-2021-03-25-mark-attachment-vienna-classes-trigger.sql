--liquibase formatted sql

--changeset ggeorgiev:285.1

alter table ext_core.INDEX_QUEUE add ATTACHMENT_ID numeric(9, 0);

--changeset ggeorgiev:285.2 splitStatements:false

CREATE TRIGGER [EXT_CORE].[MARK_ATTACHMENT_VIENNA_CLASSES_INDEX_AFTER]
    ON [EXT_CORE].[IP_MARK_ATTACHMENT_VIENNA_CLASSES]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @IndexActivity  NVARCHAR (1);
    DECLARE @DeletActivity  NVARCHAR (1);
    DECLARE @Type  NVARCHAR (2);
    DECLARE @TriggerName  NVARCHAR (50);

    SET @Type = '15';
    SET @TriggerName = '[EXT_CORE].[MARK_ATTACHMENT_VIENNA_CLASSES_INDEX_AFTER]';


    IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
        AND (SELECT count(*) FROM (
                                      SELECT [FILE_SEQ]
                                           ,[FILE_TYP]
                                           ,[FILE_SER]
                                           ,[FILE_NBR]
                                           ,[VIENNA_CLASS_CODE]
                                           ,[VIENNA_GROUP_CODE]
                                           ,[VIENNA_ELEM_CODE]
                                           ,[ATTACHMENT_ID]
                                      FROM inserted
                                      UNION
                                      SELECT [FILE_SEQ]
                                           ,[FILE_TYP]
                                           ,[FILE_SER]
                                           ,[FILE_NBR]
                                           ,[VIENNA_CLASS_CODE]
                                           ,[VIENNA_GROUP_CODE]
                                           ,[VIENNA_ELEM_CODE]
                                           ,[ATTACHMENT_ID]
                                      FROM deleted) u ) > 1
        BEGIN
            SET @IndexActivity = 'I';
            SET @DeletActivity = 'D';
        END


    IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
        AND (SELECT count(*) FROM (
                                      SELECT [FILE_SEQ]
                                           ,[FILE_TYP]
                                           ,[FILE_SER]
                                           ,[FILE_NBR]
                                           ,[VIENNA_CLASS_CODE]
                                           ,[VIENNA_GROUP_CODE]
                                           ,[VIENNA_ELEM_CODE]
                                           ,[ATTACHMENT_ID]
                                      FROM inserted
                                      UNION
                                      SELECT [FILE_SEQ]
                                           ,[FILE_TYP]
                                           ,[FILE_SER]
                                           ,[FILE_NBR]
                                           ,[VIENNA_CLASS_CODE]
                                           ,[VIENNA_GROUP_CODE]
                                           ,[VIENNA_ELEM_CODE]
                                           ,[ATTACHMENT_ID]
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
                            JOIN inserted i ON
                               iq.[FILE_SEQ] = i.[FILE_SEQ] AND
                               iq.[FILE_TYP] = i.[FILE_TYP] AND
                               iq.[FILE_SER] = i.[FILE_SER] AND
                               iq.[FILE_NBR] = i.[FILE_NBR] AND
                               iq.[VIENNA_CLASS_CODE] = i.[VIENNA_CLASS_CODE] AND
                               iq.[VIENNA_GROUP_CODE] = i.[VIENNA_GROUP_CODE] AND
                               iq.[VIENNA_ELEM_CODE] = i.[VIENNA_ELEM_CODE] AND
                               iq.[ATTACHMENT_ID] = i.[ATTACHMENT_ID]
                   WHERE iq.[TYPE] = @Type
                     AND iq.[OPERATION] = @IndexActivity
                     AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON
                                   iq.[FILE_SEQ] = d.[FILE_SEQ] AND
                                   iq.[FILE_TYP] = d.[FILE_TYP] AND
                                   iq.[FILE_SER] = d.[FILE_SER] AND
                                   iq.[FILE_NBR] = d.[FILE_NBR] AND
                                   iq.[VIENNA_CLASS_CODE] = d.[VIENNA_CLASS_CODE] AND
                                   iq.[VIENNA_GROUP_CODE] = d.[VIENNA_GROUP_CODE] AND
                                   iq.[VIENNA_ELEM_CODE] = d.[VIENNA_ELEM_CODE] AND
                                   iq.[ATTACHMENT_ID] = d.[ATTACHMENT_ID]
                       WHERE iq.[TYPE] = @Type
                         AND iq.[OPERATION] = @DeletActivity
                         AND iq.[INDEXED_AT] is null)
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
                [ATTACHMENT_ID],
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
                [ATTACHMENT_ID],
                GETDATE(),
                @IndexActivity,
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
                [ATTACHMENT_ID],
                GETDATE(),
                @DeletActivity,
                @TriggerName
            FROM
                deleted;
        END
END;

--changeset ggeorgiev:285.3
ALTER TABLE [EXT_CORE].[IP_MARK_ATTACHMENT_VIENNA_CLASSES] ENABLE TRIGGER [MARK_ATTACHMENT_VIENNA_CLASSES_INDEX_AFTER]

