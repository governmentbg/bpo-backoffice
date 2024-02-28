package bg.duosoft.ipas.persistence.model.entity.mark;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_MARK_ATTACHMENT", schema = "EXT_CORE")
public class IpMarkAttachment implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_SER")
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

    @Column(name = "MIME_TYPE")
    private String mimeType;

    @Column(name = "DATA")
    private byte[] data;

    @Column(name = "COLOUR_DESCRIPTION")
    private String colourDescription;

    @Column(name = "COLOUR_DESCRIPTION_LANG2")
    private String colourDescriptionLang2;

    @Column(name = "IMAGE_VIEW_TITLE")
    private String viewTitle;

    @Column(name = "SEQ_NBR")
    private Integer seqNbr;

    @OneToMany(mappedBy = "markAttachment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IpMarkAttachmentViennaClasses> viennaClassList;
}
