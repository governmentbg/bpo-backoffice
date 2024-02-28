package bg.duosoft.ipas.persistence.model.entity.userdoc.grounds;

import bg.duosoft.ipas.persistence.model.entity.design.SingleDesign;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpSingleDesignGroundDataPK;
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
@Table(name = "IP_SINGLE_DESIGN_GROUND_DATA", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpSingleDesignGroundData implements Serializable {

    @EmbeddedId
    private IpSingleDesignGroundDataPK pk;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    private SingleDesign singleDesign;

}
