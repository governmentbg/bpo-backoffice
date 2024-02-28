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
@Table(name = "CF_LEGAL_GROUND_CATEGORIES", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfLegalGroundCategories implements Serializable {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
