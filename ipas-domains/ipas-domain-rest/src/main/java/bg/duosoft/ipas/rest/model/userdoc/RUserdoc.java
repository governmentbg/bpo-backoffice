package bg.duosoft.ipas.rest.model.userdoc;

import bg.duosoft.ipas.rest.model.mark.RInternationalNiceClass;
import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.document.RDocument;
import bg.duosoft.ipas.rest.model.person.RUser;
import bg.duosoft.ipas.rest.model.person.RPerson;
import bg.duosoft.ipas.rest.model.process.RProcessSimpleData;
import bg.duosoft.ipas.rest.model.process.RProcessParentData;
import bg.duosoft.ipas.rest.model.mark.RProtectionData;
import bg.duosoft.ipas.rest.model.file.RFileRecordal;
import bg.duosoft.ipas.rest.model.userdoc.grounds.RUserdocRootGrounds;
import bg.duosoft.ipas.rest.model.userdoc.court_appeal.RUserdocCourtAppeal;
import bg.duosoft.ipas.rest.model.userdoc.reviewers.RUserdocReviewer;
import bg.duosoft.ipas.rest.model.efiling.REFilingData;
import bg.duosoft.ipas.rest.model.mark.RNiceClass;
import bg.duosoft.ipas.rest.model.mark.RUserdocSingleDesign;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdoc implements Serializable {
	private Integer rowVersion;
	private RDocumentId documentId;
	private RDocument document;
	private RUser captureUser;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date captureDate;
	private String applicantNotes;
	private Integer courtDocNbr;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date courtDocDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date decreeDate;
	private RPerson servicePerson;
	private String notes;
	private RUserdocPersonData userdocPersonData;
	private RProcessSimpleData processSimpleData;
	private RUserdocType userdocType;
	private RProcessParentData userdocParentData;
	private RProtectionData protectionData;
	private Boolean indExclusiveLicense;
	private Boolean indCompulsoryLicense;
	private RFileRecordal fileRecordal;
	private List<RUserdocRootGrounds> userdocRootGrounds;
	private List<RUserdocCourtAppeal> userdocCourtAppeals;
	private List<RUserdocReviewer> reviewers;
	private REFilingData userdocEFilingData;
	private RUserdocMainObjectData userdocMainObjectData;
	private List<RUserdocExtraData> userdocExtraData;
	private RUserdocApprovedData approvedData;
	private List<RNiceClass> approvedNiceClassList;
	private List<RUserdocSingleDesign> singleDesigns;
	private List<RInternationalNiceClass> internationalNiceClasses;

	public List<RUserdocRootGrounds> getUserdocRootGrounds() {
		if (userdocRootGrounds == null) {
			userdocRootGrounds = new ArrayList<>();
		}
		return userdocRootGrounds;
	}
	public List<RUserdocCourtAppeal> getUserdocCourtAppeals() {
		if (userdocCourtAppeals == null) {
			userdocCourtAppeals = new ArrayList<>();
		}
		return userdocCourtAppeals;
	}
	public List<RUserdocReviewer> getReviewers() {
		if (reviewers == null) {
			reviewers = new ArrayList<>();
		}
		return reviewers;
	}
	public List<RUserdocExtraData> getUserdocExtraData() {
		if (userdocExtraData == null) {
			userdocExtraData = new ArrayList<>();
		}
		return userdocExtraData;
	}
	public List<RNiceClass> getApprovedNiceClassList() {
		if (approvedNiceClassList == null) {
			approvedNiceClassList = new ArrayList<>();
		}
		return approvedNiceClassList;
	}
	public List<RUserdocSingleDesign> getSingleDesigns() {
		if (singleDesigns == null) {
			singleDesigns = new ArrayList<>();
		}
		return singleDesigns;
	}
	public List<RInternationalNiceClass> getInternationalNiceClasses() {
		if (internationalNiceClasses == null) {
			internationalNiceClasses = new ArrayList<>();
		}
		return internationalNiceClasses;
	}
}

