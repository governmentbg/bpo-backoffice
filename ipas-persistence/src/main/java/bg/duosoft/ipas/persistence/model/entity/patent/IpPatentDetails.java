package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_PATENT_DETAILS", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpPatentDetails implements Serializable {

    @EmbeddedId
    private IpFilePK pk;

    @Column(name="DRAWINGS")
    private Integer drawings;

    @Column(name="DRAWING_PUBLICATIONS")
    private Integer drawingPublications;

    @Column(name="DESCRIPTION_PAGES")
    private Integer descriptionPages;

    @Column(name="INVENTIONS_GROUP")
    private Integer inventionsGroup;

    @Column(name="CLAIMS")
    private Integer claims;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<IpPatentAttachment> patentAttachments;

    @Column(name = "NOT_IN_FORCE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notInForceDate;

}
