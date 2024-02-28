--liquibase formatted sql

--changeset dveizov:179.1
DROP PROCEDURE IPASPROD.splitIpPerson;

--changeset dveizov:179.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[splitIpPerson] @personNumber NUMERIC(8),
                                            @addressNumber NUMERIC(4),
                                            @newPersonNumber NUMERIC(8) OUTPUT,
                                            @newAddressNumber NUMERIC(4) OUTPUT,
                                            @executionStatus BIT OUTPUT,
                                            @errorMessage NVARCHAR(4000) OUTPUT



AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            DECLARE @personAddressCount int;
            SET @personAddressCount = (
                SELECT COUNT(*) FROM IPASPROD.IP_PERSON_ADDRESSES a WHERE a.PERSON_NBR = @personNumber
            );
            IF @personAddressCount < 2 AND @addressNumber = 1
                BEGIN
                    THROW 51000, 'Cannot split person who has less than 2 addresses', 1;
                END

            IF @addressNumber = 1 -- Check if person address number is equals to 1
                BEGIN
                    THROW 51000, 'Cannot split person who has address number with id = 1', 1;
                END

            DECLARE @selectedPersonRecordCount int;
            SET @selectedPersonRecordCount = (
                SELECT count(*) FROM IPASPROD.IP_PERSON_ADDRESSES a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            );
            IF @selectedPersonRecordCount <> 1 -- Check if action exists
                BEGIN
                    THROW 51000, 'Cannot find person', 1;
                END

            DECLARE
                -- IP_PERSON COLUMNS
                @ipPersonRowVersion         numeric(9),
                @personName                 nvarchar(700),
                @personWCode                varchar(1),
                @nationalityCountryCode     varchar(2),
                @agentCode                  numeric(5),
                @legalNature                varchar(254),
                @telephone                  varchar(200),
                @email                      varchar(200),
                @personGroupNumber          varchar(4),
                @gralPersonIdTyp            varchar(4),
                @gralPersonIdNbr            numeric(15),
                @indiPersonIdTyp            varchar(4),
                @indiPersonIdNbr            varchar(13),
                @companyRegisterDate        datetime,
                @companyRegisterNbr         varchar(20),
                @gralPersonIdTxt            varchar(25),
                @indiPersonIdTxt            varchar(25),
                @personNameLang2            varchar(700),
                @legalNatureLang2           varchar(254),
                -- IP_PERSON_ADDRESS COLUMNS
                @ipPersonAddressRowVersion  numeric(9),
                @addrStreet                 nvarchar(2000),
                @addrZone                   nvarchar(254),
                @cityName                   nvarchar(254),
                @cityCode                   varchar(6),
                @residenceCountryCode       varchar(2),
                @stateCode                  varchar(6),
                @stateName                  varchar(254),
                @zipcode                    varchar(16),
                @addrStreetLang2            varchar(2000);
            SELECT @ipPersonRowVersion = p.ROW_VERSION,
                   @personName = p.PERSON_NAME,
                   @personWCode = p.PERSON_WCODE,
                   @nationalityCountryCode = p.NATIONALITY_COUNTRY_CODE,
                   @agentCode = p.AGENT_CODE,
                   @legalNature = p.LEGAL_NATURE,
                   @telephone = p.TELEPHONE,
                   @email = p.EMAIL,
                   @personGroupNumber = p.PERSON_GROUP_NBR,
                   @gralPersonIdTyp = p.GRAL_PERSON_ID_TYP,
                   @gralPersonIdNbr = p.GRAL_PERSON_ID_NBR,
                   @indiPersonIdTyp = p.INDI_PERSON_ID_TYP,
                   @indiPersonIdNbr = p.INDI_PERSON_ID_NBR,
                   @companyRegisterDate = p.COMPANY_REGISTER_DATE,
                   @companyRegisterNbr = p.COMPANY_REGISTER_NBR,
                   @gralPersonIdTxt = p.GRAL_PERSON_ID_TXT,
                   @indiPersonIdTxt = p.INDI_PERSON_ID_TXT,
                   @personNameLang2 = p.PERSON_NAME_LANG2,
                   @legalNatureLang2 = p.LEGAL_NATURE_LANG2,
                   @ipPersonAddressRowVersion = pa.ROW_VERSION,
                   @addrStreet = pa.ADDR_STREET,
                   @addrZone = pa.ADDR_ZONE,
                   @cityName = pa.CITY_NAME,
                   @cityCode = pa.CITY_CODE,
                   @residenceCountryCode = pa.RESIDENCE_COUNTRY_CODE,
                   @stateCode = pa.STATE_CODE,
                   @stateName = pa.STATE_NAME,
                   @zipcode = pa.ZIPCODE,
                   @addrStreetLang2 = pa.ADDR_STREET_LANG2
            FROM IPASPROD.IP_PERSON_ADDRESSES pa
                     JOIN IPASPROD.IP_PERSON p ON pa.PERSON_NBR = p.PERSON_NBR
            WHERE pa.PERSON_NBR = @personNumber AND pa.ADDR_NBR = @addressNumber

            EXEC selectNextIpPersonNumber @newIpPersonNumber = @newPersonNumber OUTPUT

            INSERT INTO IPASPROD.IP_PERSON(ROW_VERSION, PERSON_NBR, PERSON_NAME, PERSON_WCODE, NATIONALITY_COUNTRY_CODE,
                                           AGENT_CODE, LEGAL_NATURE, TELEPHONE, EMAIL, PERSON_GROUP_NBR,
                                           GRAL_PERSON_ID_TYP, GRAL_PERSON_ID_NBR, INDI_PERSON_ID_TYP,
                                           INDI_PERSON_ID_NBR, COMPANY_REGISTER_DATE, COMPANY_REGISTER_NBR,
                                           GRAL_PERSON_ID_TXT, INDI_PERSON_ID_TXT, PERSON_NAME_LANG2,
                                           LEGAL_NATURE_LANG2)
            VALUES (1, @newPersonNumber, @personName, @personWCode, @nationalityCountryCode, @agentCode, @legalNature,
                    @telephone, @email, @personGroupNumber, @gralPersonIdTyp, @gralPersonIdNbr, @indiPersonIdTyp,
                    @indiPersonIdNbr, @companyRegisterDate, @companyRegisterNbr, @gralPersonIdTxt, @indiPersonIdTxt,
                    @personNameLang2, @legalNatureLang2)

            SET @newAddressNumber = 1;
            INSERT INTO IPASPROD.IP_PERSON_ADDRESSES (ROW_VERSION, PERSON_NBR, ADDR_NBR, ADDR_STREET, ADDR_ZONE,
                                                      CITY_NAME, CITY_CODE, RESIDENCE_COUNTRY_CODE, STATE_CODE,
                                                      STATE_NAME, ZIPCODE, ADDR_STREET_LANG2)
            VALUES (1, @newPersonNumber, @newAddressNumber, @addrStreet, @addrZone, @cityName, @cityCode, @residenceCountryCode,
                    @stateCode, @stateName, @zipcode, @addrStreetLang2)



            DECLARE
                @executionStatusReplaceIpPerson BIT,
                @errorMessageReplaceIpPerson NVARCHAR(4000)

            EXEC replaceIpPerson @oldPersonNumber = @personNumber, @oldAddressNumber = @addressNumber, @newPersonNumber = @newPersonNumber, @newAddressNumber = @newAddressNumber, @executionStatus = @executionStatusReplaceIpPerson OUTPUT, @errorMessage = @errorMessageReplaceIpPerson OUTPUT

            IF @executionStatusReplaceIpPerson = 1 -- Check if replaceIpPerson is executed successful
                BEGIN
                    THROW 51000, @errorMessageReplaceIpPerson, 1;
                END

           SET @executionStatus = 0;
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        DECLARE
            @errorSeverity INT,
            @errorNumber   INT,
            @errorState    INT,
            @errorLine     INT,
            @errorProc     NVARCHAR(200)
        SET @errorSeverity = ERROR_SEVERITY()
        SET @errorNumber = ERROR_NUMBER()
        SET @errorMessage = ERROR_MESSAGE()
        SET @errorState = ERROR_STATE()

        -- Not all errors generate an error state, to set to 1 if it's zero
        IF @errorState = 0
            SET @errorState = 1
        -- If the error renders the transaction as uncommittable or we have open transactions, we may want to rollback
        IF @@TRANCOUNT > 0
            BEGIN
                PRINT 'ROLLBACK'
                ROLLBACK TRANSACTION
            END

        SET @executionStatus = 1;
        RAISERROR (@errorMessage , @errorSeverity, @errorState, @errorNumber)
    END CATCH
    RETURN @@ERROR
END
