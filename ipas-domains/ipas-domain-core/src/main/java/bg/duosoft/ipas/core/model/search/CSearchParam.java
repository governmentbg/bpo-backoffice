package bg.duosoft.ipas.core.model.search;

import bg.duosoft.ipas.enums.search.PersonNameSearchType;
import bg.duosoft.ipas.enums.search.SearchOperatorTextType;
import bg.duosoft.ipas.enums.search.SearchOperatorType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CSearchParam extends SearchPage implements SearchActionParam, Serializable {

    private String title;

    private SearchOperatorTextType titleTypeSearch;

    private String englishTitle;

    private SearchOperatorTextType englishTitleTypeSearch;

    private String fileSeq;

    private List<String> fileTypes;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> selectedFileTypes;

    private String fromFileSer;

    private String toFileSer;

    private String fromFileNbr;

    private String toFileNbr;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date fromFilingDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date toFilingDate;

    private String fromRegistrationNbr;

    private String toRegistrationNbr;

    private String fromInternationalRegistrationNbr;

    private String toInternationalRegistrationNbr;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date fromRegistrationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date toRegistrationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date fromExpirationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date toExpirationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date fromEntitlementDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date toEntitlementDate;

    private String ownerName;

    private PersonNameSearchType ownerNameTypeSearch;

    private String servicePersonName;

    private PersonNameSearchType servicePersonNameTypeSearch;

    private String inventorName;

    private PersonNameSearchType inventorNameTypeSearch;

    private String representativeName;

    private PersonNameSearchType representativeNameTypeSearch;

    private List<String> statusCodes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date fromStatusDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date toStatusDate;

    private List<String> actionTypes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date fromActionDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date toActionDate;

    private Integer actionResponsibleUserId;

    private Integer actionCaptureUserId;

    private String ownerNationality;

    private String servicePersonNationality;

    private String agentCode;

    private String patentSummary;

    private SearchOperatorTextType patentSummaryTypeSearch;

    private String publicationYear;

    private String publicationBulletin;

    private String publicationSect;

    private List<String> ipcClasses;

    private List<String> cpcClasses;

    private SearchOperatorType ipcClassType = SearchOperatorType.AND;
    private SearchOperatorType cpcClassType = SearchOperatorType.AND;

    private List<String> niceClasses;

    private SearchOperatorType niceClassesType = SearchOperatorType.AND;

    private List<String> viennaClassCodes;

    private SearchOperatorType viennaClassCodeType = SearchOperatorType.AND;

    private String signCode;

    private String bgPermitNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date fromBgPermitDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date toBgPermitDate;

    private String euPermitNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date fromEuPermitDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date toEuPermitDate;

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

    private String fromRequestForValidationNbr;

    private String toRequestForValidationNbr;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date fromRequestForValidationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date toRequestForValidationDate;

    private String requestForValidationType;

    private List<String> locarnoClasses;

    private SearchOperatorType locarnoClassCodeType = SearchOperatorType.AND;

    private String applTyp;

    private String subApplTyp;

    private Integer responsibleUserId;

    public CSearchParam(SearchPage searchPage) {
        super();
        this.page(searchPage.getPage())
                .pageSize(searchPage.getPageSize())
                .sortOrder(searchPage.getSortOrder())
                .sortColumn(searchPage.getSortColumn());
    }

    public CSearchParam title(String title) {
        this.title = title;
        return this;
    }

    public CSearchParam titleTypeSearch(SearchOperatorTextType titleTypeSearch) {
        this.titleTypeSearch = titleTypeSearch;
        return this;
    }

    public CSearchParam englishTitle(String englishTitle) {
        this.englishTitle = englishTitle;
        return this;
    }

    public CSearchParam englishTitleTypeSearch(SearchOperatorTextType englishTitleTypeSearch) {
        this.englishTitleTypeSearch = englishTitleTypeSearch;
        return this;
    }

    public CSearchParam fileSeq(String fileSeq) {
        this.fileSeq = fileSeq;
        return this;
    }

    public CSearchParam fileNbr(String fileNbr) {
        this.fromFileNbr = fileNbr;
        this.toFileNbr = fileNbr;
        return this;
    }
    public CSearchParam fromFileNbr(String fromFileNbr) {
        this.fromFileNbr = fromFileNbr;
        return this;
    }

    public CSearchParam addFileType(String fileType) {
        if (fileTypes == null) {
            fileTypes = new ArrayList<>();
        }
        fileTypes.add(fileType);
        return this;
    }

    public CSearchParam addSelectedFileType(String selectedFileType) {
        if (selectedFileTypes == null) {
            selectedFileTypes = new ArrayList<>();
        }
        selectedFileTypes.add(selectedFileType);
        return this;
    }

    public CSearchParam fileSer(String fileSer) {
        this.fromFileSer = fileSer;
        this.toFileSer = fileSer;
        return this;
    }
    public CSearchParam fromFileSer(String fromFileSer) {
        this.fromFileSer = fromFileSer;
        return this;
    }

    public CSearchParam toFileSer(String toFileSer) {
        this.toFileSer = toFileSer;
        return this;
    }

    public CSearchParam toFileNbr(String toFileNbr) {
        this.toFileNbr = toFileNbr;
        return this;
    }

    public CSearchParam fromFilingDate(Date fromFilingDate) {
        this.fromFilingDate = fromFilingDate;
        return this;
    }

    public CSearchParam toFilingDate(Date toFilingDate) {
        this.toFilingDate = toFilingDate;
        return this;
    }

    public CSearchParam fromRegistrationNbr(String registrationNbr) {
        this.fromRegistrationNbr = registrationNbr;
        return this;
    }

    public CSearchParam toRegistrationNbr(String registrationNbr) {
        this.toRegistrationNbr = registrationNbr;
        return this;
    }

    public CSearchParam fromRegistrationDate(Date registrationDate) {
        this.fromRegistrationDate = registrationDate;
        return this;
    }

    public CSearchParam toRegistrationDate(Date registrationDate) {
        this.toRegistrationDate = registrationDate;
        return this;
    }

    public CSearchParam fromInternationalRegistrationNbr(String internationalRegistrationNbr) {
        this.fromInternationalRegistrationNbr = internationalRegistrationNbr;
        return this;
    }

    public CSearchParam toInternationalRegistrationNbr(String internationalRegistrationNbr) {
        this.toInternationalRegistrationNbr = internationalRegistrationNbr;
        return this;
    }

    public CSearchParam fromExpirationDate(Date expirationDate) {
        this.fromExpirationDate = expirationDate;
        return this;
    }

    public CSearchParam toExpirationDate(Date expirationDate) {
        this.toExpirationDate = expirationDate;
        return this;
    }

    public CSearchParam fromEntitlementDate(Date entitlementDate) {
        this.fromEntitlementDate = entitlementDate;
        return this;
    }

    public CSearchParam toEntitlementDate(Date entitlementDate) {
        this.toEntitlementDate = entitlementDate;
        return this;
    }

    public CSearchParam ownerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public CSearchParam ownerNameTypeSearch(PersonNameSearchType ownerNameTypeSearch) {
        this.ownerNameTypeSearch = ownerNameTypeSearch;
        return this;
    }

    public CSearchParam servicePersonName(String servicePersonName) {
        this.servicePersonName = servicePersonName;
        return this;
    }

    public CSearchParam servicePersonNameTypeSearch(PersonNameSearchType servicePersonNameTypeSearch) {
        this.servicePersonNameTypeSearch = servicePersonNameTypeSearch;
        return this;
    }

    public CSearchParam inventorName(String inventorName) {
        this.inventorName = inventorName;
        return this;
    }

    public CSearchParam inventorNameTypeSearch(PersonNameSearchType inventorNameTypeSearch) {
        this.inventorNameTypeSearch = inventorNameTypeSearch;
        return this;
    }

    public CSearchParam representativeName(String representativeName) {
        this.representativeName = representativeName;
        return this;
    }

    public CSearchParam representativeNameTypeSearch(PersonNameSearchType representativeNameTypeSearch) {
        this.representativeNameTypeSearch = representativeNameTypeSearch;
        return this;
    }

    public CSearchParam statusCodes(List<String> statusCodes) {
        this.statusCodes = statusCodes;
        return this;
    }

    public CSearchParam fromStatusDate(Date fromStatusDate) {
        this.fromStatusDate = fromStatusDate;
        return this;
    }

    public CSearchParam toStatusDate(Date toStatusDate) {
        this.toStatusDate = toStatusDate;
        return this;
    }

    public CSearchParam actionTypes(List<String> actionTypes) {
        this.actionTypes = actionTypes;
        return this;
    }

    public CSearchParam fromActionDate(Date fromActionDate) {
        this.fromActionDate = fromActionDate;
        return this;
    }

    public CSearchParam toActionDate(Date toActionDate) {
        this.toActionDate = toActionDate;
        return this;
    }

    public CSearchParam ownerNationality(String ownerNationality) {
        this.ownerNationality = ownerNationality;
        return this;
    }

    public CSearchParam servicePersonNationality(String servicePersonNationality) {
        this.servicePersonNationality = servicePersonNationality;
        return this;
    }

    public CSearchParam agentCode(String agentCode) {
        this.agentCode = agentCode;
        return this;
    }

    public CSearchParam patentSummary(String patentSummary) {
        this.patentSummary = patentSummary;
        return this;
    }

    public CSearchParam patentSummaryTypeSearch(SearchOperatorTextType patentSummaryTypeSearch) {
        this.patentSummaryTypeSearch = patentSummaryTypeSearch;
        return this;
    }

    public CSearchParam publicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }

    public CSearchParam publicationBulletin(String publicationBulletin) {
        this.publicationBulletin = publicationBulletin;
        return this;
    }

    public CSearchParam publicationSect(String publicationSect) {
        this.publicationSect = publicationSect;
        return this;
    }

    public CSearchParam ipcClasses(List<String> ipcClasses) {
        this.ipcClasses = ipcClasses;
        return this;
    }

    public CSearchParam cpcClasses(List<String> cpcClasses) {
        this.cpcClasses = cpcClasses;
        return this;
    }

    public CSearchParam ipcClassType(SearchOperatorType ipcClassType) {
        this.ipcClassType = ipcClassType;
        return this;
    }
    public CSearchParam cpcClassType(SearchOperatorType cpcClassType) {
        this.cpcClassType = cpcClassType;
        return this;
    }
    public CSearchParam niceClasses(List<String> niceClasses) {
        this.niceClasses = niceClasses;
        return this;
    }

    public CSearchParam niceClassesType(SearchOperatorType niceClassesType) {
        this.niceClassesType = niceClassesType;
        return this;
    }

    public CSearchParam viennaClassCodes(List<String> viennaClassCodes) {
        this.viennaClassCodes = viennaClassCodes;
        return this;
    }

    public CSearchParam viennaClassCodeType(SearchOperatorType viennaClassCodeType) {
        this.viennaClassCodeType = viennaClassCodeType;
        return this;
    }

    public CSearchParam signCode(String signCode) {
        this.signCode = signCode;
        return this;
    }

    public CSearchParam bgPermitNumber(String bgPermitNumber) {
        this.bgPermitNumber = bgPermitNumber;
        return this;
    }

    public CSearchParam fromBgPermitDate(Date fromBgPermitDate) {
        this.fromBgPermitDate = fromBgPermitDate;
        return this;
    }

    public CSearchParam toBgPermitDate(Date toBgPermitDate) {
        this.toBgPermitDate = toBgPermitDate;
        return this;
    }

    public CSearchParam euPermitNumber(String euPermitNumber) {
        this.euPermitNumber = euPermitNumber;
        return this;
    }

    public CSearchParam fromEuPermitDate(Date fromEuPermitDate) {
        this.fromEuPermitDate = fromEuPermitDate;
        return this;
    }

    public CSearchParam toEuPermitDate(Date toEuPermitDate) {
        this.toEuPermitDate = toEuPermitDate;
        return this;
    }

    public CSearchParam taxon(String taxon) {
        this.taxon = taxon;
        return this;
    }

    public CSearchParam proposedDenomination(String proposedDenomination) {
        this.proposedDenomination = proposedDenomination;
        return this;
    }

    public CSearchParam proposedDenominationEng(String proposedDenominationEng) {
        this.proposedDenominationEng = proposedDenominationEng;
        return this;
    }

    public CSearchParam publDenomination(String publDenomination) {
        this.publDenomination = publDenomination;
        return this;
    }

    public CSearchParam publDenominationEng(String publDenominationEng) {
        this.publDenominationEng = publDenominationEng;
        return this;
    }

    public CSearchParam apprDenomination(String apprDenomination) {
        this.apprDenomination = apprDenomination;
        return this;
    }

    public CSearchParam apprDenominationEng(String apprDenominationEng) {
        this.apprDenominationEng = apprDenominationEng;
        return this;
    }

    public CSearchParam rejDenomination(String rejDenomination) {
        this.rejDenomination = rejDenomination;
        return this;
    }

    public CSearchParam rejDenominationEng(String rejDenominationEng) {
        this.rejDenominationEng = rejDenominationEng;
        return this;
    }

    public CSearchParam features(String features) {
        this.features = features;
        return this;
    }

    public CSearchParam stability(String stability) {
        this.stability = stability;
        return this;
    }

    public CSearchParam testing(String testing) {
        this.testing = testing;
        return this;
    }

    public CSearchParam fromRequestForValidationNbr(String fromRequestForValidationNbr) {
        this.fromRequestForValidationNbr = fromRequestForValidationNbr;
        return this;
    }

    public CSearchParam toRequestForValidationNbr(String toRequestForValidationNbr) {
        this.toRequestForValidationNbr = toRequestForValidationNbr;
        return this;
    }

    public CSearchParam fromRequestForValidationDate(Date fromRequestForValidationDate) {
        this.fromRequestForValidationDate = fromRequestForValidationDate;
        return this;
    }

    public CSearchParam toRequestForValidationDate(Date toRequestForValidationDate) {
        this.toRequestForValidationDate = toRequestForValidationDate;
        return this;
    }

    public CSearchParam requestForValidationType(String requestForValidationType) {
        this.requestForValidationType = requestForValidationType;
        return this;
    }

    public CSearchParam locarnoClasses(List<String> locarnoClasses) {
        this.locarnoClasses = locarnoClasses;
        return this;
    }

    public CSearchParam locarnoClassCodeType(SearchOperatorType locarnoClassCodeType) {
        this.locarnoClassCodeType = locarnoClassCodeType;
        return this;
    }

    public CSearchParam applTyp(String applTyp) {
        this.applTyp = applTyp;
        return this;
    }

    public CSearchParam subApplTyp(String subApplTyp) {
        this.subApplTyp = subApplTyp;
        return this;
    }

    public CSearchParam responsibleUserId(Integer responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
        return this;
    }

    public CSearchParam actionResponsibleUserId(Integer actionResponsibleUserId) {
        this.actionResponsibleUserId = actionResponsibleUserId;
        return this;
    }
}