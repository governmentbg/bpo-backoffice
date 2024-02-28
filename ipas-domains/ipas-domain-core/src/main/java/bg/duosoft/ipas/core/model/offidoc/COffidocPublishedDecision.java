package bg.duosoft.ipas.core.model.offidoc;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class COffidocPublishedDecision implements Serializable {
    private byte[] attachmentContent;
    private String attachmentName;
    private Date decisionDate;

    public boolean isLoaded() {
        return null != this.attachmentContent && 0 != this.attachmentContent.length;
    }
}
