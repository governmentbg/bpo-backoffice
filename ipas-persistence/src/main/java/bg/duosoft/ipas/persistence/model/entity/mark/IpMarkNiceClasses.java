package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassNice;
import bg.duosoft.ipas.util.search.IpFileBridge;
import bg.duosoft.ipas.util.search.IpMarkNiceClassesPkBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_MARK_NICE_CLASSES", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpMarkNiceClasses implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @Field(name = "file.pk", index = Index.YES, analyze = Analyze.NO, store = Store.YES, bridge = @FieldBridge(impl = IpFileBridge.class))
    @SortableField(forField = "file.pk")
    @FieldBridge(impl = IpMarkNiceClassesPkBridge.class)
    @IndexedEmbedded
    private IpMarkNiceClassesPK pk;

    @Column(name = "NICE_CLASS_DESCRIPTION")
    private String niceClassDescription;

    @Column(name = "NICE_CLASS_EDITION")
    private Long niceClassEdition;

    @Column(name = "NICE_CLASS_DESCR_LANG2")
    private String niceClassDescrLang2;

    @Column(name = "NICE_CLASS_VERSION")
    private String niceClassVersion;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
//            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
//            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
//            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
//    })
//    private IpMark ipMark;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "NICE_CLASS_EDITION", referencedColumnName = "NICE_CLASS_EDITION", insertable = false, updatable = false),
            @JoinColumn(name = "NICE_CLASS_CODE", referencedColumnName = "NICE_CLASS_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "NICE_CLASS_VERSION", referencedColumnName = "NICE_CLASS_VERSION", insertable = false, updatable = false)
    })
    private CfClassNice cfClassNice;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "NICE_CLASS_CODE", referencedColumnName = "NICE_CLASS_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "NICE_CLASS_STATUS_WCODE", referencedColumnName = "NICE_CLASS_STATUS_WCODE", insertable = false, updatable = false)
    })
    private IpMarkNiceClassesExt ipMarkNiceClassesExt;

}
