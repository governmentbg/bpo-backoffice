package bg.duosoft.ipas.rest.custommodel.patent;

import bg.duosoft.ipas.rest.model.reception.RReception;
import bg.duosoft.ipas.rest.model.util.RAttachment;
import bg.duosoft.ipas.rest.model.patent.RPatent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAcceptPatentRequest {
    private RPatent patent;
    private List<RAttachment> attachments;
    private List<RReception> userdocReceptions;

    public RAcceptPatentRequest(RPatent patent, List<RAttachment> attachments) {
        this.patent = patent;
        this.attachments = attachments;
    }

    public List<RAttachment> getAttachments() {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        return attachments;
    }
    public List<RReception> getUserdocReceptions() {
        if (userdocReceptions == null) {
            userdocReceptions = new ArrayList<>();
        }
        return userdocReceptions;
    }
}
