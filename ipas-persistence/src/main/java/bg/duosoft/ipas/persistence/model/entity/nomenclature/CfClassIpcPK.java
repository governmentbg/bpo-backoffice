package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CfClassIpcPK implements Serializable {

    @Column(name = "IPC_EDITION_CODE")
    private String ipcEditionCode;

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

}
