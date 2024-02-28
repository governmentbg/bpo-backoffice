package bg.duosoft.ipas.persistence.model.entity.ext.core;

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
@Table(name = "IP_FILE_RECORDAL", schema = "EXT_CORE")
public class IpFileRecordal implements Serializable {

    @EmbeddedId
    private IpFileRecordalPK pk;

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "PROC_NBR")
    private Integer procNbr;

    @Column(name = "ACTION_NBR")
    private Integer actionNbr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE")
    private Date date;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "INVALIDATION_DOC_ORI")
    private String invalidationDocOri;

    @Column(name = "INVALIDATION_DOC_LOG")
    private String invalidationDocLog;

    @Column(name = "INVALIDATION_DOC_SER")
    private Integer invalidationDocSer;

    @Column(name = "INVALIDATION_DOC_NBR")
    private Integer invalidationDocNbr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INVALIDATION_DATE")
    private Date invalidationDate;

    @Column(name = "INVALIDATION_PROC_TYP")
    private String invalidationProcTyp;

    @Column(name = "INVALIDATION_PROC_NBR")
    private Integer invalidationProcNbr;

    @Column(name = "INVALIDATION_ACTION_NBR")
    private Integer invalidationActionNbr;

}
