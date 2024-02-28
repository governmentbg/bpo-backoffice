--liquibase formatted sql

--changeset dveizov:161.1
DROP PROCEDURE IPASPROD.replaceIpPerson
;
--changeset dveizov:161.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[replaceIpPerson] @oldPersonNumber numeric(8),
                                              @oldAddressNumber numeric(4),
                                              @newPersonNumber numeric(8),
                                              @newAddressNumber numeric(4)
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            DECLARE @selectedOldPersonRecordCount int;
            SET @selectedOldPersonRecordCount = (
                SELECT count(*) FROM IPASPROD.IP_PERSON_ADDRESSES a where a.PERSON_NBR = @oldPersonNumber AND a.ADDR_NBR = @oldAddressNumber
            );
            IF @selectedOldPersonRecordCount <> 1 -- Check if person exists
                BEGIN
                    THROW 51000, 'Cannot find person', 1;
                END

            DECLARE @selectedNewPersonRecordCount int;
            SET @selectedNewPersonRecordCount = (
                SELECT count(*) FROM IPASPROD.IP_PERSON_ADDRESSES a where a.PERSON_NBR = @newPersonNumber AND a.ADDR_NBR = @newAddressNumber
            );
            IF @selectedNewPersonRecordCount <> 1 -- Check if action exists
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
                @partnershipHistoryCount int;

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
                 @partnershipHistoryCount = @partnershipHistoryCount OUTPUT

            IF @ipMarkOwnersCount > 0 -- Replace person in IPASPROD.IP_MARK_OWNERS
                BEGIN
                    PRINT 'IPASPROD.IP_MARK_OWNERS: '+ CAST(@ipMarkOwnersCount as varchar) ;
                    UPDATE IPASPROD.IP_MARK_OWNERS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @ipMarkRepresentativesCount > 0 -- Replace person in IPASPROD.IP_MARK_REPR
                BEGIN
                    PRINT 'IPASPROD.IP_MARK_REPR: '+ CAST(@ipMarkRepresentativesCount as varchar) ;
                    UPDATE IPASPROD.IP_MARK_REPRS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @ipMarkMainOwnerCount > 0 -- Replace person in IPASPROD.IP_MARK - MAIN_OWNER
                BEGIN
                    PRINT 'IPASPROD.IP_MARK - MAIN_OWNER: '+ CAST(@ipMarkMainOwnerCount as varchar) ;
                    UPDATE IPASPROD.IP_MARK SET MAIN_OWNER_PERSON_NBR = @newPersonNumber, MAIN_OWNER_ADDR_NBR = @newAddressNumber WHERE MAIN_OWNER_PERSON_NBR = @oldPersonNumber AND MAIN_OWNER_ADDR_NBR = @oldAddressNumber
                END
            IF @ipMarkServicePersonCount > 0 -- Replace person in IPASPROD.IP_MARK - SERVICE_PERSON
                BEGIN
                    PRINT 'IPASPROD.IP_MARK - SERVICE_PERSON: '+ CAST(@ipMarkServicePersonCount as varchar) ;
                    UPDATE IPASPROD.IP_MARK SET SERVICE_PERSON_NBR = @newPersonNumber, SERVICE_ADDR_NBR = @newAddressNumber WHERE SERVICE_PERSON_NBR = @oldPersonNumber AND SERVICE_ADDR_NBR = @oldAddressNumber
                END
            IF @ipPatentOwnersCount > 0 -- Replace person in IPASPROD.IP_PATENT_OWNERS
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT_OWNERS: '+ CAST(@ipPatentOwnersCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT_OWNERS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @ipPatentRepresentativesCount > 0 -- Replace person in IPASPROD.IP_PATENT_REPRS
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT_REPRS: '+ CAST(@ipPatentRepresentativesCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT_REPRS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @ipPatentInventorsCount > 0 -- Replace person in IPASPROD.IP_PATENT_INVENTORS
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT_INVENTORS: '+ CAST(@ipPatentInventorsCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT_INVENTORS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @ipPatentMainOwnerCount > 0 -- Replace person in IPASPROD.IP_PATENT - MAIN_OWNER
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT - MAIN_OWNER: '+ CAST(@ipPatentMainOwnerCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT SET MAIN_OWNER_PERSON_NBR = @newPersonNumber, MAIN_OWNER_ADDR_NBR = @newAddressNumber WHERE MAIN_OWNER_PERSON_NBR = @oldPersonNumber AND MAIN_OWNER_ADDR_NBR = @oldAddressNumber
                END
            IF @ipPatentServicePersonCount > 0 -- Replace person in IPASPROD.IP_PATENT - SERVICE_PERSON
                BEGIN
                    PRINT 'IPASPROD.IP_PATENT - SERVICE_PERSON: '+ CAST(@ipPatentServicePersonCount as varchar) ;
                    UPDATE IPASPROD.IP_PATENT SET SERVICE_PERSON_NBR = @newPersonNumber, SERVICE_ADDR_NBR = @newAddressNumber WHERE SERVICE_PERSON_NBR = @oldPersonNumber AND SERVICE_ADDR_NBR = @oldAddressNumber
                END
            IF @ipFileMainOwnerCount > 0 -- Replace person in IPASPROD.IP_FILE - MAIN_OWNER
                BEGIN
                    PRINT 'IPASPROD.IP_FILE - MAIN_OWNER: '+ CAST(@ipFileMainOwnerCount as varchar) ;
                    UPDATE IPASPROD.IP_FILE SET MAIN_OWNER_PERSON_NBR = @newPersonNumber, MAIN_OWNER_ADDR_NBR = @newAddressNumber WHERE MAIN_OWNER_PERSON_NBR = @oldPersonNumber AND MAIN_OWNER_ADDR_NBR = @oldAddressNumber
                END
            IF @ipFileServicePersonCount > 0 -- Replace person in IPASPROD.IP_FILE - SERVICE_PERSON
                BEGIN
                    PRINT 'IPASPROD.IP_FILE - SERVICE_PERSON: '+ CAST(@ipFileServicePersonCount as varchar) ;
                    UPDATE IPASPROD.IP_FILE SET SERVICE_PERSON_NBR = @newPersonNumber, SERVICE_ADDR_NBR = @newAddressNumber WHERE SERVICE_PERSON_NBR = @oldPersonNumber AND SERVICE_ADDR_NBR = @oldAddressNumber
                END
            IF @ipUserdocApplicantCount > 0 -- Replace person in IPASPROD.IP_USERDOC - APPLICANT
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC - APPLICANT: '+ CAST(@ipUserdocApplicantCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET APPLICANT_PERSON_NBR = @newPersonNumber, APPLICANT_ADDR_NBR = @newAddressNumber WHERE APPLICANT_PERSON_NBR = @oldPersonNumber AND APPLICANT_ADDR_NBR = @oldAddressNumber
                END
            IF @ipUserdocPayeeCount > 0 -- Replace person in IPASPROD.IP_USERDOC - PAYEE
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC - PAYEE: '+ CAST(@ipUserdocPayeeCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET PAYEE_PERSON_NBR = @newPersonNumber, PAYEE_ADDR_NBR = @newAddressNumber WHERE PAYEE_PERSON_NBR = @oldPersonNumber AND PAYEE_ADDR_NBR = @oldAddressNumber
                END
            IF @ipUserdocPayerCount > 0 -- Replace person in IPASPROD.IP_USERDOC - PAYER
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC - PAYER: '+ CAST(@ipUserdocPayerCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET PAYER_PERSON_NBR = @newPersonNumber, PAYER_ADDR_NBR = @newAddressNumber WHERE PAYER_PERSON_NBR = @oldPersonNumber AND PAYER_ADDR_NBR = @oldAddressNumber
                END
            IF @ipUserdocGranteeCount > 0 -- Replace person in IPASPROD.IP_USERDOC - GRANTEE
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC - GRANTEE: '+ CAST(@ipUserdocGranteeCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET GRANTEE_PERSON_NBR = @newPersonNumber, GRANTEE_ADDR_NBR = @newAddressNumber WHERE GRANTEE_PERSON_NBR = @oldPersonNumber AND GRANTEE_ADDR_NBR = @oldAddressNumber
                END
            IF @ipUserdocGrantorCount > 0 -- Replace person in IPASPROD.IP_USERDOC - GRANTOR
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC - GRANTOR: '+ CAST(@ipUserdocGrantorCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC SET GRANTOR_PERSON_NBR = @newPersonNumber, GRANTOR_ADDR_NBR = @newAddressNumber WHERE GRANTOR_PERSON_NBR = @oldPersonNumber AND GRANTOR_ADDR_NBR = @oldAddressNumber
                END
            IF @ipUserdocOldOwnersCount > 0 -- Replace person in IPASPROD.IP_USERDOC_OLD_OWNERS
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC_OLD_OWNERS: '+ CAST(@ipUserdocOldOwnersCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC_OLD_OWNERS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @ipUserdocNewOwnersCount > 0 -- Replace person in IPASPROD.IP_USERDOC_NEW_OWNERS
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC_NEW_OWNERS: '+ CAST(@ipUserdocNewOwnersCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC_NEW_OWNERS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @ipUserdocRepresentativeCount > 0 -- Replace person in IPASPROD.IP_USERDOC_REPRS
                BEGIN
                    PRINT 'IPASPROD.IP_USERDOC_REPRS: '+ CAST(@ipUserdocRepresentativeCount as varchar) ;
                    UPDATE IPASPROD.IP_USERDOC_REPRS SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @ipUserdocPersonCount > 0 -- Replace person in EXT_CORE.IP_USERDOC_PERSON
                BEGIN
                    PRINT 'EXT_CORE.IP_USERDOC_PERSON: '+ CAST(@ipUserdocPersonCount as varchar) ;
                    UPDATE EXT_CORE.IP_USERDOC_PERSON SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @extReceptionCorrespondentCount > 0 -- Replace person in EXT_RECEPTION.CORRESPONDENT
                BEGIN
                    PRINT 'EXT_RECEPTION.CORRESPONDENT: '+ CAST(@extReceptionCorrespondentCount as varchar) ;
                    UPDATE EXT_RECEPTION.CORRESPONDENT SET PERSON_NBR = @newPersonNumber, ADDRESS_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDRESS_NBR = @oldAddressNumber
                END
            IF @extReceptionUserdocCorrespondentCount > 0 -- Replace person in EXT_RECEPTION.USERDOC_CORRESPONDENT
                BEGIN
                    PRINT 'EXT_RECEPTION.USERDOC_CORRESPONDENT: '+ CAST(@extReceptionUserdocCorrespondentCount as varchar) ;
                    UPDATE EXT_RECEPTION.USERDOC_CORRESPONDENT SET PERSON_NBR = @newPersonNumber, ADDRESS_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDRESS_NBR = @oldAddressNumber
                END
            IF @agentHistoryCount > 0 -- Replace person in EXT_AGENT.AGENT_HISTORY
                BEGIN
                    PRINT 'EXT_AGENT.AGENT_HISTORY: '+ CAST(@agentHistoryCount as varchar) ;
                    UPDATE EXT_AGENT.AGENT_HISTORY SET PERSON_NBR = @newPersonNumber WHERE PERSON_NBR = @oldPersonNumber
                END
            IF @extendedAgentCount > 0 -- Replace person in EXT_AGENT.EXTENDED_AGENT
                BEGIN
                    PRINT 'EXT_AGENT.EXTENDED_AGENT: '+ CAST(@extendedAgentCount as varchar) ;
                    UPDATE EXT_AGENT.EXTENDED_AGENT SET PERSON_NBR = @newPersonNumber WHERE PERSON_NBR = @oldPersonNumber
                END
            IF @extendedPersonAddressesCount > 0 -- Replace person in EXT_AGENT.EXTENDED_PERSON_ADDRESSES
                BEGIN
                    PRINT 'EXT_AGENT.EXTENDED_PERSON_ADDRESSES: '+ CAST(@extendedPersonAddressesCount as varchar) ;
                    UPDATE EXT_AGENT.EXTENDED_PERSON_ADDRESSES SET PERSON_NBR = @newPersonNumber, ADDR_NBR = @newAddressNumber WHERE PERSON_NBR = @oldPersonNumber AND ADDR_NBR = @oldAddressNumber
                END
            IF @extendedPartnershipCount > 0 -- Replace person in EXT_AGENT.EXTENDED_PARTNERSHIP
                BEGIN
                    PRINT 'EXT_AGENT.EXTENDED_PARTNERSHIP: '+ CAST(@extendedPartnershipCount as varchar) ;
                    UPDATE EXT_AGENT.EXTENDED_PARTNERSHIP SET PERSON_NBR = @newPersonNumber WHERE PERSON_NBR = @oldPersonNumber
                END
            IF @partnershipAgentAgentsCount > 0 -- Replace person in EXT_AGENT.EXTENDED_PARTNERSHIP - AGENT
                BEGIN
                    PRINT 'EXT_AGENT.EXTENDED_PARTNERSHIP - AGENT: '+ CAST(@partnershipAgentAgentsCount as varchar) ;
                    UPDATE EXT_AGENT.PARTNERSHIP_AGENT SET AGENT_PERSON_NBR = @newPersonNumber WHERE AGENT_PERSON_NBR = @oldPersonNumber
                END
            IF @partnershipAgentPartnershipsCount > 0 -- Replace person in EXT_AGENT.EXTENDED_PARTNERSHIP - PARTNERSHIP
                BEGIN
                    PRINT 'EXT_AGENT.EXTENDED_PARTNERSHIP - PARTNERSHIP: '+ CAST(@partnershipAgentPartnershipsCount as varchar) ;
                    UPDATE EXT_AGENT.PARTNERSHIP_AGENT SET PARTNERSHIP_PERSON_NBR = @newPersonNumber WHERE PARTNERSHIP_PERSON_NBR = @oldPersonNumber
                END
            IF @partnershipHistoryCount > 0 -- Replace person in EXT_AGENT.EXTENDED_PARTNERSHIP
                BEGIN
                    PRINT 'EXT_AGENT.PARTNERSHIP_HISTORY: '+ CAST(@partnershipHistoryCount as varchar) ;
                    UPDATE EXT_AGENT.PARTNERSHIP_HISTORY SET PARTNERSHIP_NBR = @newPersonNumber WHERE PARTNERSHIP_NBR = @oldPersonNumber
                END

            EXEC deleteIpPerson @personNumber = @oldPersonNumber, @addressNumber = @oldAddressNumber, @usageValidation = 0
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        DECLARE
            @ErrorSeverity INT,
            @ErrorNumber   INT,
            @ErrorMessage  NVARCHAR(4000),
            @ErrorState    INT,
            @ErrorLine     INT,
            @ErrorProc     NVARCHAR(200)
        SET @ErrorSeverity = ERROR_SEVERITY()
        SET @ErrorNumber = ERROR_NUMBER()
        SET @ErrorMessage = ERROR_MESSAGE()
        SET @ErrorState = ERROR_STATE()

        -- Not all errors generate an error state, to set to 1 if it's zero
        IF @ErrorState = 0
            SET @ErrorState = 1
        -- If the error renders the transaction as uncommittable or we have open transactions, we may want to rollback
        IF @@TRANCOUNT > 0
            BEGIN
                PRINT 'ROLLBACK'
                ROLLBACK TRANSACTION
            END
        RAISERROR (@ErrorMessage , @ErrorSeverity, @ErrorState, @ErrorNumber)
    END CATCH
    RETURN @@ERROR
END
