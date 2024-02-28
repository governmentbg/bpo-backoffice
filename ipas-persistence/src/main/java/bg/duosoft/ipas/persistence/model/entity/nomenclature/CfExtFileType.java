package bg.duosoft.ipas.persistence.model.entity.nomenclature;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_EXT_FILE_TYPE", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfExtFileType implements Serializable {

    @Id
    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_TYPE_NAME")
    private String name;

    @Column(name = "FILE_TYP_ORDER")
    private Integer order;
}
