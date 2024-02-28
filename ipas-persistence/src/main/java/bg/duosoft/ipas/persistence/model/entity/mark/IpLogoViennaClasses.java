package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.util.search.IpFileBridge;
import bg.duosoft.ipas.util.search.IpLogoViennaClassesPkBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_LOGO_VIENNA_CLASSES", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpLogoViennaClasses implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @Field(name = "file.pk", index = Index.YES, analyze = Analyze.NO, store = Store.YES, bridge = @FieldBridge(impl = IpFileBridge.class))
    @SortableField(forField = "file.pk")
    @FieldBridge(impl = IpLogoViennaClassesPkBridge.class)
    @IndexedEmbedded
    private IpLogoViennaClassesPK pk;

    @Column(name = "VIENNA_EDITION_CODE")
    private String viennaEditionCode;

    @Column(name = "VCL_WPUBLISH_VALIDATED")
    private String vclWpublishValidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    private IpLogo logo;

}
