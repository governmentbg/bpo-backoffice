--liquibase formatted sql

--changeset dveizov:155.1
DROP PROCEDURE IPASPROD.findIpPersonUse
;
--changeset dveizov:155.2 splitStatements:false
CREATE PROCEDURE [IPASPROD].[findIpPersonUse] @personNumber numeric(8),
                                              @addressNumber numeric(4),
                                              @ipMarkOwnersCount int OUTPUT,
                                              @ipMarkRepresentativesCount int OUTPUT,
                                              @ipMarkMainOwnerCount int OUTPUT,
                                              @ipMarkServicePersonCount int OUTPUT,
                                              @ipPatentOwnersCount int OUTPUT,
                                              @ipPatentRepresentativesCount int OUTPUT,
                                              @ipPatentInventorsCount int OUTPUT,
                                              @ipPatentMainOwnerCount int OUTPUT,
                                              @ipPatentServicePersonCount int OUTPUT,
                                              @ipFileMainOwnerCount int OUTPUT,
                                              @ipFileServicePersonCount int OUTPUT,
                                              @ipUserdocApplicantCount int OUTPUT,
                                              @ipUserdocPayeeCount int OUTPUT,
                                              @ipUserdocPayerCount int OUTPUT,
                                              @ipUserdocGranteeCount int OUTPUT,
                                              @ipUserdocGrantorCount int OUTPUT,
                                              @ipUserdocOldOwnersCount int OUTPUT,
                                              @ipUserdocNewOwnersCount int OUTPUT,
                                              @ipUserdocRepresentativeCount int OUTPUT,
                                              @ipUserdocPersonCount int OUTPUT,
                                              @ipPersonAbdocsSyncCount int OUTPUT,
                                              @extReceptionCorrespondentCount int OUTPUT,
                                              @extReceptionUserdocCorrespondentCount int OUTPUT,
                                              @agentHistoryCount int OUTPUT,
                                              @extendedAgentCount int OUTPUT,
                                              @extendedPersonAddressesCount int OUTPUT,
                                              @extendedPartnershipCount int OUTPUT,
                                              @partnershipAgentAgentsCount int OUTPUT,
                                              @partnershipAgentPartnershipsCount int OUTPUT,
                                              @partnershipHistoryCount int OUTPUT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            SET @ipMarkOwnersCount = (
                SELECT count(*) FROM IPASPROD.IP_MARK_OWNERS a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @ipMarkRepresentativesCount = (
                SELECT count(*) FROM IPASPROD.IP_MARK_REPRS a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @ipMarkMainOwnerCount = (
                SELECT count(*) FROM IPASPROD.IP_MARK a where a.MAIN_OWNER_PERSON_NBR = @personNumber AND a.MAIN_OWNER_ADDR_NBR = @addressNumber
            )

            SET @ipMarkServicePersonCount = (
                SELECT count(*) FROM IPASPROD.IP_MARK a where a.SERVICE_PERSON_NBR = @personNumber AND a.SERVICE_ADDR_NBR = @addressNumber
            )

            SET @ipPatentOwnersCount = (
                SELECT count(*) FROM IPASPROD.IP_PATENT_OWNERS a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @ipPatentRepresentativesCount = (
                SELECT count(*) FROM IPASPROD.IP_PATENT_REPRS a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @ipPatentInventorsCount = (
                SELECT count(*) FROM IPASPROD.IP_PATENT_INVENTORS a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @ipPatentMainOwnerCount = (
                SELECT count(*) FROM IPASPROD.IP_PATENT a where a.MAIN_OWNER_PERSON_NBR = @personNumber AND a.MAIN_OWNER_ADDR_NBR = @addressNumber
            )

            SET @ipPatentServicePersonCount = (
                SELECT count(*) FROM IPASPROD.IP_PATENT a where a.SERVICE_PERSON_NBR = @personNumber AND a.SERVICE_ADDR_NBR = @addressNumber
            )

            SET @ipFileMainOwnerCount = (
                SELECT count(*) FROM IPASPROD.IP_FILE a where a.MAIN_OWNER_PERSON_NBR = @personNumber AND a.MAIN_OWNER_ADDR_NBR = @addressNumber
            )

            SET @ipFileServicePersonCount = (
                SELECT count(*) FROM IPASPROD.IP_FILE a where a.SERVICE_PERSON_NBR = @personNumber AND a.SERVICE_ADDR_NBR = @addressNumber
            )

            SET @ipUserdocApplicantCount = (
                SELECT count(*) FROM IPASPROD.IP_USERDOC a where a.APPLICANT_PERSON_NBR = @personNumber AND a.APPLICANT_ADDR_NBR = @addressNumber
            )

            SET @ipUserdocPayeeCount = (
                SELECT count(*) FROM IPASPROD.IP_USERDOC a where a.PAYEE_PERSON_NBR = @personNumber AND a.PAYEE_ADDR_NBR = @addressNumber
            )

            SET @ipUserdocPayerCount = (
                SELECT count(*) FROM IPASPROD.IP_USERDOC a where a.PAYER_PERSON_NBR = @personNumber AND a.PAYER_ADDR_NBR = @addressNumber
            )

            SET @ipUserdocGranteeCount = (
                SELECT count(*) FROM IPASPROD.IP_USERDOC a where a.GRANTEE_PERSON_NBR = @personNumber AND a.GRANTEE_ADDR_NBR = @addressNumber
            )

            SET @ipUserdocGrantorCount = (
                SELECT count(*) FROM IPASPROD.IP_USERDOC a where a.GRANTOR_PERSON_NBR = @personNumber AND a.GRANTOR_ADDR_NBR = @addressNumber
            )

            SET @ipUserdocOldOwnersCount = (
                SELECT count(*) FROM IPASPROD.IP_USERDOC_OLD_OWNERS a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @ipUserdocNewOwnersCount = (
                SELECT count(*) FROM IPASPROD.IP_USERDOC_NEW_OWNERS a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @ipUserdocRepresentativeCount = (
                SELECT count(*) FROM IPASPROD.IP_USERDOC_REPRS a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @ipUserdocPersonCount = (
                SELECT count(*) FROM EXT_CORE.IP_USERDOC_PERSON a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @ipPersonAbdocsSyncCount = (
                SELECT count(*) FROM EXT_CORE.IP_PERSON_ABDOCS_SYNC a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @extReceptionCorrespondentCount = (
                SELECT count(*) FROM EXT_RECEPTION.CORRESPONDENT a where a.PERSON_NBR = @personNumber AND a.ADDRESS_NBR = @addressNumber
            )

            SET @extReceptionUserdocCorrespondentCount = (
                SELECT count(*) FROM EXT_RECEPTION.USERDOC_CORRESPONDENT a where a.PERSON_NBR = @personNumber AND a.ADDRESS_NBR = @addressNumber
            )

            SET @agentHistoryCount = (
                SELECT count(*) FROM EXT_AGENT.AGENT_HISTORY a where a.PERSON_NBR = @personNumber
            )

            SET @extendedAgentCount = (
                SELECT count(*) FROM EXT_AGENT.EXTENDED_AGENT a where a.PERSON_NBR = @personNumber
            )

            SET @extendedPersonAddressesCount = (
                SELECT count(*) FROM EXT_AGENT.EXTENDED_PERSON_ADDRESSES a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            )

            SET @extendedPartnershipCount = (
                SELECT count(*) FROM EXT_AGENT.EXTENDED_PARTNERSHIP a where a.PERSON_NBR = @personNumber
            )

            SET @partnershipAgentAgentsCount = (
                SELECT count(*) FROM EXT_AGENT.PARTNERSHIP_AGENT a where a.AGENT_PERSON_NBR = @personNumber
            )

            SET @partnershipAgentPartnershipsCount = (
                SELECT count(*) FROM EXT_AGENT.PARTNERSHIP_AGENT a where a.PARTNERSHIP_PERSON_NBR = @personNumber
            )

            SET @partnershipHistoryCount = (
                SELECT count(*) FROM EXT_AGENT.PARTNERSHIP_HISTORY a where a.PARTNERSHIP_NBR = @personNumber
            )

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
