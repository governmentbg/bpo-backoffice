--liquibase formatted sql

--changeset dveizov:193 splitStatements:false
CREATE TRIGGER [IPASPROD].[UPDATE_PERSON_HIERARCHY_DATA]
    ON [IPASPROD].[IP_PERSON]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @insertedPerson numeric(15);
    DECLARE @insertedPersonOlderVersion numeric(15);
    DECLARE @deletedPerson numeric(15);
    DECLARE @deletedPersonOlderVersion numeric(15);
    DECLARE @deletedPersonNewerVersion numeric(15);

    -- update
    IF EXISTS(SELECT * FROM inserted) AND EXISTS(SELECT * FROM deleted)
        BEGIN

            SELECT @insertedPerson = d.PERSON_NBR, @insertedPersonOlderVersion = d.GRAL_PERSON_ID_NBR
            FROM inserted d;

            SELECT @deletedPerson = d.PERSON_NBR, @deletedPersonOlderVersion = d.GRAL_PERSON_ID_NBR
            FROM deleted d;

            IF (@deletedPersonOlderVersion IS NOT NULL AND @insertedPersonOlderVersion IS NULL) OR (@insertedPersonOlderVersion IS NOT NULL AND @deletedPersonOlderVersion IS NULL) OR (@deletedPersonOlderVersion <> @insertedPersonOlderVersion)
                BEGIN
                    DECLARE @lastInsertedPersonVersion numeric(8)
                    EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @insertedPerson, @ipPersonLastVersion = @lastInsertedPersonVersion OUTPUT
                    EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastInsertedPersonVersion

                    IF (@deletedPersonOlderVersion IS NOT NULL)
                        BEGIN
                            DECLARE @lastDeletedPersonOlderVersion numeric(8)
                            EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @deletedPersonOlderVersion, @ipPersonLastVersion = @lastDeletedPersonOlderVersion OUTPUT
                            EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastDeletedPersonOlderVersion
                        END
                END
        END

    -- insert
    IF EXISTS(SELECT * FROM inserted) AND NOT EXISTS(SELECT * FROM deleted)
        BEGIN
            SELECT @insertedPerson = d.PERSON_NBR, @insertedPersonOlderVersion = d.GRAL_PERSON_ID_NBR FROM inserted d;

            IF @insertedPersonOlderVersion IS NOT NULL
                BEGIN
                    DECLARE @lastPersonVersionInsert numeric(8)
                    EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @insertedPerson, @ipPersonLastVersion = @lastPersonVersionInsert OUTPUT
                    EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastPersonVersionInsert
                END
        END

    -- delete
    IF EXISTS(SELECT * FROM deleted) AND NOT EXISTS(SELECT * FROM inserted)
        BEGIN
            SELECT @deletedPersonOlderVersion = d.GRAL_PERSON_ID_NBR,
                   @deletedPerson = d.PERSON_NBR
            FROM deleted d;

            SET @deletedPersonNewerVersion = (
                SELECT p.PERSON_NBR FROM IPASPROD.IP_PERSON p WHERE p.GRAL_PERSON_ID_NBR = @deletedPerson
            );

            IF @deletedPersonNewerVersion IS NOT NULL
                BEGIN
                    UPDATE IPASPROD.IP_PERSON SET GRAL_PERSON_ID_NBR = @deletedPersonOlderVersion WHERE PERSON_NBR = @deletedPersonNewerVersion

                    DECLARE @lastPersonNewerVersionDelete numeric(8)
                    EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @deletedPersonNewerVersion, @ipPersonLastVersion = @lastPersonNewerVersionDelete OUTPUT
                    EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastPersonNewerVersionDelete
                END

            IF @deletedPersonOlderVersion IS NOT NULL
                BEGIN
                    DECLARE @lastPersonOlderVersionDelete numeric(8)
                    EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @deletedPersonOlderVersion, @ipPersonLastVersion = @lastPersonOlderVersionDelete OUTPUT
                    EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastPersonOlderVersionDelete
                END
        END
END
;
