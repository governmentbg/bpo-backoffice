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
public class CfSubStatusPK implements Serializable {

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    @Column(name = "SUB_STATUS_CODE")
    private Integer subStatusCode;

}
