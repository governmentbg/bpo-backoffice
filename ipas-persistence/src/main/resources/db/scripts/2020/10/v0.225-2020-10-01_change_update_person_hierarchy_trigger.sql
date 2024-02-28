--liquibase formatted sql

--changeset dveizov:225.1
IF EXISTS(SELECT * FROM sys.triggers WHERE type = 'TR' AND name = 'UPDATE_PERSON_HIERARCHY_DATA') DROP TRIGGER [IPASPROD].[UPDATE_PERSON_HIERARCHY_DATA];

--changeset dveizov:225.2 splitStatements:false
CREATE TRIGGER [IPASPROD].[UPDATE_PERSON_HIERARCHY_DATA]
    ON [IPASPROD].[IP_PERSON]
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @insertedPerson numeric(15);
    DECLARE @insertedPersonOlderVersion numeric(15);
    DECLARE @insertedPersonNewerVersion numeric(15);
    DECLARE @deletedPerson numeric(15);
    DECLARE @deletedPersonOlderVersion numeric(15);
    DECLARE @deletedPersonNewerVersion numeric(15);

    -- update
    IF EXISTS(SELECT * FROM inserted) AND EXISTS(SELECT * FROM deleted)
        BEGIN
            DECLARE @deletedPersonGralType varchar(4);
            DECLARE @insertedPersonGralType varchar(4);

            SELECT @insertedPerson = d.PERSON_NBR, @insertedPersonGralType = d.GRAL_PERSON_ID_TYP, @insertedPersonNewerVersion = d.GRAL_PERSON_ID_NBR FROM inserted d;
            SELECT @deletedPerson = d.PERSON_NBR, @deletedPersonGralType = d.GRAL_PERSON_ID_TYP, @deletedPersonNewerVersion = d.GRAL_PERSON_ID_NBR FROM deleted d;
            IF (@deletedPersonNewerVersion IS NOT NULL AND @insertedPersonNewerVersion IS NULL)
                OR (@deletedPersonNewerVersion IS NULL AND @insertedPersonNewerVersion IS NOT NULL)
                OR (@deletedPersonNewerVersion <> @insertedPersonNewerVersion)
                OR (@deletedPersonGralType IS NOT NULL AND @insertedPersonGralType IS NULL)
                BEGIN
                    DECLARE @lastInsertedPersonVersion numeric(8)
                    EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @insertedPerson, @ipPersonLastVersion = @lastInsertedPersonVersion OUTPUT
                    EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastInsertedPersonVersion

                    IF (@deletedPersonNewerVersion IS NOT NULL)
                        BEGIN
                            DECLARE @lastDeletedPersonNewerVersion numeric(8)
                            EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @deletedPersonNewerVersion, @ipPersonLastVersion = @lastDeletedPersonNewerVersion OUTPUT
                            EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastDeletedPersonNewerVersion
                        END
                END
        END

    -- insert
    IF EXISTS(SELECT * FROM inserted) AND NOT EXISTS(SELECT * FROM deleted)
        BEGIN
            SELECT @insertedPerson = d.PERSON_NBR, @insertedPersonNewerVersion = d.GRAL_PERSON_ID_NBR FROM inserted d;

            IF @insertedPersonNewerVersion IS NOT NULL
                BEGIN
                    DECLARE @lastPersonVersionInsert numeric(8)
                    EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @insertedPerson, @ipPersonLastVersion = @lastPersonVersionInsert OUTPUT
                    EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastPersonVersionInsert
                END
        END

    -- delete
    IF EXISTS(SELECT * FROM deleted) AND NOT EXISTS(SELECT * FROM inserted)
        BEGIN
            SELECT @deletedPersonNewerVersion = d.GRAL_PERSON_ID_NBR, @deletedPerson = d.PERSON_NBR FROM deleted d;
            IF @deletedPersonNewerVersion IS NOT NULL
                BEGIN
                    DECLARE @lastPersonNewerVersionDelete numeric(8)
                    EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @deletedPersonNewerVersion, @ipPersonLastVersion = @lastPersonNewerVersionDelete OUTPUT
                    EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastPersonNewerVersionDelete;
                END

            DECLARE @olderVersion numeric(15);
            DECLARE @lastPersonOlderVersion numeric(8)
            DECLARE curson_child CURSOR LOCAL FOR SELECT p.PERSON_NBR as olderVersion FROM IPASPROD.IP_PERSON p where p.GRAL_PERSON_ID_NBR = @deletedPerson;
            OPEN curson_child;
            FETCH NEXT FROM curson_child INTO @olderVersion;
            WHILE @@FETCH_STATUS = 0
                BEGIN
                    UPDATE IPASPROD.IP_PERSON SET GRAL_PERSON_ID_NBR = @deletedPersonNewerVersion WHERE PERSON_NBR = @olderVersion

                    EXECUTE IPASPROD.selectIpPersonLastVersion @personNumber = @olderVersion, @ipPersonLastVersion = @lastPersonOlderVersion OUTPUT
                    EXECUTE IPASPROD.updateIpPersonGralIdTypeStartingByLastVersion @personNumber = @lastPersonOlderVersion

                    FETCH NEXT FROM curson_child INTO @olderVersion;
                END;
            CLOSE curson_child;
            DEALLOCATE curson_child;
        END
END

