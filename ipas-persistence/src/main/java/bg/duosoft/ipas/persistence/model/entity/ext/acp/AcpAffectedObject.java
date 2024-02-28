package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.NumericField;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "ACP_AFFECTED_OBJECTS", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpAffectedObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_SER")
    @NumericField
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

    @Column(name = "AO_FILE_SEQ")
    private String affectedObjectFileSeq;

    @Column(name = "AO_FILE_TYP")
    private String affectedObjectFileTyp;

    @Column(name = "AO_FILE_SER")
    @NumericField
    private Integer affectedObjectFileSer;

    @Column(name = "AO_FILE_NBR")
    private Integer affectedObjectFileNbr;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "AO_FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "AO_FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "AO_FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "AO_FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    @IndexedEmbedded
    private AcpAffectedObjectIpFile affectedObjectData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EXTERNAL_AO_ID", referencedColumnName = "ID")
    private AcpExternalAffectedObject externalAffectedObject;

}
