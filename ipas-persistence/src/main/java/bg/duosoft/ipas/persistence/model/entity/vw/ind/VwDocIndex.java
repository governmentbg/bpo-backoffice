package bg.duosoft.ipas.persistence.model.entity.vw.ind;

import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 02.03.2021
 * Time: 13:31
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "VW_DOC_INDEX", schema = "EXT_CORE")
@Cacheable(value = false)
public class VwDocIndex implements VwIndex {
    @EmbeddedId
    private IpDocPK pk;

    @Column(name = "DOC_SEQ_NBR")
    private Integer docSeqNbr;

    @Column(name = "DOC_SEQ_TYP")
    private String docSeqTyp;

    @Column(name = "EXTERNAL_SYSTEM_ID")
    private String externalSystemId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILING_DATE")
    private Date filingDate;

    private IpProcPK procPk;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    private IpFilePK filePk;

    @Column(name = "FILE_PROC_TYP")
    private String fileProcTyp;

    @Column(name = "FILE_PROC_NBR")
    private Integer fileProcNbr;

    @Column(name = "FILE_STATUS_CODE")
    private String fileStatusCode;

    @Column(name = "USERDOC_TYP")
    private String userdocTyp;

}
