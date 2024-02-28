package bg.duosoft.ipas.persistence.model.entity.mark;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Raya
 * 27.03.2019
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "EXTENDED_IP_MARK_NICE_CLASSES", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpMarkNiceClassesExt implements Serializable {

    @EmbeddedId
    private IpMarkNiceClassesPK pk;

    @Column(name = "ALL_TERMS_DECLARATION")
    private String allTermsDeclaration;
}
