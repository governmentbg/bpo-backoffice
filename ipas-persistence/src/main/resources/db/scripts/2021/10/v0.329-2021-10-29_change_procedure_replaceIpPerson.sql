--liquibase formatted sql

--changeset mmurlev:329.1
drop procedure replaceIpPerson

--changeset mmurlev:329.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[replaceIpPerson] @oldPersonNumber NUMERIC(8),
                                              @oldAddressNumber NUMERIC(4),
                                              @newPersonNumber NUMERIC(8),
                                              @newAddressNumber NUMERIC(4),
                                              @executionStatus BIT OUTPUT,
                                              @errorMessage NVARCHAR(4000) OUTPUT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            DECLARE @selectedOldPersonRecordCount int;
            SET @selectedOldPersonRecordCount = (
                SELECT count(*) FROM IPASPROD.IP_PERSON_ADDRESSES a where a.PERSON_NBR = @oldPersonNumber AND a.ADDR_NBR = @oldAddressNumber
            );
            IF @selectedOldPersonRecordCount <> 1
                BEGIN
                    THROW 51000, 'Cannot find person', 1;
                END

            DECLARE @selectedNewPersonRecordCount int;
            SET @selectedNewPersonRecordCount = (
                SELECT count(*) FROM IPASPROD.IP_PERSON_ADDRESSES a where a.PERSON_NBR = @newPersonNumber AND a.ADDR_NBR = @newAddressNumber
            );
            IF @selectedNewPersonRecordCount <> 1
                BEGIN
                    THROW 51000, 'Cannot find new person', 1;
                END

            DECLARE
                @ipMarkOwnersCount int,
                @ipMarkRepresentativesCount int ,
                @ipMarkMainOwnerCount int ,
                @ipMarkServicePersonCount int ,
                @ipPatentOwnersCount int ,
                @ipPatentRepresentativesCount int ,
                @ipPatentInventorsCount int ,
                @ipPatentMainOwnerCount int ,
                @ipPatentServicePersonCount int ,
                @ipFileMainOwnerCount int ,
                @ipFileServicePersonCount int ,
                @ipUserdocApplicantCount int ,
                @ipUserdocPayeeCount int ,
                @ipUserdocPayerCount int ,
                @ipUserdocGranteeCount int ,
                @ipUserdocGrantorCount int ,
                @ipUserdocOldOwnersCount int ,
                @ipUserdocNewOwnersCount int ,
                @ipUserdocRepresentativeCount int ,
                @ipUserdocPersonCount int ,
                @ipPersonAbdocsSyncCount int ,
                @extReceptionCorrespondentCount int ,
                @extReceptionUserdocCorrespondentCount int ,
                @agentHistoryCount int ,
                @extendedAgentCount int ,
                @extendedPersonAddressesCount int ,
                @extendedPartnershipCount int ,
                @partnershipAgentAgentsCount int ,
                @partnershipAgentPartnershipsCount int ,
                @partnershipHistoryCount int ,
                @acpRepresentativesCount int ,
                @acpInfringerCount int ,
                @acpServicePersonCount int;

            exec findIpPersonUse @personNumber = @oldPersonNumber, @addressNumber = @oldAddressNumber,
                 @ipMarkOwnersCount = @ipMarkOwnersCount OUTPUT,
                 @ipMarkRepresentativesCount = @ipMarkRepresentativesCount OUTPUT,
                 @ipMarkMainOwnerCount = @ipMarkMainOwnerCount OUTPUT,
                 @ipMarkServicePersonCount = @ipMarkServicePersonCount OUTPUT,
                 @ipPatentOwnersCount = @ipPatentOwnersCount OUTPUT,
                 @ipPatentRepresentativesCount = @ipPatentRepresentativesCount OUTPUT,
                 @ipPatentInventorsCount = @ipPatentInventorsCount OUTPUT,
                 @ipPatentMainOwnerCount = @ipPatentMainOwnerCount OUTPUT,
                 @ipPatentServicePersonCount = @ipPatentServicePersonCount OUTPUT,
                 @ipFileMainOwnerCount = @ipFileMainOwnerCount OUTPUT,
                 @ipFileServicePersonCount = @ipFileServicePersonCount OUTPUT,
                 @ipUserdocApplicantCount = @ipUserdocApplicantCount OUTPUT,
                 @ipUserdocPayeeCount = @ipUserdocPayeeCount OUTPUT,
                 @ipUserdocPayerCount = @ipUserdocPayerCount OUTPUT,
                 @ipUserdocGranteeCount = @ipUserdocGranteeCount OUTPUT,
                 @ipUserdocGrantorCount = @ipUserdocGrantorCount OUTPUT,
                 @ipUserdocOldOwnersCount = @ipUserdocOldOwnersCount OUTPUT,
                 @ipUserdocNewOwnersCount = @ipUserdocNewOwnersCount OUTPUT,
                 @ipUserdocRepresentativeCount = @ipUserdocRepresentativeCount OUTPUT,
                 @ipUserdocPersonCount = @ipUserdocPersonCount OUTPUT,
                 @ipPersonAbdocsSyncCount = @ipPersonAbdocsSyncCount OUTPUT,
                 @extReceptionCorrespondentCount = @extReceptionCorrespondentCount OUTPUT,
                 @extReceptionUserdocCorrespondentCount = @extReceptionUserdocCorrespondentCount OUTPUT,
                 @agentHistoryCount = @agentHistoryCount OUTPUT,
                 @extendedAgentCount = @extendedAgentCount OUTPUT,
                 @extendedPersonAddressesCount = @extendedPersonAddressesCount OUTPUT,
                 @extendedPartnershipCount = @extendedPartnershipCount OUTPUT,
                 @partnershipAgentAgentsCount = @partnershipAgentAgentsCount OUTPUT,
                 @partnershipAgentPartnershipsCount = @partnershipAgentPartnershipsCount OUTPUT,
                 @partnershipHistoryCount = @partnershipHistoryCount OUTPUT,
                 @acpRepresentativesCount = @acpRepresentativesCount OUTPUT,
                 @acpInfringerCount = @acpInfringerCount OUTPUT,
                 @acpServicePersonCount = @acpServicePersonCount OUTPUT

            DECLARE @logNotes varchar(4000);
            SET @logNotes = '';

            IF @ipMarkOwnersCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_MARK_OWNERS: '+ CAST(@ipMarkOwnersCount as varchar) ;
                    UPDATE IPASPROD.IP_MARK_OWNERS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_MARK_OWNERS('+ CAST(@ipMarkOwnersCount as varchar)+'),'
                END
            IF @ipMarkRepresentativesCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_MARK_REPR: '+ CAST(@ipMarkRepresentativesCount as varchar) ;
                    UPDATE IPASPROD.IP_MARK_REPRS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_MARK_REPR('+ CAST(@ipMarkRepresentativesCount as varchar)+'),'
                END
            IF @ipMarkMainOwnerCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_MARK.MAIN_OWNER: '+ CAST(@ipMarkMainOwnerCount as varchar) ;
                    UPDATE IPASPROD.IP_MARK SET MAIN_OWNER_PERSON_NBR = @newPersonNumber, MAIN_OWNER_ADDR_NBR = @newAddressNumber WHERE MAIN_OWNER_PERSON_NBR = @oldPersonNumber AND MAIN_OWNER_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_MARK.MAIN_OWNER('+ CAST(@ipMarkMainOwnerCount as varchar)+'),'
              END
            IF @ipMarkServicePersonCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_MARK.SERVICE_PERSON: '+ CAST(@ipMarkServicePersonCount as varchar) ;
                    UPDATE IPASPROD.IP_MARK SET SERVICE_PERSON_NBR = @newPersonNumber, SERVICE_ADDR_NBR = @newAddressNumber WHERE SERVICE_PERSON_NBR = @oldPersonNumber AND SERVICE_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_MARK.SERVICE_PERSON('+ CAST(@ipMarkServicePersonCount as varchar)+'),'
                END
            IF @ipPatentOwnersCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT_OWNERS: '+ CAST(@ipPatentOwnersCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT_OWNERS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_PATENT_OWNERS('+ CAST(@ipPatentOwnersCount as varchar)+'),'
                END
            IF @ipPatentRepresentativesCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT_REPRS: '+ CAST(@ipPatentRepresentativesCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT_REPRS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_PATENT_REPRS('+ CAST(@ipPatentRepresentativesCount as varchar)+'),'
                END
            IF @ipPatentInventorsCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT_INVENTORS: '+ CAST(@ipPatentInventorsCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT_INVENTORS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_PATENT_INVENTORS('+ CAST(@ipPatentInventorsCount as varchar)+'),'
                END
            IF @ipPatentMainOwnerCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT.MAIN_OWNER: '+ CAST(@ipPatentMainOwnerCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT SET MAIN_OWNER_PERSON_NBR = @newPersonNumber, MAIN_OWNER_ADDR_NBR = @newAddressNumber WHERE MAIN_OWNER_PERSON_NBR = @oldPersonNumber AND MAIN_OWNER_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_PATENT.MAIN_OWNER('+ CAST(@ipPatentMainOwnerCount as varchar)+'),'
                END
            IF @ipPatentServicePersonCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT.SERVICE_PERSON: '+ CAST(@ipPatentServicePersonCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT SET SERVICE_PERSON_NBR = @newPersonNumber, SERVICE_ADDR_NBR = @newAddressNumber WHERE SERVICE_PERSON_NBR = @oldPersonNumber AND SERVICE_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_PATENT.SERVICE_PERSON('+ CAST(@ipPatentServicePersonCount as varchar)+'),'
                END
            IF @ipFileMainOwnerCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_FILE.MAIN_OWNER: '+ CAST(@ipFileMainOwnerCount as varchar) ;
                    UPDATE IPASPROD.IP_FILE SET MAIN_OWNER_PERSON_NBR = @newPersonNumber, MAIN_OWNER_ADDR_NBR = @newAddressNumber WHERE MAIN_OWNER_PERSON_NBR = @oldPersonNumber AND MAIN_OWNER_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_FILE.MAIN_OWNER('+ CAST(@ipFileMainOwnerCount as varchar)+'),'
                END
            IF @ipFileServicePersonCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_FILE.SERVICE_PERSON: '+ CAST(@ipFileServicePersonCount as varchar) ;
                    UPDATE IPASPROD.IP_FILE SET SERVICE_PERSON_NBR = @newPersonNumber, SERVICE_ADDR_NBR = @newAddressNumber WHERE SERVICE_PERSON_NBR = @oldPersonNumber AND SERVICE_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_FILE.SERVICE_PERSON('+ CAST(@ipFileServicePersonCount as varchar)+'),'
                END
            IF @ipUserdocApplicantCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC.APPLICANT: '+ CAST(@ipUserdocApplicantCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET APPLICANT_PERSON_NBR = @newPersonNumber, APPLICANT_ADDR_NBR = @newAddressNumber WHERE APPLICANT_PERSON_NBR = @oldPersonNumber AND APPLICANT_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_USERDOC.APPLICANT('+ CAST(@ipUserdocApplicantCount as varchar)+'),'
                END
            IF @ipUserdocPayeeCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC.PAYEE: '+ CAST(@ipUserdocPayeeCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET PAYEE_PERSON_NBR = @newPersonNumber, PAYEE_ADDR_NBR = @newAddressNumber WHERE PAYEE_PERSON_NBR = @oldPersonNumber AND PAYEE_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_USERDOC.PAYEE('+ CAST(@ipUserdocPayeeCount as varchar)+'),'
                END
            IF @ipUserdocPayerCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC.PAYER: '+ CAST(@ipUserdocPayerCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET PAYER_PERSON_NBR = @newPersonNumber, PAYER_ADDR_NBR = @newAddressNumber WHERE PAYER_PERSON_NBR = @oldPersonNumber AND PAYER_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_USERDOC.PAYER('+ CAST(@ipUserdocPayerCount as varchar)+'),'
                END
            IF @ipUserdocGranteeCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC.GRANTEE: '+ CAST(@ipUserdocGranteeCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET GRANTEE_PERSON_NBR = @newPersonNumber, GRANTEE_ADDR_NBR = @newAddressNumber WHERE GRANTEE_PERSON_NBR = @oldPersonNumber AND GRANTEE_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_USERDOC.GRANTEE('+ CAST(@ipUserdocGranteeCount as varchar)+'),'
                END
            IF @ipUserdocGrantorCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC.GRANTOR: '+ CAST(@ipUserdocGrantorCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET GRANTOR_PERSON_NBR = @newPersonNumber, GRANTOR_ADDR_NBR = @newAddressNumber WHERE GRANTOR_PERSON_NBR = @oldPersonNumber AND GRANTOR_ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_USERDOC.GRANTOR('+ CAST(@ipUserdocGrantorCount as varchar)+'),'
                END
            IF @ipUserdocOldOwnersCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC_OLD_OWNERS: '+ CAST(@ipUserdocOldOwnersCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC_OLD_OWNERS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_USERDOC_OLD_OWNERS('+ CAST(@ipUserdocOldOwnersCount as varchar)+'),'
                END
            IF @ipUserdocNewOwnersCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC_NEW_OWNERS: '+ CAST(@ipUserdocNewOwnersCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC_NEW_OWNERS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_USERDOC_NEW_OWNERS('+ CAST(@ipUserdocNewOwnersCount as varchar)+'),'
                END
            IF @ipUserdocRepresentativeCount > 0
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC_REPRS: '+ CAST(@ipUserdocRepresentativeCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC_REPRS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'IPASPROD.IP_USERDOC_REPRS('+ CAST(@ipUserdocRepresentativeCount as varchar)+'),'
                END
            IF @ipUserdocPersonCount > 0
                BEGIN
                    PRINT 'EXT_CORE.IP_USERDOC_PERSON: '+ CAST(@ipUserdocPersonCount as varchar) ;
                    UPDATE EXT_CORE.IP_USERDOC_PERSON SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'EXT_CORE.IP_USERDOC_PERSON('+ CAST(@ipUserdocPersonCount as varchar)+'),'
                END
            IF @extReceptionCorrespondentCount > 0
                BEGIN
                    PRINT 'EXT_RECEPTION.CORRESPONDENT: '+ CAST(@extReceptionCorrespondentCount as varchar) ;
                    UPDATE EXT_RECEPTION.CORRESPONDENT SET PERSON_NBR = @newPersonNumber, ADDRESS_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDRESS_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'EXT_RECEPTION.CORRESPONDENT('+ CAST(@extReceptionCorrespondentCount as varchar)+'),'
                END
            IF @extReceptionUserdocCorrespondentCount > 0
                BEGIN
                    PRINT 'EXT_RECEPTION.USERDOC_CORRESPONDENT: '+ CAST(@extReceptionUserdocCorrespondentCount as varchar) ;
                    UPDATE EXT_RECEPTION.USERDOC_CORRESPONDENT SET PERSON_NBR = @newPersonNumber, ADDRESS_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDRESS_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'EXT_RECEPTION.USERDOC_CORRESPONDENT('+ CAST(@extReceptionUserdocCorrespondentCount as varchar)+'),'
                END
            IF @agentHistoryCount > 0
                BEGIN
                    PRINT 'EXT_AGENT.AGENT_HISTORY: '+ CAST(@agentHistoryCount as varchar) ;
                    UPDATE EXT_AGENT.AGENT_HISTORY SET PERSON_NBR = @newPersonNumber WHERE PERSON_NBR = @oldPersonNumber
                    SET @logNotes = @logNotes + 'EXT_AGENT.AGENT_HISTORY('+ CAST(@agentHistoryCount as varchar)+'),'
                END
            IF @extendedAgentCount > 0
                BEGIN
                    PRINT 'EXT_AGENT.EXTENDED_AGENT: '+ CAST(@extendedAgentCount as varchar) ;
                    UPDATE EXT_AGENT.EXTENDED_AGENT SET PERSON_NBR = @newPersonNumber WHERE PERSON_NBR = @oldPersonNumber
                    SET @logNotes = @logNotes + 'EXT_AGENT.EXTENDED_AGENT('+ CAST(@extendedAgentCount as varchar)+'),'
                END
            IF @extendedPersonAddressesCount > 0
                BEGIN
                    PRINT 'EXT_AGENT.EXTENDED_PERSON_ADDRESSES: '+ CAST(@extendedPersonAddressesCount as varchar) ;
                    UPDATE EXT_AGENT.EXTENDED_PERSON_ADDRESSES SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                    SET @logNotes = @logNotes + 'EXT_AGENT.EXTENDED_PERSON_ADDRESSES('+ CAST(@extendedPersonAddressesCount as varchar)+'),'
                END
            IF @extendedPartnershipCount > 0
                BEGIN
                    PRINT 'EXT_AGENT.EXTENDED_PARTNERSHIP: '+ CAST(@extendedPartnershipCount as varchar) ;
                    UPDATE EXT_AGENT.EXTENDED_PARTNERSHIP SET PERSON_NBR = @newPersonNumber WHERE PERSON_NBR = @oldPersonNumber
                    SET @logNotes = @logNotes + 'EXT_AGENT.EXTENDED_PARTNERSHIP('+ CAST(@extendedPartnershipCount as varchar)+'),'
                END
            IF @partnershipAgentAgentsCount > 0
                BEGIN
                    PRINT 'EXT_AGENT.PARTNERSHIP_AGENT.AGENT: '+ CAST(@partnershipAgentAgentsCount as varchar) ;
                    UPDATE EXT_AGENT.PARTNERSHIP_AGENT SET AGENT_PERSON_NBR = @newPersonNumber WHERE AGENT_PERSON_NBR = @oldPersonNumber
                    SET @logNotes = @logNotes + 'EXT_AGENT.PARTNERSHIP_AGENT.AGENT('+ CAST(@partnershipAgentAgentsCount as varchar)+'),'
                END
            IF @partnershipAgentPartnershipsCount > 0
                BEGIN
                    PRINT 'EXT_AGENT.PARTNERSHIP_AGENT.PARTNERSHIP: '+ CAST(@partnershipAgentPartnershipsCount as varchar) ;
                    UPDATE EXT_AGENT.PARTNERSHIP_AGENT SET PARTNERSHIP_PERSON_NBR = @newPersonNumber WHERE PARTNERSHIP_PERSON_NBR = @oldPersonNumber
                    SET @logNotes = @logNotes + 'EXT_AGENT.PARTNERSHIP_AGENT.PARTNERSHIP('+ CAST(@partnershipAgentPartnershipsCount as varchar)+'),'
                END
            IF @partnershipHistoryCount > 0
                BEGIN
                    PRINT 'EXT_AGENT.PARTNERSHIP_HISTORY: '+ CAST(@partnershipHistoryCount as varchar) ;
                    UPDATE EXT_AGENT.PARTNERSHIP_HISTORY SET PARTNERSHIP_NBR = @newPersonNumber WHERE PARTNERSHIP_NBR = @oldPersonNumber
                    SET @logNotes = @logNotes + 'EXT_AGENT.PARTNERSHIP_HISTORY('+ CAST(@partnershipHistoryCount as varchar)+'),'
                END
             IF @acpRepresentativesCount > 0
                BEGIN
                    PRINT 'EXT_CORE.ACP_REPRS: '+ CAST(@acpRepresentativesCount as varchar) ;
                    UPDATE EXT_CORE.ACP_REPRS SET PERSON_NBR = @newPersonNumber WHERE PERSON_NBR = @oldPersonNumber
                    SET @logNotes = @logNotes + 'EXT_CORE.ACP_REPRS('+ CAST(@acpRepresentativesCount as varchar)+'),'
                END
             IF @acpInfringerCount > 0
                BEGIN
                    PRINT 'EXT_CORE.ACP_INFRINGER: '+ CAST(@acpInfringerCount as varchar) ;
                    UPDATE EXT_CORE.ACP_INFRINGER SET INFRINGER_PERSON_NBR = @newPersonNumber WHERE INFRINGER_PERSON_NBR = @oldPersonNumber
                    SET @logNotes = @logNotes + 'EXT_CORE.ACP_INFRINGER('+ CAST(@acpInfringerCount as varchar)+'),'
                END
             IF @acpServicePersonCount > 0
                BEGIN
                    PRINT 'EXT_CORE.ACP_SERVICE_PERSON: '+ CAST(@acpServicePersonCount as varchar) ;
                    UPDATE EXT_CORE.ACP_SERVICE_PERSON SET SERVICE_PERSON_NBR = @newPersonNumber WHERE SERVICE_PERSON_NBR = @oldPersonNumber
                    SET @logNotes = @logNotes + 'EXT_CORE.ACP_SERVICE_PERSON('+ CAST(@acpServicePersonCount as varchar)+'),'
                END
            IF @logNotes <> ''
                BEGIN
                    SET @logNotes = LEFT(@logNotes, LEN(@logNotes) - 1)
                END
            DECLARE
                @newPersonName                 nvarchar(700),
                @newPersonWCode                varchar(1),
                @newNationalityCountryCode     varchar(2),
                @newAgentCode                  numeric(5),
                @newTelephone                  varchar(200),
                @newEmail                      varchar(200),
                @newGralPersonIdTyp            varchar(4),
                @newGralPersonIdNbr            numeric(15),
                @newAddrStreet                 nvarchar(2000),
                @newAddrZone                   nvarchar(254),
                @newCityName                   nvarchar(254),
                @newCityCode                   varchar(6),
                @newResidenceCountryCode       varchar(2),
                @newStateCode                  varchar(6),
                @newStateName                  varchar(254),
                @newZipcode                    varchar(16);

            SELECT
                   @newPersonName = p.PERSON_NAME,
                   @newPersonWCode = p.PERSON_WCODE,
                   @newNationalityCountryCode = p.NATIONALITY_COUNTRY_CODE,
                   @newAgentCode = p.AGENT_CODE,
                   @newTelephone = p.TELEPHONE,
                   @newEmail = p.EMAIL,
                   @newGralPersonIdTyp = p.GRAL_PERSON_ID_TYP,
                   @newGralPersonIdNbr = p.GRAL_PERSON_ID_NBR,
                   @newAddrStreet = pa.ADDR_STREET,
                   @newAddrZone = pa.ADDR_ZONE,
                   @newCityName = pa.CITY_NAME,
                   @newCityCode = pa.CITY_CODE,
                   @newResidenceCountryCode = pa.RESIDENCE_COUNTRY_CODE,
                   @newStateCode = pa.STATE_CODE,
                   @newStateName = pa.STATE_NAME,
                   @newZipcode = pa.ZIPCODE
            FROM IPASPROD.IP_PERSON_ADDRESSES pa JOIN IPASPROD.IP_PERSON p ON pa.PERSON_NBR = p.PERSON_NBR WHERE pa.PERSON_NBR = @newPersonNumber AND pa.ADDR_NBR = @newAddressNumber;

            DECLARE
                @oldPersonName                 nvarchar(700),
                @oldPersonWCode                varchar(1),
                @oldNationalityCountryCode     varchar(2),
                @oldAgentCode                  numeric(5),
                @oldTelephone                  varchar(200),
                @oldEmail                      varchar(200),
                @oldGralPersonIdTyp            varchar(4),
                @oldGralPersonIdNbr            numeric(15),
                @oldAddrStreet                 nvarchar(2000),
                @oldAddrZone                   nvarchar(254),
                @oldCityName                   nvarchar(254),
                @oldCityCode                   varchar(6),
                @oldResidenceCountryCode       varchar(2),
                @oldStateCode                  varchar(6),
                @oldStateName                  varchar(254),
                @oldZipcode                    varchar(16);

            SELECT
                   @oldPersonName = p.PERSON_NAME,
                   @oldPersonWCode = p.PERSON_WCODE,
                   @oldNationalityCountryCode = p.NATIONALITY_COUNTRY_CODE,
                   @oldAgentCode = p.AGENT_CODE,
                   @oldTelephone = p.TELEPHONE,
                   @oldEmail = p.EMAIL,
                   @oldGralPersonIdTyp = p.GRAL_PERSON_ID_TYP,
                   @oldGralPersonIdNbr = p.GRAL_PERSON_ID_NBR,
                   @oldAddrStreet = pa.ADDR_STREET,
                   @oldAddrZone = pa.ADDR_ZONE,
                   @oldCityName = pa.CITY_NAME,
                   @oldCityCode = pa.CITY_CODE,
                   @oldResidenceCountryCode = pa.RESIDENCE_COUNTRY_CODE,
                   @oldStateCode = pa.STATE_CODE,
                   @oldStateName = pa.STATE_NAME,
                   @oldZipcode = pa.ZIPCODE
            FROM IPASPROD.IP_PERSON_ADDRESSES pa JOIN IPASPROD.IP_PERSON p ON pa.PERSON_NBR = p.PERSON_NBR WHERE pa.PERSON_NBR = @oldPersonNumber AND pa.ADDR_NBR = @oldAddressNumber;

            EXEC deleteIpPerson @personNumber = @oldPersonNumber, @addressNumber = @oldAddressNumber, @usageValidation = 0;

            INSERT INTO EXT_CORE.IP_PERSON_REPLACE_LOG (OLD_PERSON_NBR, OLD_ADDRESS_NBR, NEW_PERSON_NBR, NEW_ADDRESS_NBR, OLD_PERSON_NAME,
                                                        OLD_ADDR_STREET, OLD_CITY_NAME, OLD_NATIONALITY_COUNTRY_CODE, OLD_RESIDENCE_COUNTRY_CODE, OLD_PERSON_WCODE,
                                                        OLD_AGENT_CODE, OLD_TELEPHONE, OLD_EMAIL, OLD_GRAL_PERSON_ID_TYP, OLD_GRAL_PERSON_ID_NBR,
                                                        OLD_ADDR_ZONE, OLD_CITY_CODE, OLD_STATE_CODE, OLD_STATE_NAME, OLD_ZIPCODE,
                                                        NEW_PERSON_NAME, NEW_ADDR_STREET, NEW_CITY_NAME, NEW_NATIONALITY_COUNTRY_CODE, NEW_RESIDENCE_COUNTRY_CODE,
                                                        NEW_PERSON_WCODE, NEW_AGENT_CODE, NEW_TELEPHONE, NEW_EMAIL, NEW_GRAL_PERSON_ID_TYP,
                                                        NEW_GRAL_PERSON_ID_NBR, NEW_ADDR_ZONE, NEW_CITY_CODE, NEW_STATE_CODE, NEW_STATE_NAME, NEW_ZIPCODE, REPLACE_INFO)
            VALUES (@oldPersonNumber, @oldAddressNumber, @newPersonNumber, @newAddressNumber, @oldPersonName,
                    @oldAddrStreet, @oldCityName, @oldNationalityCountryCode, @oldResidenceCountryCode, @oldPersonWCode,
                    @oldAgentCode, @oldTelephone, @oldEmail, @oldGralPersonIdTyp, @oldGralPersonIdNbr,
                    @oldAddrZone, @oldCityCode, @oldStateCode, @oldStateName, @oldZipcode,
                    @newPersonName, @newAddrStreet, @newCityName, @newNationalityCountryCode, @newResidenceCountryCode,
                    @newPersonWCode, @newAgentCode, @newTelephone, @newEmail, @newGralPersonIdTyp,
                    @newGralPersonIdNbr, @newAddrZone, @newCityCode, @newStateCode, @newStateName, @newZipcode, @logNotes);

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


        IF @errorState = 0
            SET @errorState = 1

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



