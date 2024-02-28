package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassIpc;
import bg.duosoft.ipas.util.search.CfClassIpcBridge;
import bg.duosoft.ipas.util.search.IpFileBridge;
import bg.duosoft.ipas.util.search.IpPatentIpcClassPKBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_IPC_CLASSES", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpPatentIpcClasses implements Serializable {

    @EmbeddedId
    @Field(name = "file.pk", index = Index.YES, analyze = Analyze.NO, store = Store.YES, bridge = @FieldBridge(impl = IpFileBridge.class))
    @SortableField(forField = "file.pk")
    @FieldBridge(impl = IpPatentIpcClassPKBridge.class)
    @IndexedEmbedded
    private IpPatentIpcClassesPK pk;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Column(name = "IPC_SYMBOL_POSITION")
    private String ipcSymbolPosition;

    @Column(name = "IPC_WPUBLISH_VALIDATED")
    private String ipcWPublishValidated;

    @Column(name = "IPC_SYMBOL_CAPTURE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ipcSymbolCaptureDate;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "IPC_EDITION_CODE", referencedColumnName = "IPC_EDITION_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "IPC_SECTION_CODE", referencedColumnName = "IPC_SECTION_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "IPC_CLASS_CODE", referencedColumnName = "IPC_CLASS_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "IPC_SUBCLASS_CODE", referencedColumnName = "IPC_SUBCLASS_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "IPC_GROUP_CODE", referencedColumnName = "IPC_GROUP_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "IPC_SUBGROUP_CODE", referencedColumnName = "IPC_SUBGROUP_CODE", insertable = false, updatable = false)
    })
    @Field(analyze = Analyze.NO, store = Store.YES, name = "ipcCode")
    @FieldBridge(impl = CfClassIpcBridge.class)
    private CfClassIpc cfClassIpc;
}
