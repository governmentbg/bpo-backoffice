package bg.duosoft.ipas.core.model.mark;

import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;
import java.util.Objects;


@Data
public class CLogo
        implements Serializable {
    private static final long serialVersionUID = 8863480926116363655L;
    private String logoType;
    private byte[] logoData;
    private String colourDescription;
    private String colourDescriptionInOtherLang;
    private List<CViennaClass> viennaClassList;

    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public String getBase64EncodedLogo() {
        if (Objects.isNull(this.logoData))
            return null;
        return Base64.getEncoder().encodeToString(this.logoData);
    }

    public boolean isLoaded() {
        return null != this.logoData && 0 != this.logoData.length;
    }
}


