package bg.duosoft.ipas.persistence.model.entity.mark;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_MARK_SOUND", schema = "IPASPROD")
@Cacheable(value = false)
public class IpMarkSound implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpMarkSoundPK pk;

    @Column(name = "SECTOR_LENGTH")
    private Integer sectorLength;

    @Column(name = "MULTIMEDIA_FORMAT_WCODE")
    private Long multimediaFormatWcode;

    @Column(name = "SECTOR_DATA")
    private String sectorData;

}
