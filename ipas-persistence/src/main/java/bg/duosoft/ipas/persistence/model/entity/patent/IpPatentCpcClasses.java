package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpc;
import bg.duosoft.ipas.util.search.CfClassCpcBridge;
import bg.duosoft.ipas.util.search.IpFileBridge;
import bg.duosoft.ipas.util.search.IpPatentCpcClassPKBridge;
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
@Table(name = "IP_PATENT_CPC_CLASSES", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpPatentCpcClasses implements Serializable {

    @EmbeddedId
    @Field(name = "file.pk", index = Index.YES, analyze = Analyze.NO, store = Store.YES, bridge = @FieldBridge(impl = IpFileBridge.class))
    @SortableField(forField = "file.pk")
    @FieldBridge(impl = IpPatentCpcClassPKBridge.class)
    @IndexedEmbedded
    private IpPatentCpcClassesPK pk;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Column(name = "CPC_SYMBOL_POSITION")
    private String cpcSymbolPosition;

    @Column(name = "CPC_WPUBLISH_VALIDATED")
    private String cpcWPublishValidated;

    @Column(name = "CPC_SYMBOL_CAPTURE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cpcSymbolCaptureDate;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "CPC_EDITION_CODE", referencedColumnName = "CPC_EDITION_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "CPC_SECTION_CODE", referencedColumnName = "CPC_SECTION_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "CPC_CLASS_CODE", referencedColumnName = "CPC_CLASS_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "CPC_SUBCLASS_CODE", referencedColumnName = "CPC_SUBCLASS_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "CPC_GROUP_CODE", referencedColumnName = "CPC_GROUP_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "CPC_SUBGROUP_CODE", referencedColumnName = "CPC_SUBGROUP_CODE", insertable = false, updatable = false)
    })
    @Field(analyze = Analyze.NO, store = Store.YES, name = "cpcCode")
    @FieldBridge(impl = CfClassCpcBridge.class)
    private CfClassCpc cfClassCpc;
}
