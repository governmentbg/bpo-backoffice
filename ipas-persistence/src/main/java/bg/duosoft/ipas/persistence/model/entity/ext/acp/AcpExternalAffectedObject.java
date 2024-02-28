package bg.duosoft.ipas.persistence.model.entity.ext.acp;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "ACP_EXTERNAL_AFFECTED_OBJECT", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpExternalAffectedObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EXTERNAL_ID")
    private String externalId;

    @Column(name = "REGISTRATION_NUMBER")
    private String registrationNumber;

    @Column(name = "NAME")
    private String name;

}
