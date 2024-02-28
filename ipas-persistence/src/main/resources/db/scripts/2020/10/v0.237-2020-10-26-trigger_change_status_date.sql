--liquibase formatted sql

--changeset dveizov:237 splitStatements:false
CREATE TRIGGER [IPASPROD].[UPDATE_PROCESS_STATUS_DATE]
    ON [IPASPROD].[IP_ACTION]
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    -- update
    IF EXISTS(SELECT * FROM inserted) AND EXISTS(SELECT * FROM deleted)
        BEGIN
            DECLARE
                @procTyp varchar(4),
                @procNbr numeric(8),
                @actionNbr numeric(10),
                @newActionDate datetime,
                @oldStatusDate datetime,
                @newStatusDate datetime

            SELECT @procTyp = i.PROC_TYP, @procNbr = i.PROC_NBR, @actionNbr = i.ACTION_NBR, @newActionDate = i.ACTION_DATE FROM inserted i;

            SET @newStatusDate = (
                SELECT max(a.ACTION_DATE) FROM IPASPROD.IP_ACTION a where a.IND_CHANGES_STATUS = 'S' and a.PROC_TYP = @procTyp and a.PROC_NBR = @procNbr
            );

            SET @oldStatusDate = (
                SELECT a.STATUS_DATE FROM IPASPROD.IP_PROC a where a.PROC_TYP = @procTyp and a.PROC_NBR = @procNbr
            );

            IF @oldStatusDate <> @newStatusDate
                BEGIN
                    IF @newStatusDate IS NOT NULL
                        BEGIN
                            UPDATE IPASPROD.IP_PROC SET STATUS_DATE = @newStatusDate WHERE PROC_TYP = @procTyp AND PROC_NBR = @procNbr
                        END
                END
        END
END