package bg.duosoft.ipas.core.model.mark;

import bg.duosoft.ipas.core.model.acp.*;
import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CMark
        implements Serializable {
    private static final long serialVersionUID = -4429340699051491256L;
    private Boolean indReadAttachments;
    private Integer rowVersion;
    private CMadridApplicationData madridApplicationData;
    private CProtectionData protectionData;
    @Valid
    private CFile file;
    @Valid
    private CSignData signData;
    private CLimitationData limitationData;
    private CRenewalData renewalData;
    private boolean reception;
    @Valid
    private CRelationshipExtended relationshipExtended;
    private Date novelty1Date;
    private Date novelty2Date;
    private CEFilingData markEFilingData;
    private CMarkInternationalReplacement markInternationalReplacement;
    private List<CEnotifMark> enotifMarks;
    private String description;
    private List<CInternationalNiceClass> internationalNiceClasses;
    private List<CMarkUsageRule> usageRules;


    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public Boolean getIndReadAttachments() {
        return indReadAttachments;
    }


    // related to file type = 'A'
    private List<CAcpAffectedObject> acpAffectedObjects;
    private List<CAcpViolationPlace> acpViolationPlaces;
    private CAcpDetails acpDetails;
    private List<CAcpTakenItem> acpTakenItems;
    private List<CAcpCheckReason> acpCheckReasons;
    private CAcpCheckData acpCheckData;
    private CAcpAdministrativePenalty acpAdministrativePenalty;
}



