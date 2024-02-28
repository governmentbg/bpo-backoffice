package bg.duosoft.ipas.core.model.patent;

import lombok.Data;

import java.io.Serializable;

@Data
public class CAttachmentTypeBookmarks implements Serializable {
    private String bookmarkName;
    private Boolean bookmarkRequired;
    private Integer bookmarkOrder;
}
