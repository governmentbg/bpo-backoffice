package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "RECEPTION_USERDOC_REQUEST", schema = "EXT_RECEPTION")
@Cacheable(value = false)
public class ReceptionUserdocRequest implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "DOC_ORI")
    private String docOri;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "DOC_SEQ_TYP")
    private String docSeqTyp;

    @Column(name = "DOC_SEQ_NBR")
    private Integer docSeqNbr;

    @Column(name = "DOC_SEQ_SER")
    private Integer docSeqSer;

    @Column(name = "USERDOC_TYPE")
    private String userdocType;

    @Column(name = "EXTERNAL_ID")
    private Integer externalId;

    @Column(name = "EXTERNAL_SYSTEM_ID")
    private String externalSystemId;

    @Column(name = "ORIGINAL_EXPECTED")
    private Boolean originalExpected;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILING_DATE")
    private Date filingDate;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "RELATED_OBJECT_SEQ")
    private String relatedObjectSeq;

    @Column(name = "RELATED_OBJECT_TYP")
    private String relatedObjectTyp;

    @Column(name = "RELATED_OBJECT_SER")
    private Integer relatedObjectSer;

    @Column(name = "RELATED_OBJECT_NBR")
    private Integer relatedObjectNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE", insertable = false)
    private Date createDate;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "SUBMISSION_TYPE")
    private SubmissionType submissionType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RECEPTION_USERDOC_REQUEST_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private List<ReceptionUserdocCorrespondent> correspondents;
}
