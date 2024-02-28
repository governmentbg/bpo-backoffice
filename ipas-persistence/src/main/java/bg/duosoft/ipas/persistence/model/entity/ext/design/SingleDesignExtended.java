package bg.duosoft.ipas.persistence.model.entity.ext.design;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfImageViewType;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentDrawingsPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "SINGLE_DESIGN_EXTENDED", schema = "EXT_CORE")
@Cacheable(value = false)
public class SingleDesignExtended  implements Serializable {

    @EmbeddedId
    private IpPatentDrawingsPK pk;

    @Column(name = "IMAGE_REFUSED")
    private Boolean imageRefused;

    @Column(name = "IMAGE_PUBLISHED")
    private Boolean imagePublished;

    @ManyToOne
    @JoinColumn(name = "VIEW_TYPE_ID", referencedColumnName = "VIEW_TYPE_ID")
    private CfImageViewType imageViewType;

}
