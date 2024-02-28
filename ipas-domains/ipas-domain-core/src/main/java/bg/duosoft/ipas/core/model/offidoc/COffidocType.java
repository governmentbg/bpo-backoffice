package bg.duosoft.ipas.core.model.offidoc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class COffidocType implements Serializable {
    private String offidocType;
    private String offidocName;
    private String defaultTemplate;
    private String direction;
    private Boolean hasPublication;
    private List<COffidocTypeTemplate> templates;
    private List<COffidocTypeStaticTemplate> staticTemplates;
}