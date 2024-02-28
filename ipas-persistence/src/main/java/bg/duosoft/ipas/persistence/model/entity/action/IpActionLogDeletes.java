package bg.duosoft.ipas.persistence.model.entity.action;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_ACTION_LOG_DELETES", schema = "IPASPROD")
@IdClass(IpActionLogDeletesPK.class)
@Cacheable(value = false)
public class IpActionLogDeletes implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "PROC_TYP")
    private String procTyp;

    @Id
    @Column(name = "PROC_NBR")
    private Integer procNbr;

    @Id
    @Column(name = "LOG_ACTION_NBR")
    private Integer logActionNbr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTION_DATE")
    private Date actionDate;

    @Column(name = "ACTION_TYP")
    private String actionTyp;

    @Column(name = "CAPTURE_USER_ID")
    private Integer captureUserId;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETING_DATE")
    private Date deletingDate;

    @Column(name = "DELETING_USER_ID")
    private Integer deletingUserId;

    @Column(name = "ACTION_NBR")
    private Integer actionNbr;

    @Column(name = "PRIOR_STATUS_CODE")
    private String priorStatusCode;

    @Column(name = "NEW_STATUS_CODE")
    private String newStatusCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PRIOR_DUE_DATE")
    private Date priorDueDate;

    @Column(name = "PRIOR_RESPONSIBLE_USER_ID")
    private Integer priorResponsibleUserId;

    @Column(name = "DELETE_REASON")
    private String deleteReason;

}
