package bg.duosoft.ipas.core.model.patent;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CAttachmentType implements Serializable {
    private Integer id;
    private String name;
    private String attachmentNameSuffix;
    private Integer attachmentOrder;
    private String attachmentFileTypes;
    private String attachmentExtension;
    private List<CAttachmentTypeBookmarks> bookmarks;
}
