package bg.duosoft.ipas.core.model.file;

import bg.duosoft.ipas.core.model.acp.CAcpPersonsData;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CFile implements Serializable {

    private static final long serialVersionUID = 6335639393544831665L;
    private String notes;
    private String title;
    private Integer rowVersion;
    private COwnershipData ownershipData;
    private CPriorityData priorityData;
    @Valid
    private CFilingData filingData;
    private CRepresentationData representationData;
    @Valid
    private CRegistrationData registrationData;
    private List<CRelationship> relationshipList;
    private CFileId fileId;
    private CProcessId processId;
    private CProcessSimpleData processSimpleData;
    private CPublicationData publicationData;
    private CPerson servicePerson;
    private List<CFileRecordal> fileRecordals;
    private CAcpPersonsData acpPersonsData;

    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public CProcessSimpleData getProcessSimpleData() {
        return processSimpleData;
    }
}


