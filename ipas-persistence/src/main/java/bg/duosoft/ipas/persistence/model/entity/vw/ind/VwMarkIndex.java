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
@Table(name = "VW_MARK_INDEX", schema = "EXT_CORE")
@Cacheable(value = false)
public class VwMarkIndex implements VwIpObjectIndex {
    @EmbeddedId
    private IpFilePK pk;

    private VwFileIndex file;

    @Column(name = "IMG")
    private Integer img;

    @Column(name = "NICE_CLASSES")
    private String niceClasses;

    @Column(name = "OWNER_PERSON_NBRS")
    private String ownerPersonNumbers;

    @Column(name = "REPRESENTATIVE_PERSON_NBRS")
    private String representativePersonNumbers;

    @Column(name = "SIGN_WCODE")
    private String signWcode;

    @Column(name = "intregn")
    private String intregn;
}
