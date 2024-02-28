--liquibase formatted sql

--changeset dveizov:253.1
ALTER TABLE EXT_RECEPTION.CORRESPONDENT ADD REPRESENTATIVE_TYP varchar(2) DEFAULT NUlL;

--changeset dveizov:253.2 splitStatements:false
CREATE TRIGGER [EXT_RECEPTION].[VALIDATE_CORRESPONDENT_REPRESENTATIVE_TYPE]
    ON [EXT_RECEPTION].[CORRESPONDENT]
    AFTER INSERT, UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @insertedRepresentativeType varchar(2);
    DECLARE @insertedPersonNumber numeric(15);
    DECLARE @insertedPersonAddressNumber numeric(4);
    DECLARE @insertedCorrespondentType varchar(50);

    SELECT @insertedRepresentativeType = i.REPRESENTATIVE_TYP,
           @insertedPersonNumber = i.PERSON_NBR,
           @insertedPersonAddressNumber = i.ADDRESS_NBR,
           @insertedCorrespondentType = i.TYPE
    FROM inserted i;

    IF @insertedCorrespondentType = 3 AND @insertedRepresentativeType IS NULL
        BEGIN
            THROW 51000, 'REPRESENTATIVE_TYP cannot be empty for records with type REPRESENTATIVE', 1;
        END

    IF @insertedRepresentativeType IS NOT NULL
        BEGIN
            IF @insertedCorrespondentType <> 3
                BEGIN
                    THROW 51000, 'Cannot insert REPRESENTATIVE_TYP for records with type different from REPRESENTATIVE', 1;
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

--changeset dveizov:253.3
UPDATE d
SET d.REPRESENTATIVE_TYP = 'AG'
FROM EXT_RECEPTION.CORRESPONDENT d
         JOIN IPASPROD.IP_PERSON p on d.PERSON_NBR = p.PERSON_NBR
JOIN EXT_RECEPTION.CF_CORRESPONDENT_TYPE t on d.TYPE = t.ID
WHERE p.AGENT_CODE is not null AND t.NAME_EN = 'Representative'


--changeset dveizov:253.4
UPDATE d
SET d.REPRESENTATIVE_TYP = 'PA'
FROM EXT_RECEPTION.CORRESPONDENT d
         JOIN IPASPROD.IP_PERSON p on d.PERSON_NBR = p.PERSON_NBR
         JOIN EXT_AGENT.EXTENDED_PARTNERSHIP pa on p.PERSON_NBR = pa.PERSON_NBR
         JOIN EXT_RECEPTION.CF_CORRESPONDENT_TYPE t on d.TYPE = t.ID
WHERE pa.PARTNERSHIP_CODE is not null AND t.NAME_EN = 'Representative'

--changeset dveizov:253.5
ALTER TABLE EXT_RECEPTION.USERDOC_CORRESPONDENT ADD REPRESENTATIVE_TYP varchar(2) DEFAULT NUlL;


--changeset dveizov:253.6 splitStatements:false
CREATE TRIGGER [EXT_RECEPTION].[VALIDATE_USERDOC_CORRESPONDENT_REPRESENTATIVE_TYPE]
    ON [EXT_RECEPTION].[USERDOC_CORRESPONDENT]
    AFTER INSERT, UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @insertedRepresentativeType varchar(2);
    DECLARE @insertedPersonNumber numeric(15);
    DECLARE @insertedPersonAddressNumber numeric(4);
    DECLARE @insertedCorrespondentType varchar(50);

    SELECT @insertedRepresentativeType = i.REPRESENTATIVE_TYP,
           @insertedPersonNumber = i.PERSON_NBR,
           @insertedPersonAddressNumber = i.ADDRESS_NBR,
           @insertedCorrespondentType = i.TYPE
    FROM inserted i;

    IF @insertedCorrespondentType = 3 AND @insertedRepresentativeType IS NULL
        BEGIN
            THROW 51000, 'REPRESENTATIVE_TYP cannot be empty for records with type REPRESENTATIVE', 1;
        END

    IF @insertedRepresentativeType IS NOT NULL
        BEGIN
            IF @insertedCorrespondentType <> 3
                BEGIN
                    THROW 51000, 'Cannot insert REPRESENTATIVE_TYP for records with type different from REPRESENTATIVE', 1;
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

--changeset dveizov:253.7
UPDATE d
SET d.REPRESENTATIVE_TYP = 'AG'
FROM EXT_RECEPTION.USERDOC_CORRESPONDENT d
         JOIN IPASPROD.IP_PERSON p on d.PERSON_NBR = p.PERSON_NBR
         JOIN EXT_RECEPTION.CF_CORRESPONDENT_TYPE t on d.TYPE = t.ID
WHERE p.AGENT_CODE is not null AND t.NAME_EN = 'Representative'


--changeset dveizov:253.8
UPDATE d
SET d.REPRESENTATIVE_TYP = 'PA'
FROM EXT_RECEPTION.USERDOC_CORRESPONDENT d
         JOIN IPASPROD.IP_PERSON p on d.PERSON_NBR = p.PERSON_NBR
         JOIN EXT_AGENT.EXTENDED_PARTNERSHIP pa on p.PERSON_NBR = pa.PERSON_NBR
         JOIN EXT_RECEPTION.CF_CORRESPONDENT_TYPE t on d.TYPE = t.ID
WHERE pa.PARTNERSHIP_CODE is not null AND t.NAME_EN = 'Representative'

