package com.duosoft.ipas.util.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserdocExtraData {

    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty(value = "EFFECTIVE_DATE")
    private Date effectiveDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty(value = "INVALIDATION_DATE")
    private Date invalidationDate;

    @JsonProperty(value = "SERVICE_SCOPE")
    private Boolean serviceScope;

    @JsonProperty(value = "DESCRIPTION")
    private String description;

    @JsonProperty(value = "BANKRUPTCY_CASE_NUMBER")
    private String bankruptcyCaseNumber;

    @JsonProperty(value = "BANKRUPTCY_COURT_NAME")
    private String bankruptcyCourtName;

    @JsonProperty(value = "SECURITY_MEASURE_PROHIBITION_RIGHTS_USE")
    private Boolean securityMeasureProhibitionRightsUse;

    @JsonProperty(value = "SECURITY_MEASURE_PROHIBITION_RIGHTS_MANAGE")
    private Boolean securityMeasureProhibitionRightsManage;

    @JsonProperty(value = "PLEDGE_SEQUENCE_NUMBER")
    private Integer pledgeSequenceNumber;

    @JsonProperty(value = "PLEDGE_EXPIRATION_DATE")
    private String pledgeExpirationDate;

    @JsonProperty(value = "PLEDGE_AMOUNT")
    private String pledgeAmount;

    @JsonProperty(value = "PLEDGE_INTEREST")
    private String pledgeInterest;

    @JsonProperty(value = "PLEDGE_PENALTY")
    private String pledgePenalty;

    @JsonProperty(value = "PLEDGE_DESCRIPTION")
    private String pledgeDescription;

    @JsonProperty(value = "PLEDGE_ADDITIONAL_DATA")
    private String pledgeAdditionalData;

    @JsonProperty(value = "TRANSFER_INVALIDATION_REASON")
    private String transferInvalidationReason;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty(value = "RENEWAL_NEW_EXPIRATION_DATE")
    private Date renewalNewExpirationDate;

    @JsonProperty(value = "LICENSE_CONTRACT_NUMBER_AND_DATE")
    private String licenseContractNumberAndDate;

    @JsonProperty(value = "LICENSE_TYPE")
    private Boolean licenseType;

    @JsonProperty(value = "LICENSE_IS_COMPULSORY")
    private Boolean licenseIsCompulsory;

    @JsonProperty(value = "LICENSE_TERRITORIAL_SCOPE")
    private Boolean licenseTerritorialScope;

    @JsonProperty(value = "LICENSE_TERRITORIAL_RESTRICTION")
    private String licenseTerritorialRestriction;

    @JsonProperty(value = "LICENSE_SUBLICENSE")
    private Boolean licenseSublicense;

    @JsonProperty(value = "LICENSE_SUBLICENSE_IDENTIFIER")
    private String licenseSublicenseIdentifier;

    @JsonProperty(value = "LICENSE_SUBLICENSE_GRANT_RIGHT")
    private Boolean licenseSublicenseGrantRight;

    @JsonProperty(value = "LICENSE_EXPIRATION_DATE_TYPE")
    private Boolean licenseExpirationDateType;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty(value = "LICENSE_EXPIRATION_DATE")
    private Date licenseExpirationDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonProperty(value = "CLAIM_CONTESTED_DECISION_DATE")
    private Date claimContestedDecisionDate;

    @JsonProperty(value = "CLAIM_CONTESTED_DECISION_NUMBER")
    private Integer claimContestedDecisionNumber;

    @JsonProperty(value = "CLAIM_MOTIVE")
    private String claimMotive;

}
