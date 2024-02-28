package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_APPLICANT_TYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfApplicantType implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "APPLICANT_TYP")
    private BigInteger applicantTyp;

    @Column(name = "APPLICANT_TYPE_NAME")
    private String applicantTypeName;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;


}
