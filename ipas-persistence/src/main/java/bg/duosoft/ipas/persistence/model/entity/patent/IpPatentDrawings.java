package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.ext.design.SingleDesignExtended;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_DRAWINGS", schema = "IPASPROD")
@Cacheable(value = false)
public class IpPatentDrawings implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpPatentDrawingsPK pk;

    @Column(name = "IMAGE_FORMAT_WCODE")
    private Long imageFormatWCode;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "DRAWING_DATA")
    private byte[] drawingData;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ",    referencedColumnName = "FILE_SEQ",    insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP",    referencedColumnName = "FILE_TYP",    insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER",    referencedColumnName = "FILE_SER",    insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR",    referencedColumnName = "FILE_NBR",    insertable = false, updatable = false),
            @JoinColumn(name = "DRAWING_NBR", referencedColumnName = "DRAWING_NBR", insertable = false, updatable = false),
    })
    private SingleDesignExtended singleDesignExtended;

}
