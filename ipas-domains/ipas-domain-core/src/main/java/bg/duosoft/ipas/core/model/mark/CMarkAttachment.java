package bg.duosoft.ipas.core.model.mark;

import bg.duosoft.ipas.enums.AttachmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMarkAttachment implements Serializable {
    private Integer id;
    private String mimeType;
    private byte[] data;
    private String colourDescription;
    private String colourDescriptionInOtherLang;
    private String viewTitle;
    private Integer sequenceNumber;
    private List<CViennaClass> viennaClassList;
    private AttachmentType attachmentType;

    public boolean isLoaded() {
        return Objects.nonNull(this.data) && this.data.length > 0;
    }
}


