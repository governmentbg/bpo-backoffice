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
@Table(name = "RECEPTION_REQUEST", schema = "EXT_RECEPTION")
@Cacheable(value = false)
public class ReceptionRequest implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileType;

    @Column(name = "FILE_SER")
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

    @Column(name = "EXTERNAL_ID")
    private Integer externalId;

    @Column(name = "ORIGINAL_EXPECTED")
    private Boolean originalExpected;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILING_DATE")
    private Date filingDate;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "STATUS")
    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE", insertable = false)
    private Date createDate;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "SUBMISSION_TYPE")
    private SubmissionType submissionType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RECEPTION_REQUEST_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private List<ReceptionCorrespondent> correspondents;
}
