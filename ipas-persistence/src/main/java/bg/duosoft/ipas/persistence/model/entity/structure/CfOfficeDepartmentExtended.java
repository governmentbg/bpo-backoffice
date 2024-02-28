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
 * Time: 18:11
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "CF_OFFICE_DEPARTMENT", schema = "EXT_USER")
@Cacheable(value = false)
public class CfOfficeDepartmentExtended extends CfOfficeDepartment {
    @Column(name = "IND_INACTIVE")
    private String indInactive;
}
