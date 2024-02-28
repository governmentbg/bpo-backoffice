package bg.duosoft.ipas.persistence.model.entity.ext.agent;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CF_PARTNERSHIP_TYPE", schema = "EXT_AGENT")
@Cacheable(value = false)
public class CfPartnershipType implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_EN")
    private String nameEn;

}
