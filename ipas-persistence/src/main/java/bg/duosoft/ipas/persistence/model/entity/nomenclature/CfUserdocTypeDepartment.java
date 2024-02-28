package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartment;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentExtended;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_USERDOC_TYPE_DEPARTMENT", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfUserdocTypeDepartment implements Serializable {

    @EmbeddedId
    private CfUserdocTypeDepartmentPK pk;

    @OneToOne
    @JoinColumn(name = "OFFICE_DIVISION_CODE", referencedColumnName = "OFFICE_DIVISION_CODE", insertable = false, updatable = false)
    @JoinColumn(name = "OFFICE_DEPARTMENT_CODE", referencedColumnName = "OFFICE_DEPARTMENT_CODE", insertable = false, updatable = false)
    private CfOfficeDepartmentExtended department;

}
