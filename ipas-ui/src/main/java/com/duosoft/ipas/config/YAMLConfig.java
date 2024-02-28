package com.duosoft.ipas.config;

import bg.duosoft.ipas.enums.search.PersonNameSearchType;
import bg.duosoft.ipas.enums.search.SearchOperatorTextType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Component
@ConfigurationProperties("ipas.properties")
public class YAMLConfig {

    private Integer maxSessionObjects;
    private String logoMaxSize;
    private String userDocsLegalGroundTypesVersion;
    private String audioMaxSize;
    private String videoMaxSize;
    private String logoAllowedTypeRegex;
    private String paymentsURL;
    private Integer maxAutoCompletResults;
    private boolean validateSecurityRoles;
    private Integer niceClassMin;
    private Integer niceClassMax;
    private String abdocsSuperUser;
    private String restServicesAdminExecutionUsername;
    private Search search = new Search();

    @Getter
    @Setter
    public class Search{

        private Integer leadingZero = 6;

        private Userdoc userdoc = new Userdoc();

        private SearchBase mark = new SearchBase();
        private SearchBase internationalMark = new SearchBase();
        private SearchBase gi = new SearchBase();
        private SearchBase acp = new SearchBase();
        private SearchBase plant = new SearchBase();
        private SearchBase design = new SearchBase();
        private SearchBase internationalDesign = new SearchBase();
        private SearchBase patent = new SearchBase();
        private SearchBase patentLike = new SearchBase();
        private SearchBase eupatent = new SearchBase();
        private SearchBase utilityModel = new SearchBase();
        private SearchBase spc = new SearchBase();

        @Getter
        @Setter
        public class SearchBase{
            private Integer leadingZero = Search.this.getLeadingZero();

            private String msgPrefix;
            private String detailPageUrl;
            private List<String> fileTypes = new ArrayList<>();
            private List<String> fileTypeUrls = new ArrayList<>();

            private String fileNbr;
            private String title;
            private SearchOperatorTextType titleSearchType;
            private String englishTitle;
            private SearchOperatorTextType englishTitleSearchType;
            private String responsibleUser;
            private String applTyp;
            private String filingDate;
            private String registrationNbr;
            private String registrationDate;
            private String internationalRegistrationNbr;
            private String expirationDate;
            private String entitlementDate;
            private String owner;
            private String ownerName;
            private String ownerNationality;
            private PersonNameSearchType ownerNameSearchType;
            private String servicePerson;
            private String servicePersonName;
            private String servicePersonNationality;
            private PersonNameSearchType servicePersonNameSearchType;
            private String inventorName;
            private PersonNameSearchType inventorNameSearchType;
            private String representative;
            private String representativeName;
            private PersonNameSearchType representativeNameSearchType;
            private String agentCode;
            private String statusCodes;
            private String actionTypes;
            private String patentSummary;
            private SearchOperatorTextType patentSummarySearchType;
            private String publication;
            private String ipcClasses;
            private String cpcClasses;
            private String niceClasses;
            private String viennaClassCodes;
            private String signCode;
            private String bgPermitNumber;
            private String bgPermitDate;
            private String euPermitNumber;
            private String euPermitDate;
            private String taxon;
            private String proposedDenomination;
            private String proposedDenominationEng;
            private String publDenomination;
            private String publDenominationEng;
            private String apprDenomination;
            private String apprDenominationEng;
            private String rejDenomination;
            private String rejDenominationEng;
            private String features;
            private String stability;
            private String testing;
            private String requestForValidation;
            private String locarnoClasses;
            private String exportFileName;

            public void setTitleSearchType(String str) {
                this.titleSearchType = SearchOperatorTextType.valueOf(str);
            }

            public void setPatentSummarySearchType(String str) {
                this.patentSummarySearchType = SearchOperatorTextType.valueOf(str);
            }

            public String getExportFileName() {
                if(Objects.isNull(exportFileName)) {
                    exportFileName = msgPrefix;
                }
                return exportFileName;
            }
        }
        @Getter
        @Setter
        public class Userdoc{
            private Integer leadingZero = Search.this.getLeadingZero();
        }
    }
}