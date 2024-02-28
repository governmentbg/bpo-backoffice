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
@Table(name = "CF_FILE_TYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfFileType implements Serializable {

  @Column(name = "ROW_VERSION")
  private Integer rowVersion;

  @Id
  @Column(name = "FILE_TYP")
  private String fileTyp;

  @Column(name = "FILE_TYPE_NAME")
  private String fileTypeName;

  @Column(name = "XML_DESIGNER")
  private String xmlDesigner;

  @Column(name = "DOC_SEQ_TYP")
  private String docSeqTyp;

}