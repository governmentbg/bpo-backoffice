package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_GEO_COUNTRY", schema = "IPASPROD")
@Cacheable(value = false)
public class CfGeoCountry implements Serializable {

  @Column(name = "ROW_VERSION")
  private Integer rowVersion;

  @Id
  @Column(name = "COUNTRY_CODE")
  @Field(name = "country_code", analyze = Analyze.NO, index= Index.YES, store = Store.YES)
  private String countryCode;

  @Column(name = "COUNTRY_NAME")
  private String countryName;

  @Column(name = "NATIONALITY")
  private String nationality;

  @Column(name = "IND_WIPO_STATISTICS")
  private String indWipoStatistics;

  @Column(name = "XML_DESIGNER")
  private String xmlDesigner;

}
