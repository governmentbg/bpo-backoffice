package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.util.search.IpFileBridge;
import bg.duosoft.ipas.util.search.IpLogoViennaClassesPkBridge;
import bg.duosoft.ipas.util.search.IpMarkAttachmentViennaClassesPkBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_MARK_ATTACHMENT_VIENNA_CLASSES", schema = "EXT_CORE")
@Indexed
public class IpMarkAttachmentViennaClasses implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @Field(name = "file.pk", index = Index.YES, analyze = Analyze.NO, store = Store.YES, bridge = @FieldBridge(impl = IpFileBridge.class))
    @SortableField(forField = "file.pk")
    @FieldBridge(impl = IpMarkAttachmentViennaClassesPkBridge.class)
    @IndexedEmbedded
    private IpMarkAttachmentViennaClassesPK pk;

    @Column(name = "VIENNA_EDITION_CODE")
    private String viennaEditionCode;

    @Column(name = "VCL_WPUBLISH_VALIDATED")
    private String vclWpublishValidated;

    @MapsId(value = "attachmentId")
    @ManyToOne
    @JoinColumn(name = "ATTACHMENT_ID", referencedColumnName = "ID")
    private IpMarkAttachment markAttachment;
}
