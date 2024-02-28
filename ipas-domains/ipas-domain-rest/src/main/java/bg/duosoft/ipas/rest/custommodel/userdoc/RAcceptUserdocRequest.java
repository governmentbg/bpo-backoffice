package bg.duosoft.ipas.rest.custommodel.userdoc;


import bg.duosoft.ipas.rest.custommodel.userdoc.grounds.RRootGroundsFoFormat;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.util.RAttachment;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.userdoc.RUserdoc;
import bg.duosoft.ipas.rest.custommodel.userdoc.grounds.RRootGroundsFoFormatNew;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RAcceptUserdocRequest {
    // Class should be renamed from RRootGroundsFoFormatNew to RRootGroundsFoFormat and variable renamed from groundsNew to grounds
    private List<RRootGroundsFoFormatNew> groundsNew;
    // Should be deleted after test
    private List<RRootGroundsFoFormat> grounds;
    private RFileId affectedId;
    private Integer affectedRegistrationNbr;
    private String affectedRegistrationDup;
    private RUserdoc userdoc;
    private List<RAttachment> attachments;
    private Boolean relateRequestToObject;
    private RDocumentId parentDocumentId;
}
