package bg.duosoft.ipas.core.model.patent;

import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
public class CPatentAttachment implements Serializable {
    private Integer id;
    private CAttachmentType attachmentType;
    private String attachmentName;
    @EqualsAndHashCode.Exclude private boolean contentLoaded;
    private byte[] attachmentContent;
    private Date dateCreated;
    private Boolean hasBookmarks;
    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public boolean isContentLoaded() {
        return contentLoaded;
    }
}
