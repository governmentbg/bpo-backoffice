package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_CORRESPONDENT_TYPE", schema = "EXT_RECEPTION")
@Cacheable(value = false)
public class CorrespondentType implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_EN")
    private String nameEn;
}
