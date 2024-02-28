package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfOffidocTypeStaticTemplate;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfOffidocTypeTemplate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_OFFIDOC_TYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfOffidocType implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "OFFIDOC_TYP")
    private String offidocTyp;

    @Column(name = "OFFIDOC_NAME")
    private String offidocName;

    @Column(name = "NAME_WFILE")
    private String nameWfile;

    @Column(name = "IND_INCLUDE_FIGURE")
    private String indIncludeFigure;

    @Column(name = "GENERATE_PROC_TYP")
    private String generateProcTyp;

    @Column(name = "IND_RESPONSE_REQ")
    private String indResponseReq;

    @Column(name = "SIGNATURE_LEVEL_WCODE")
    private Integer signatureLevelWcode;

    @Column(name = "IND_LAYOUT_FOR_USERDOC_TYPE")
    private String indLayoutForUserdocType;

    @Column(name = "OFFIDOC_GROUP_DESCRIPTION")
    private String offidocGroupDescription;

    @Column(name = "IND_LAYOUT_FOR_FILE_TYPE")
    private String indLayoutForFileType;

    @Column(name = "IND_LAYOUT_FOREACH_LAW")
    private String indLayoutForeachLaw;

    @Column(name = "OFFIDOC_SUBTYP_WCODE")
    private Integer offidocSubtypWcode;

    @Column(name = "IND_TRIGGER_FREEZE_FILE_PROC")
    private String indTriggerFreezeFileProc;

    @Column(name = "IND_FREEZE_NO_OFFIDOC")
    private String indFreezeNoOffidoc;

    @Column(name = "IND_FREEZE_CONTINUE_WHEN_END")
    private String indFreezeContinueWhenEnd;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @Column(name = "EDOC_TYP")
    private String edocTyp;

    @Column(name = "RESPONSE_USERDOC_TYP")
    private String responseUserdocTyp;

    @Column(name = "IND_FLAG1")
    private String indFlag1;

    @Column(name = "IND_FLAG2")
    private String indFlag2;

    @Column(name = "IND_FLAG3")
    private String indFlag3;

    @Column(name = "IND_FLAG4")
    private String indFlag4;

    @Column(name = "IND_FLAG5")
    private String indFlag5;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "OFFIDOC_TYP", referencedColumnName = "OFFIDOC_TYP", insertable = false, updatable = false)
    private List<CfOffidocTypeTemplate> templates;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "OFFIDOC_TYP", referencedColumnName = "OFFIDOC_TYP", insertable = false, updatable = false)
    private List<CfOffidocTypeStaticTemplate> staticTemplates;

}
