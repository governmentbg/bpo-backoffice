--liquibase formatted sql

--changeset dveizov:160 splitStatements:false
CREATE PROCEDURE [IPASPROD].[deleteIpPerson] @personNumber numeric(8), @addressNumber numeric(4), @usageValidation bit
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            DECLARE @personCount int;
            SET @personCount = (
                SELECT count(*) FROM IPASPROD.IP_PERSON_ADDRESSES a where a.PERSON_NBR = @personNumber AND a.ADDR_NBR = @addressNumber
            );
            IF @personCount <> 1 -- Check if person exists
                BEGIN
                    THROW 51000, 'Person does not exist !', 1;
                END

            DECLARE @personAddressesCount int;
            SET @personAddressesCount = (
                SELECT COUNT(*) FROM IPASPROD.IP_PERSON_ADDRESSES a WHERE a.PERSON_NBR = @personNumber
            );

            IF @usageValidation = 1
                BEGIN
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

                    exec findIpPersonUse @personNumber = @personNumber, @addressNumber = @addressNumber,
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

                    DECLARE @totalUsageCount int;
                    SET @totalUsageCount = (
                        SELECT @ipMarkOwnersCount +
                               @ipMarkRepresentativesCount + @ipMarkMainOwnerCount + @ipMarkServicePersonCount + @ipPatentOwnersCount +
                               @ipPatentRepresentativesCount + @ipPatentInventorsCount + @ipPatentMainOwnerCount + @ipPatentServicePersonCount +
                               @ipFileMainOwnerCount + @ipFileServicePersonCount + @ipUserdocApplicantCount + @ipUserdocPayeeCount +
                               @ipUserdocPayerCount + @ipUserdocGranteeCount + @ipUserdocGrantorCount + @ipUserdocOldOwnersCount +
                               @ipUserdocNewOwnersCount + @ipUserdocRepresentativeCount + @ipUserdocPersonCount + @ipPersonAbdocsSyncCount +
                               @extReceptionCorrespondentCount + @extReceptionUserdocCorrespondentCount + @agentHistoryCount + @extendedAgentCount +
                               @extendedPersonAddressesCount + @extendedPartnershipCount + @partnershipAgentAgentsCount + @partnershipAgentPartnershipsCount +
                               @partnershipHistoryCount
                    );
                    PRINT 'TOTAL: ' + CAST(@totalUsageCount as varchar);
                    IF @totalUsageCount > 0
                        BEGIN
                            THROW 51000,'Cannot delete person who exists in other tables', 1;
                        END
                END

            PRINT 'Delete IPASPROD.IP_PERSON_ADDRESS: PERSON_NBR = '+ CAST(@personNumber as varchar)+', ADDR_NBR = ' +CAST(@addressNumber as varchar);
            DELETE FROM IPASPROD.IP_PERSON_ADDRESSES WHERE PERSON_NBR = @personNumber and ADDR_NBR = @addressNumber

            IF @personAddressesCount < 2 -- Delete IPASPROD.IP_PERSON record
                BEGIN
                    PRINT 'Delete IPASPROD.IP_PERSON: PERSON_NBR = '+ CAST(@personNumber as varchar) ;
                    DELETE FROM IPASPROD.IP_PERSON WHERE PERSON_NBR = @personNumber
                END
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        DECLARE
            @ErrorSeverity INT,
            @ErrorNumber INT,
            @ErrorMessage NVARCHAR(4000),
            @ErrorState INT,
            @ErrorLine INT,
            @ErrorProc NVARCHAR(200)
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
