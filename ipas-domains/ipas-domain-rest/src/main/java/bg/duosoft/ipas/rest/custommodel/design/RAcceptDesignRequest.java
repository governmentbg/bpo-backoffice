package bg.duosoft.ipas.rest.custommodel.design;


import bg.duosoft.ipas.rest.model.reception.RReception;
import bg.duosoft.ipas.rest.model.util.RAttachment;
import bg.duosoft.ipas.rest.model.patent.RPatent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAcceptDesignRequest {
    private RPatent mainDesign;
    private List<RPatent> singleDesigns;
    private List<RAttachment> attachments;
    private Boolean doNotRegisterInDocflowSystem;
    private List<RReception> userdocReceptions;
    public RAcceptDesignRequest(RPatent mainDesign, List<RPatent> singleDesigns, List<RAttachment> attachments) {
        this.mainDesign = mainDesign;
        this.singleDesigns = singleDesigns;
        this.attachments = attachments;
        this.doNotRegisterInDocflowSystem = false;
    }

    public RAcceptDesignRequest(RPatent mainDesign, List<RPatent> singleDesigns, List<RAttachment> attachments, Boolean doNotRegisterInDocflowSystem) {
        this.mainDesign = mainDesign;
        this.singleDesigns = singleDesigns;
        this.attachments = attachments;
        this.doNotRegisterInDocflowSystem = doNotRegisterInDocflowSystem;
    }
}
