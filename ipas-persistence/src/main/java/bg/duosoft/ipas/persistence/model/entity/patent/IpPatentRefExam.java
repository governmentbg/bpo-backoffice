package bg.duosoft.ipas.persistence.model.entity.patent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_REF_EXAM", schema = "IPASPROD")
@Cacheable(value = false)
public class IpPatentRefExam implements Serializable {

    private static final long serialVersionUID = 938309247175140273L;

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpPatentRefExamPK pk;

    @Column(name = "REF_CATEG")
    private String refCategory;

    @Column(name = "REF_DESC")
    private String refDescription;

    @Column(name = "REF_CLAIMS")
    private String refClaims;

}
