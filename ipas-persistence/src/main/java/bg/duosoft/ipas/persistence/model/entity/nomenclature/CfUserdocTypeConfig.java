package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_USERDOC_TYPE_CONFIG", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfUserdocTypeConfig implements Serializable {
    @Id
    @Column(name = "USERDOC_TYP")
    private String userdocTyp;

    @Column(name = "REGISTER_TO_PROCESS")
    private String registerToProcess;

    @Column(name = "MARK_INHERIT_RESPONSIBLE_USER")
    private String markInheritResponsibleUser;

    @Column(name = "INHERIT_RESPONSIBLE_USER")
    private String inheritResponsibleUser;

    @Column(name = "ABDOCS_USER_TARGETING_ON_REGISTRATION")
    private String abdocsUserTargetingOnRegistration;

    @Column(name = "ABDOCS_USER_TARGETING_ON_RESPONSIBLE_USER_CHANGE")
    private String abdocsUserTargetingOnResponsibleUserChange;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USERDOC_TYP", referencedColumnName = "USERDOC_TYP", insertable = false, updatable = false)
    private List<CfUserdocTypeDepartment> departments;

    @Column(name = "PUBLIC_LIABILITIES")
    private String publicLiabilities;

}
