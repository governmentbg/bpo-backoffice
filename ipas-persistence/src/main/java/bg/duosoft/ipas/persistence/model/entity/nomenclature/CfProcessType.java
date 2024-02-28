package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_PROCESS_TYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfProcessType implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "PROC_TYPE_NAME")
    private String procTypeName;

    @Column(name = "RELATED_TO_WCODE")
    private Integer relatedToWcode;

    @Column(name = "PRIMARY_INI_STATUS_CODE")
    private String primaryIniStatusCode;

    @Column(name = "IND_INHERIT_RESPONSIBLE")
    private String indInheritResponsible;

    @Column(name = "SECONDARY_INI_STATUS_CODE")
    private String secondaryIniStatusCode;

    @Column(name = "AUTOMATIC_PROCESS_WCODE")
    private Long automaticProcessWcode;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @Column(name = "IND_USED")
    private String indUsed;

    @Column(name = "FUNCTION_NAME")
    private String functionName;

}
