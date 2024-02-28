package bg.duosoft.ipas.persistence.model.entity.doc;

import lombok.*;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@ToString
public class IpDocPK implements Serializable {

  @Column(name = "DOC_ORI")
  @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
  private String docOri;

  @Column(name = "DOC_LOG")
  @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
  private String docLog;

  @Column(name = "DOC_SER")
  @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
  @NumericField
  private Integer docSer;

  @Column(name = "DOC_NBR")
  @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
  @NumericField
  private Integer docNbr;

}
