package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "CF_PROCESS_RESULT_TYPE")
@Setter
@Getter
@Cacheable(value = false)
public class CfProcessResultType
        implements Serializable {
    @Id
    @Column(name = "PROCESS_RESULT_TYP")
    private String processResultTyp;
    @Column(name = "ROW_VERSION")
    private Long rowVersion;
    @Column(name = "PROCESS_RESULT_NAME")
    private String processResultName;
    @Column(name = "NICE_CLASS_STATUS_WCODE")
    private String niceClassStatusWcode;
    @Column(name = "NICE_LETTER")
    private String niceLetter;
    @Column(name = "IND_PENDING")
    private String indPending;
    @Column(name = "IND_SUCCESS")
    private String indSuccess;
    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;
    @Column(name = "NICE_COLOUR_CODE")
    private String niceColourCode;
    private static final long serialVersionUID = 1L;
}


