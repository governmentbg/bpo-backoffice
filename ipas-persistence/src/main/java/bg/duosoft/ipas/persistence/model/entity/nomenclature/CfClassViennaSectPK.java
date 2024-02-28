package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CfClassViennaSectPK
        implements Serializable {
    @Column(name = "VIENNA_CATEGORY_CODE")
    private Long viennaCategoryCode;
    @Column(name = "VIENNA_DIVISION_CODE")
    private Long viennaDivisionCode;
    @Column(name = "VIENNA_SECTION_CODE")
    private Long viennaSectionCode;
    @Column(name = "VIENNA_EDITION_CODE")
    private String viennaEditionCode;
    private static final long serialVersionUID = 1L;


}


