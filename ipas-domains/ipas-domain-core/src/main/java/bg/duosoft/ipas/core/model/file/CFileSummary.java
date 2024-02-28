package bg.duosoft.ipas.core.model.file;

import bg.duosoft.ipas.core.model.miscellaneous.CStatusId;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CFileSummary implements Serializable {
    private static final long serialVersionUID = -2463428478092459994L;
    private String fileSummaryDescription;
    private String fileSummaryClasses;
    private String fileSummaryOwner;
    private String fileSummaryRepresentative;
    private String fileSummaryStatus;
    private String pctApplicationId;
    private Boolean indMark;
    private Boolean indPatent;
    private String fileIdAsString;
    private Integer similarityPercent;
    private String fileSummaryCountry;
    private String fileSummaryResponsibleName;
    private String workflowWarningText;
    private String fileSummaryDescriptionInOtherLang;
    private String fileSummaryOwnerInOtherLang;
    private String fileSummaryRepresentativeInOtherLang;
    private Integer publicationNbr;
    private String publicationSer;
    private String disclaimer;
    private String disclaimerInOtherLang;
    private String publicationTyp;
    private String selected;
    private List<CUserdoc> cUserdocs;
    private CStatusId statusId;
    private CFilingData filingData;
    private CRegistrationData registrationData;
    private CFileId fileId;

}
