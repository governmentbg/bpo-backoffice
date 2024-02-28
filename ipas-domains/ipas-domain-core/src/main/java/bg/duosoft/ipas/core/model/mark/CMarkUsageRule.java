package bg.duosoft.ipas.core.model.mark;

import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CMarkUsageRule implements Serializable {
    private Integer id;
    private String name;
    private byte[] content;
    private CUsageRule usageRule;
    private Date dateCreated;
    private boolean contentLoaded;

    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public boolean isContentLoaded() {
        return contentLoaded;
    }
}
