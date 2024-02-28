package bg.duosoft.ipas.persistence.model.entity.mark;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_MARK_INTL_REPLACEMENT_NICE_CLASSES", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpMarkIntlReplacementNiceClasses {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpMarkNiceClassesPK pk;

    @Column(name = "NICE_CLASS_DESCRIPTION")
    private String niceClassDescription;

    @Column(name = "NICE_CLASS_EDITION")
    private Long niceClassEdition;

    @Column(name = "NICE_CLASS_DESCR_LANG2")
    private String niceClassDescrLang2;

    @Column(name = "NICE_CLASS_VERSION")
    private String niceClassVersion;

}
