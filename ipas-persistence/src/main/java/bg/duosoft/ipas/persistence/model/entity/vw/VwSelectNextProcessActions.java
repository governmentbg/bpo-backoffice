package bg.duosoft.ipas.persistence.model.entity.vw;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Immutable
@Table(name = "VW_SELECT_NEXT_PROCESS_ACTIONS", schema = "IPASPROD")
@Getter
@EqualsAndHashCode
public class VwSelectNextProcessActions implements Serializable {

    @EmbeddedId
    private VwSelectNextProcessActionsId id;

    @Column(name = "ACTION_TYPE_NAME")
    private String actionTypeName;

    @Column(name = "AUTOMATIC_ACTION_WCODE")
    private Integer automaticActionWcode;

    @Column(name = "ACTION_TYPE_GROUP")
    private String actionTypeGroup;

    @Column(name = "LIST_CODE")
    private String actionTypeListCode;

    @Column(name = "RESTRICT_LAW_CODE")
    private Integer restrictLawCode;

    @Column(name = "RESTRICT_FILE_TYP")
    private String restrictFileTyp;

    @Column(name = "RESTRICT_APPL_TYP")
    private String restrictApplicationType;

    @Column(name = "RESTRICT_APPL_SUBTYP")
    private String restrictApplicationSubType;

    @Column(name = "USERDOC_LIST_CODE_INCLUDE")
    private String userdocListCodeInclude;

    @Column(name = "USERDOC_LIST_CODE_EXCLUDE")
    private String userdocListCodeExclude;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    @Column(name = "STATUS_NAME")
    private String statusName;

    @Column(name = "PROCESS_ACTION_TYPE")
    private String processActionType;

    @Column(name = "CONTAIN_NOTES")
    private int containNotes;

    @Column(name = "CONTAIN_MANUAL_DUE_DATE")
    private int containManualDueDate;

    @Column(name = "CALC_TERM_FROM_ACTION_DATE")
    private int calculateTermFromActionDate;

    @Column(name = "GENERATED_OFFIDOC")
    private String generatedOffidoc;

}
