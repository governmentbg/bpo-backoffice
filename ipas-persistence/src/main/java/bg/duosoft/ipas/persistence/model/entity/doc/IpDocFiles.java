package bg.duosoft.ipas.persistence.model.entity.doc;

import bg.duosoft.ipas.util.search.IpDocFilePKBridge;
import bg.duosoft.ipas.util.search.IpDocPKBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_DOC_FILES", schema = "IPASPROD")
@Cacheable(value = false)
public class IpDocFiles implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @Field(name = "ipDocFilePK", store = Store.YES, analyze = Analyze.NO, bridge = @FieldBridge(impl = IpDocFilePKBridge.class))
    private IpDocFilesPK pk;

    @Column(name = "PRIOR_EXPIRATION_DATE")
    private Date priorExpirationDate;

}
