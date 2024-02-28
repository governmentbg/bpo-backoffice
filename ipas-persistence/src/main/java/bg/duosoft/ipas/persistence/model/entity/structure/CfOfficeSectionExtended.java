package bg.duosoft.ipas.persistence.model.entity.structure;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: ggeorgiev
 * Date: 29.10.2019 г.
 * Time: 17:16
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "CF_OFFICE_SECTION", schema = "EXT_USER")
@Cacheable(value = false)
public class CfOfficeSectionExtended extends CfOfficeSection {
    @Column(name = "IND_INACTIVE")
    private String indInactive;
}
