package bg.duosoft.ipas.rest.model.search;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.enums.search.*;
import bg.duosoft.ipas.rest.model.search.SearchActionParam;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RSearchParam extends SearchPage implements SearchActionParam {
	private String title;
	private SearchOperatorTextType titleTypeSearch;
	private String englishTitle;
	private SearchOperatorTextType englishTitleTypeSearch;
	private String fileSeq;
	private List<String> fileTypes;
	private List<String> selectedFileTypes;
	private String fromFileSer;
	private String toFileSer;
	private String fromFileNbr;
	private String toFileNbr;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date fromFilingDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date toFilingDate;
	private String fromRegistrationNbr;
	private String toRegistrationNbr;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date fromRegistrationDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date toRegistrationDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date fromExpirationDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date toExpirationDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date fromEntitlementDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date toEntitlementDate;
	private String ownerName;
	private PersonNameSearchType ownerNameTypeSearch;
	private String inventorName;
	private PersonNameSearchType inventorNameTypeSearch;
	private String representativeName;
	private PersonNameSearchType representativeNameTypeSearch;
	private List<String> statusCodes;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date fromStatusDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date toStatusDate;
	private List<String> actionTypes;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date fromActionDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date toActionDate;
	private Integer actionResponsibleUserId;
	private Integer actionCaptureUserId;
	private String ownerNationality;
	private String agentCode;
	private String patentSummary;
	private SearchOperatorTextType patentSummaryTypeSearch;
	private String publicationYear;
	private String publicationBulletin;
	private String publicationSect;
	private List<String> ipcClasses;
	private List<String> cpcClasses;
	private SearchOperatorType ipcClassType;
	private List<String> niceClasses;
	private SearchOperatorType niceClassesType;
	private List<String> viennaClassCodes;
	private SearchOperatorType viennaClassCodeType;
	private String signCode;
	private String bgPermitNumber;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date fromBgPermitDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date toBgPermitDate;
	private String euPermitNumber;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date fromEuPermitDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
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
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date fromRequestForValidationDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date toRequestForValidationDate;
	private String requestForValidationType;
	private List<String> locarnoClasses;
	private SearchOperatorType locarnoClassCodeType;
	private String applTyp;
	private String subApplTyp;
	private Integer responsibleUserId;
	public List<String> getFileTypes() {
		if (fileTypes == null) {
			fileTypes = new ArrayList<>();
		}
		return fileTypes;
	}
	public List<String> getSelectedFileTypes() {
		if (selectedFileTypes == null) {
			selectedFileTypes = new ArrayList<>();
		}
		return selectedFileTypes;
	}
	public List<String> getStatusCodes() {
		if (statusCodes == null) {
			statusCodes = new ArrayList<>();
		}
		return statusCodes;
	}
	public List<String> getActionTypes() {
		if (actionTypes == null) {
			actionTypes = new ArrayList<>();
		}
		return actionTypes;
	}
	public List<String> getIpcClasses() {
		if (ipcClasses == null) {
			ipcClasses = new ArrayList<>();
		}
		return ipcClasses;
	}

	public List<String> getCpcClasses() {
		if (cpcClasses == null) {
			cpcClasses = new ArrayList<>();
		}
		return cpcClasses;
	}

	public List<String> getNiceClasses() {
		if (niceClasses == null) {
			niceClasses = new ArrayList<>();
		}
		return niceClasses;
	}
	public List<String> getViennaClassCodes() {
		if (viennaClassCodes == null) {
			viennaClassCodes = new ArrayList<>();
		}
		return viennaClassCodes;
	}
	public List<String> getLocarnoClasses() {
		if (locarnoClasses == null) {
			locarnoClasses = new ArrayList<>();
		}
		return locarnoClasses;
	}
}

