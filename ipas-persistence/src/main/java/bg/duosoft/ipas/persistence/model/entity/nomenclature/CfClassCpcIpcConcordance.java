package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_CLASS_CPC_IPC_CONCORDANCE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfClassCpcIpcConcordance implements Serializable {
    @EmbeddedId
    private CfClassCpcIpcConcordancePK pk;

    @Column(name = "CPC_SYMBOL")
    private String cpcSymbol;

    @Column(name = "IPC_SECTION_CODE")
    private String ipcSectionCode;

    @Column(name = "IPC_CLASS_CODE")
    private String ipcClassCode;

    @Column(name = "IPC_SUBCLASS_CODE")
    private String ipcSubclassCode;

    @Column(name = "IPC_GROUP_CODE")
    private String ipcGroupCode;

    @Column(name = "IPC_SUBGROUP_CODE")
    private String ipcSubgroupCode;

    @Column(name = "IPC_SYMBOL")
    private String ipcSymbol;

    @Column(name = "LATEST_VERSION")
    private String latestVersion;
}
