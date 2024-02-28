package bg.duosoft.ipas.persistence.model.entity.ext.spc;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "SPC_EXTENDED", schema = "EXT_CORE")
@Getter
@Setter
@Cacheable(value = false)
public class SpcExtended implements Serializable {

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "BG_PERMIT_NUMBER")
    @Field(index= Index.YES, analyze = Analyze.YES, store = Store.NO, indexNullAs = "")
    private String bgPermitNumber;

    @Column(name = "BG_PERMIT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
    private Date bgPermitDate;

    @Column(name = "EU_PERMIT_NUMBER")
    @Field(index= Index.YES, analyze = Analyze.YES, store = Store.NO, indexNullAs = "")
    private String euPermitNumber;

    @Column(name = "EU_PERMIT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
    private Date euPermitDate;

    @Column(name = "PRODUCT_CLAIMS")
    private String productClaims;

    @Column(name = "BG_NOTIFICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bgNotificationDate;

    @Column(name = "EU_NOTIFICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date euNotificationDate;

}
