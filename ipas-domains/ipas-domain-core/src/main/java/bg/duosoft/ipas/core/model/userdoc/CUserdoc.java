package bg.duosoft.ipas.core.model.userdoc;

import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.mark.CInternationalNiceClass;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.mark.CProtectionData;
import bg.duosoft.ipas.core.model.mark.CUserdocSingleDesign;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.core.model.userdoc.court_appeal.CUserdocCourtAppeal;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.model.userdoc.reviewers.CUserdocReviewer;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class CUserdoc implements Serializable {
    private static final long serialVersionUID = -4712235627737660629L;
    private Integer rowVersion;
    private CDocumentId documentId;
    private CDocument document;
    private CUser captureUser;
    private Date captureDate;
    private String applicantNotes;
    private Integer courtDocNbr;
    private Date courtDocDate;
    private Date decreeDate;
    private CPerson servicePerson;
    private String notes;
    private CUserdocPersonData userdocPersonData;
    private CProcessSimpleData processSimpleData;
    private CUserdocType userdocType;
    private CProcessParentData userdocParentData;
    private CProtectionData protectionData;
    private Boolean indExclusiveLicense;
    private Boolean indCompulsoryLicense;
    private CFileRecordal fileRecordal;
    private List<CUserdocRootGrounds> userdocRootGrounds;
    private List<CUserdocCourtAppeal> userdocCourtAppeals;
    private List<CUserdocReviewer> reviewers;
    private CEFilingData userdocEFilingData;
    private CUserdocMainObjectData userdocMainObjectData;
    private List<CUserdocExtraData> userdocExtraData;
    private CUserdocPatentData patentData;
    private CUserdocApprovedData approvedData;
    private List<CNiceClass> approvedNiceClassList;
    private List<CInternationalNiceClass> internationalNiceClasses;
    private List<CUserdocSingleDesign> singleDesigns;

    public boolean isElectronicApplication(){
        return Objects.nonNull(this.userdocEFilingData);
    }

    @ObjectDiffProperty(equalsOnlyValueProviderMethod = "getUserdocType")
    public CUserdocType getUserdocType() {
        return userdocType;
    }
}
