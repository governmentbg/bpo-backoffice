package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class CfClassViennaCategPK implements Serializable {

    @Column(name = "VIENNA_CATEGORY_CODE")
    private Integer viennaCategoryCode;

    @Column(name = "VIENNA_EDITION_CODE")
    private String viennaEditionCode;

}
