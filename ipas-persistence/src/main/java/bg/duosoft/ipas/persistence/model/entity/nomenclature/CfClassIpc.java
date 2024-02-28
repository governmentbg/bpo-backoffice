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
@Table(name = "CF_CLASS_IPC", schema = "IPASPROD")
@Cacheable(value = false)
public class CfClassIpc implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private CfClassIpcPK pk;

    @Column(name = "IPC_NAME")
    private String ipcName;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @Column(name = "IPC_LATEST_VERSION")
    private String ipcLatestVersion;

}
