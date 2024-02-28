package bg.duosoft.ipas.persistence.model.entity.vw.ind;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * User: ggeorgiev
 * Date: 12.03.2021
 * Time: 17:02
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "VW_PATENT_INDEX", schema = "EXT_CORE")
@Cacheable(value = false)
public class VwPatentIndex implements VwIpObjectIndex {
    @EmbeddedId
    private IpFilePK pk;

    private VwFileIndex file;

    @Column(name = "OWNER_PERSON_NBRS")
    private String ownerPersonNumbers;

    @Column(name = "REPRESENTATIVE_PERSON_NBRS")
    private String representativePersonNumbers;

    @Column(name = "INVENTOR_PERSON_NBRS")
    private String inventorPersonNumbers;

    private VwPlantIndex plant;

    private VwSpcIndex spc;
}
