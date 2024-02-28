package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpTakenItemStorage;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpTakenItemType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ACP_TAKEN_ITEM", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpTakenItem implements Serializable {

    @EmbeddedId
    private AcpTakenItemPK pk;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    private CfAcpTakenItemType type;

    @Column(name = "TYPE_DESCRIPTION")
    private String typeDescription;

    @Column(name = "COUNT")
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "STORAGE_ID", referencedColumnName = "ID")
    private CfAcpTakenItemStorage storage;

    @Column(name = "STORAGE_DESCRIPTION")
    private String storageDescription;

    @Column(name = "FOR_DESTRUCTION")
    private Boolean forDestruction;

    @Column(name = "RETURNED")
    private Boolean returned;

    @Column(name = "IN_STOCK")
    private Boolean inStock;
}
