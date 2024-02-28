package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassLocarno;
import bg.duosoft.ipas.util.search.IpPatentIpcClassPKBridge;
import bg.duosoft.ipas.util.search.IpPatentLocarnoClassesPKBridge;
import bg.duosoft.ipas.util.search.IpPatentSummaryPKBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_LOCARNO_CLASSES", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpPatentLocarnoClasses implements Serializable {


    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @FieldBridge(impl = IpPatentLocarnoClassesPKBridge.class)
    private IpPatentLocarnoClassesPK pk;

    @Column(name = "LOC_WPUBLISH_VALIDATED")
    private String locWPublishValidated;

    @Column(name = "LOCARNO_EDITION_CODE")
    private String locarnoEditionCode;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "LOCARNO_CLASS_CODE", referencedColumnName = "LOCARNO_CLASS_CODE",insertable = false,updatable = false),
            @JoinColumn(name = "LOCARNO_EDITION_CODE", referencedColumnName = "LOCARNO_EDITION_CODE",insertable = false,updatable = false)
    })
    @IndexedEmbedded
    private CfClassLocarno locarnoClasses;

}
