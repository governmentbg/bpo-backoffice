--liquibase formatted sql

--changeset murlev:360.1
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD CPC_EDITION_CODE varchar(20);
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD CPC_SECTION_CODE varchar;
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD CPC_CLASS_CODE varchar(2);
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD CPC_SUBCLASS_CODE varchar(2);
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD CPC_GROUP_CODE varchar(10);
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD CPC_SUBGROUP_CODE varchar(10);
ALTER TABLE [EXT_CORE].[INDEX_QUEUE] ADD CPC_QUALIFICATION_CODE varchar(2);

--changeset murlev:360.2 splitStatements:false
CREATE TRIGGER [IPASPROD].[PATENT_CPC_CLASSES_INDEX_AFTER]
    ON [IPASPROD].[IP_PATENT_CPC_CLASSES]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

	DECLARE @IndexActivity  NVARCHAR (1);
	DECLARE @DeletActivity  NVARCHAR (1);
    DECLARE @Type  NVARCHAR (2);
    DECLARE @TriggerName  NVARCHAR (50);

    SET @Type = '16';
    SET @TriggerName = '[IPASPROD].[PATENT_CPC_CLASSES_INDEX_AFTER]';


	IF EXISTS (SELECT * FROM inserted) AND EXISTS (SELECT * FROM deleted)
		AND (SELECT count(*) FROM (
		        SELECT [FILE_SEQ]
                      ,[FILE_TYP]
                      ,[FILE_SER]
                      ,[FILE_NBR]
                      ,[CPC_EDITION_CODE]
                      ,[CPC_SECTION_CODE]
                      ,[CPC_CLASS_CODE]
                      ,[CPC_SUBCLASS_CODE]
                      ,[CPC_GROUP_CODE]
                      ,[CPC_SUBGROUP_CODE]
                      ,[CPC_QUALIFICATION_CODE]
                    FROM inserted
                UNION
                SELECT [FILE_SEQ]
                      ,[FILE_TYP]
                      ,[FILE_SER]
                      ,[FILE_NBR]
                      ,[CPC_EDITION_CODE]
                      ,[CPC_SECTION_CODE]
                      ,[CPC_CLASS_CODE]
                      ,[CPC_SUBCLASS_CODE]
                      ,[CPC_GROUP_CODE]
                      ,[CPC_SUBGROUP_CODE]
                      ,[CPC_QUALIFICATION_CODE]
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
                      ,[CPC_EDITION_CODE]
                      ,[CPC_SECTION_CODE]
                      ,[CPC_CLASS_CODE]
                      ,[CPC_SUBCLASS_CODE]
                      ,[CPC_GROUP_CODE]
                      ,[CPC_SUBGROUP_CODE]
                      ,[CPC_QUALIFICATION_CODE]
                    FROM inserted
                UNION
                SELECT [FILE_SEQ]
                      ,[FILE_TYP]
                      ,[FILE_SER]
                      ,[FILE_NBR]
                      ,[CPC_EDITION_CODE]
                      ,[CPC_SECTION_CODE]
                      ,[CPC_CLASS_CODE]
                      ,[CPC_SUBCLASS_CODE]
                      ,[CPC_GROUP_CODE]
                      ,[CPC_SUBGROUP_CODE]
                      ,[CPC_QUALIFICATION_CODE]
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
                               iq.[CPC_EDITION_CODE] = i.[CPC_EDITION_CODE] AND
                               iq.[CPC_SECTION_CODE] = i.[CPC_SECTION_CODE] AND
                               iq.[CPC_CLASS_CODE] = i.[CPC_CLASS_CODE] AND
                               iq.[CPC_SUBCLASS_CODE] = i.[CPC_SUBCLASS_CODE] AND
                               iq.[CPC_GROUP_CODE] = i.[CPC_GROUP_CODE] AND
                               iq.[CPC_SUBGROUP_CODE] = i.[CPC_SUBGROUP_CODE] AND
                               iq.[CPC_QUALIFICATION_CODE] = i.[CPC_QUALIFICATION_CODE]
                   WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @IndexActivity AND iq.[INDEXED_AT] is null)
        AND NOT EXISTS(SELECT *
                       FROM [EXT_CORE].[INDEX_QUEUE] iq
                                JOIN deleted d ON
                                   iq.[FILE_SEQ] = d.[FILE_SEQ] AND
                                   iq.[FILE_TYP] = d.[FILE_TYP] AND
                                   iq.[FILE_SER] = d.[FILE_SER] AND
                                   iq.[FILE_NBR] = d.[FILE_NBR] AND
                                   iq.[CPC_EDITION_CODE] = d.[CPC_EDITION_CODE] AND
                                   iq.[CPC_SECTION_CODE] = d.[CPC_SECTION_CODE] AND
                                   iq.[CPC_CLASS_CODE] = d.[CPC_CLASS_CODE] AND
                                   iq.[CPC_SUBCLASS_CODE] = d.[CPC_SUBCLASS_CODE] AND
                                   iq.[CPC_GROUP_CODE] = d.[CPC_GROUP_CODE] AND
                                   iq.[CPC_SUBGROUP_CODE] = d.[CPC_SUBGROUP_CODE] AND
                                   iq.[CPC_QUALIFICATION_CODE] = d.[CPC_QUALIFICATION_CODE]
                       WHERE iq.[TYPE] = @Type AND iq.[OPERATION] = @DeletActivity AND iq.[INDEXED_AT] is null)
BEGIN
INSERT INTO [EXT_CORE].[INDEX_QUEUE](
    TYPE,
    FILE_SEQ,
    FILE_TYP,
    FILE_SER,
    FILE_NBR,
    [CPC_EDITION_CODE],
    [CPC_SECTION_CODE],
    [CPC_CLASS_CODE],
    [CPC_SUBCLASS_CODE],
    [CPC_GROUP_CODE],
    [CPC_SUBGROUP_CODE],
[CPC_QUALIFICATION_CODE],
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
    [CPC_EDITION_CODE],
    [CPC_SECTION_CODE],
    [CPC_CLASS_CODE],
    [CPC_SUBCLASS_CODE],
    [CPC_GROUP_CODE],
    [CPC_SUBGROUP_CODE],
    [CPC_QUALIFICATION_CODE],
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
    [CPC_EDITION_CODE],
    [CPC_SECTION_CODE],
    [CPC_CLASS_CODE],
    [CPC_SUBCLASS_CODE],
    [CPC_GROUP_CODE],
    [CPC_SUBGROUP_CODE],
    [CPC_QUALIFICATION_CODE],
    GETDATE(),
    @DeletActivity,
    @TriggerName
FROM
    deleted;
END
END
;

ALTER TABLE [IPASPROD].[IP_PATENT_CPC_CLASSES] ENABLE TRIGGER [PATENT_CPC_CLASSES_INDEX_AFTER]
;

