--liquibase formatted sql

--changeset dveizov:296.1
DROP TRIGGER [IPASPROD].[UPDATE_PERSON_INDI_DATA];

--changeset dveizov:296.2 splitStatements:false
CREATE TRIGGER [IPASPROD].[UPDATE_PERSON_INDI_DATA]
    ON [IPASPROD].[IP_PERSON]
    AFTER INSERT, UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @insertedPerson numeric(15);
    DECLARE @insertedPersonIndiTxt varchar(50);
    DECLARE @insertedPersonIndiType varchar(20);
    DECLARE @indIndividualType varchar(1);


    SELECT @insertedPerson = d.PERSON_NBR, @insertedPersonIndiType = d.INDI_PERSON_ID_TYP, @insertedPersonIndiTxt = d.INDI_PERSON_ID_TXT FROM inserted d;


    IF ((@insertedPersonIndiType IS NOT NULL))
        BEGIN
            SELECT @indIndividualType = t.IND_INDIVIDUAL_ID FROM IPASPROD.CF_PERSON_ID_TYPE t where t.PERSON_ID_TYP = @insertedPersonIndiType
            IF (@indIndividualType <> 'S')
                BEGIN
                    THROW 51000, 'INDI_PERSON_ID_TYP must be defined as IND_INDIVIDUAL_ID = S in IPASPROD.CF_PERSON_ID_TYPE ', 1;
                END

        END

    IF (((@insertedPersonIndiTxt IS NULL OR @insertedPersonIndiTxt = '') AND @insertedPersonIndiType IS NOT NULL))
        BEGIN
            UPDATE IPASPROD.IP_PERSON SET INDI_PERSON_ID_TXT = NULL, INDI_PERSON_ID_TYP = NULL WHERE PERSON_NBR = @insertedPerson
        END

    IF (((@insertedPersonIndiTxt = '') AND (@insertedPersonIndiType IS NULL OR @insertedPersonIndiType = '')))
        BEGIN
            UPDATE IPASPROD.IP_PERSON SET INDI_PERSON_ID_TXT = NULL, INDI_PERSON_ID_TYP = NULL WHERE PERSON_NBR = @insertedPerson
        END

    IF ((@insertedPersonIndiTxt IS NOT NULL AND @insertedPersonIndiTxt <> '') AND (@insertedPersonIndiType IS NULL OR @insertedPersonIndiType = '') )
        BEGIN
            THROW 51000, 'INDI_PERSON_ID_TYP cannot be empty if there is INDI_PERSON_ID_TXT !', 1;
        END

END
;
