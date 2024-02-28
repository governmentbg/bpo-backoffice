package bg.duosoft.ipas.persistence.model.entity.structure;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: ggeorgiev
 * Date: 29.10.2019 г.
 * Time: 18:58
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@Table(name = "CF_OFFICE_DIVISION", schema = "EXT_USER")
@Cacheable(value = false)
public class CfOfficeDivisionExtended extends CfOfficeDivision {
    @Column(name = "IND_INACTIVE")
    private String indInactive;
}
