package bg.duosoft.ipas.persistence.model.entity.ext;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "ext_liability_details", schema = "IPASPROD")
public class ExtLiabilityDetails extends ExtLiabilityDetailsBase implements Serializable {


}
