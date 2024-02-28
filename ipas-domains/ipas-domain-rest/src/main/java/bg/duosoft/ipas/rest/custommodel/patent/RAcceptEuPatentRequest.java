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
public class RAcceptEuPatentRequest {
    private RPatent euPatent;
    private List<RAttachment> attachments;
    private List<RReception> userdocReceptions;
    private String userdocType;
    private Integer objectNumber;

    public RAcceptEuPatentRequest(RPatent euPatent, List<RAttachment> attachments, String userdocType, Integer objectNumber) {
        this.euPatent = euPatent;
        this.attachments = attachments;
        this.userdocType = userdocType;
        this.objectNumber = objectNumber;
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
