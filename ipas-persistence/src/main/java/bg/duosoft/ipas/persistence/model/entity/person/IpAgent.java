package bg.duosoft.ipas.persistence.model.entity.person;

import bg.duosoft.ipas.util.search.BooleanToIndInactiveBridge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "IP_AGENT", schema = "IPASPROD")
@Cacheable(value = false)
public class IpAgent implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "AGENT_CODE")
    @Field(name = "agentCodeField", analyze = Analyze.NO, index= Index.YES, store = Store.YES)
    @Field(name = "agentCodeExact", analyze = Analyze.NO, index= Index.YES, store = Store.YES, norms = Norms.NO)
    @FieldBridge(impl = IntegerBridge.class)
    private Integer agentCode;

    @Column(name = "AGENT_NAME")
    private String agentName;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "NOTIFICATION_WCODE")
    private String notificationWcode;

    @Column(name = "AGENT_ALT_CODE")
    private String agentAltCode;

    @Column(name = "IND_INACTIVE")
    @Field(analyze = Analyze.NO, index= Index.YES, store = Store.YES, bridge = @FieldBridge(impl = BooleanToIndInactiveBridge.class))
    private String indInactive;

    @OneToOne(mappedBy = "ipAgent", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private IpPerson ipPerson;

}
