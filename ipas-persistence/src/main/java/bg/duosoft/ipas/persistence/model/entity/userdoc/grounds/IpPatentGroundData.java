package bg.duosoft.ipas.persistence.model.entity.userdoc.grounds;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocRootGroundsPK;
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
@Table(name = "IP_PATENT_GROUND_DATA", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpPatentGroundData implements Serializable {

    @EmbeddedId
    private IpUserdocRootGroundsPK pk;

    @Column(name = "PARTIAL_INVALIDITY")
    private Boolean partialInvalidity;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ROOT_GROUND_ID", referencedColumnName = "ROOT_GROUND_ID", insertable = false, updatable = false)
    })
    private IpUserdocRootGrounds rootGround;

}
