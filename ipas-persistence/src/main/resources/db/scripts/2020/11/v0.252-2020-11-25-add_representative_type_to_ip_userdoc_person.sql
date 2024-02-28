--liquibase formatted sql

--changeset dveizov:252.1
ALTER TABLE EXT_CORE.IP_USERDOC_PERSON ADD REPRESENTATIVE_TYP varchar(2) DEFAULT NUlL;

--changeset dveizov:252.2 splitStatements:false
CREATE TRIGGER [EXT_CORE].[VALIDATE_USERDOC_PERSON_REPRESENTATIVE_TYPE]
    ON [EXT_CORE].[IP_USERDOC_PERSON]
    AFTER INSERT, UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @insertedRepresentativeType varchar(2);
    DECLARE @insertedPersonNumber numeric(15);
    DECLARE @insertedPersonAddressNumber numeric(4);
    DECLARE @insertedRole varchar(50);

    SELECT @insertedRepresentativeType = i.REPRESENTATIVE_TYP,
           @insertedPersonNumber = i.PERSON_NBR,
           @insertedPersonAddressNumber = i.ADDR_NBR,
           @insertedRole = i.ROLE
    FROM inserted i;

    IF @insertedRole = 'REPRESENTATIVE' AND @insertedRepresentativeType IS NULL
        BEGIN
            THROW 51000, 'REPRESENTATIVE_TYP cannot be empty for records with role REPRESENTATIVE', 1;
        END

    IF @insertedRole = 'OLD_REPRESENTATIVE' AND @insertedRepresentativeType IS NULL
        BEGIN
            THROW 51000, 'REPRESENTATIVE_TYP cannot be empty for records with role OLD_REPRESENTATIVE', 1;
        END

    IF @insertedRole = 'NEW_REPRESENTATIVE' AND @insertedRepresentativeType IS NULL
        BEGIN
            THROW 51000, 'REPRESENTATIVE_TYP cannot be empty for records with role NEW_REPRESENTATIVE', 1;
        END

    IF @insertedRepresentativeType IS NOT NULL
        BEGIN
            IF @insertedRole <> 'REPRESENTATIVE' AND @insertedRole <> 'OLD_REPRESENTATIVE' AND @insertedRole <> 'NEW_REPRESENTATIVE'
                BEGIN
                    THROW 51000, 'Cannot insert REPRESENTATIVE_TYP for records with role different from REPRESENTATIVE, OLD_REPRESENTATIVE, NEW_REPRESENTATIVE', 1;
                end

            DECLARE @existingRepresentativeTypeCount int;
            SET @existingRepresentativeTypeCount = (
                SELECT count(*) FROM IPASPROD.CF_REPRESENTATIVE_TYPE t where t.REPRESENTATIVE_TYP = @insertedRepresentativeType
            );
            IF @existingRepresentativeTypeCount <> 1 -- Check if action exists
                BEGIN
                    THROW 51000, 'Wrong REPRESENTATIVE_TYPE value', 1;
                END

            IF @insertedRepresentativeType = 'AG'
                BEGIN
                    DECLARE @personAgentCode numeric(5);
                    SET @personAgentCode = (
                        SELECT p.AGENT_CODE
                        FROM IPASPROD.IP_PERSON p JOIN IPASPROD.IP_PERSON_ADDRESSES pa on p.PERSON_NBR = pa.PERSON_NBR
                        where pa.PERSON_NBR = @insertedPersonNumber AND pa.ADDR_NBR = @insertedPersonAddressNumber
                    );

                    IF @personAgentCode IS NULL
                        BEGIN
                            THROW 51000, 'REPRESENTATIVE_TYPE = AG is only for persons with agent code', 1;
                        END
                END

            IF @insertedRepresentativeType = 'PA'
                BEGIN
                    DECLARE @personPartnershipCode varchar(10);
                    SET @personPartnershipCode = (
                        SELECT par.PARTNERSHIP_CODE
                        FROM IPASPROD.IP_PERSON p
                                 JOIN IPASPROD.IP_PERSON_ADDRESSES pa on p.PERSON_NBR = pa.PERSON_NBR
                                 JOIN EXT_AGENT.EXTENDED_PARTNERSHIP par on p.PERSON_NBR = par.PERSON_NBR
                        where pa.PERSON_NBR = @insertedPersonNumber AND pa.ADDR_NBR = @insertedPersonAddressNumber
                    );

                    IF @personPartnershipCode IS NULL OR @personPartnershipCode = ''
                        BEGIN
                            THROW 51000, 'REPRESENTATIVE_TYPE = PA is only for persons with partnership code', 1;
                        END
                END

        END

END

--changeset dveizov:252.3
UPDATE d
SET d.REPRESENTATIVE_TYP = 'AG'
FROM EXT_CORE.IP_USERDOC_PERSON d
         JOIN IPASPROD.IP_PERSON p on d.PERSON_NBR = p.PERSON_NBR
WHERE p.AGENT_CODE is not null AND d.ROLE IN ('REPRESENTATIVE', 'OLD_REPRESENTATIVE', 'NEW_REPRESENTATIVE')


--changeset dveizov:252.4
UPDATE d
SET d.REPRESENTATIVE_TYP = 'PA'
FROM EXT_CORE.IP_USERDOC_PERSON d
         JOIN IPASPROD.IP_PERSON p on d.PERSON_NBR = p.PERSON_NBR
         JOIN EXT_AGENT.EXTENDED_PARTNERSHIP par on p.PERSON_NBR = par.PERSON_NBR
WHERE par.PARTNERSHIP_CODE is not null AND d.ROLE IN ('REPRESENTATIVE', 'OLD_REPRESENTATIVE', 'NEW_REPRESENTATIVE')