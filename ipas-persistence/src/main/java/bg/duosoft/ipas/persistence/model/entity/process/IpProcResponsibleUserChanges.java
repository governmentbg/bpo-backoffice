package bg.duosoft.ipas.persistence.model.entity.process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User: Georgi
 * Date: 5.11.2020 г.
 * Time: 17:29
 */
@Entity
@Table(name = "IP_PROC_RESPONSIBLE_USER_CHANGES", schema = "EXT_CORE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(IpProcResponsibleUserChangesPK.class)
@Cacheable(value = false)
public class IpProcResponsibleUserChanges implements Serializable {

    @Id
    @Column(name = "PROC_TYP")
    private String procTyp;

    @Id
    @Column(name = "PROC_NBR")
    private Integer procNbr;

    @Id
    @Column(name = "CHANGE_NBR")
    private Integer changeNbr;

    @Column(name = "USER_CHANGED")
    private Integer userChanged;

    @Column(name = "DATE_CHANGED")
    private Date dateChanged;

    @Column(name = "OLD_RESPONSIBLE_USER_ID")
    private Integer oldResponsibleUserId;

    @Column(name = "NEW_RESPONSIBLE_USER_ID")
    private Integer newResponsibleUserId;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "ABDOCS_USER_TARGETING_PROCESSED_DATE")
    private Date abdocsUserTargetingProcessedDate;

    public IpProcResponsibleUserChanges(String procTyp,
                                        Integer procNbr,
                                        Integer changeNbr,
                                        Integer userChanged,
                                        Integer oldResponsibleUserId,
                                        Integer newResponsibleUserId) {
        this.procTyp = procTyp;
        this.procNbr = procNbr;
        this.changeNbr = changeNbr;
        this.userChanged = userChanged;
        this.dateChanged = new Date();
        this.oldResponsibleUserId = oldResponsibleUserId;
        this.newResponsibleUserId = newResponsibleUserId;
        this.status = 0;
    }

}
