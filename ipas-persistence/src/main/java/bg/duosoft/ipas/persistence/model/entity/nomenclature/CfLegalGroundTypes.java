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
@Table(name = "CF_LEGAL_GROUND_TYPES", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfLegalGroundTypes implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "descr")
    private String description;

    @Column(name = "version")
    private String version;

    @ManyToOne
    @JoinColumn(name = "legal_ground_category", referencedColumnName = "code")
    private CfLegalGroundCategories CfLegalGroundCategory;
}
