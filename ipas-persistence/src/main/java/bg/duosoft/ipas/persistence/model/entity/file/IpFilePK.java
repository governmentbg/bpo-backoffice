package bg.duosoft.ipas.persistence.model.entity.file;

import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import lombok.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Embeddable
public class IpFilePK implements Serializable, FileSeqTypSerNbrPK {

  @Column(name = "FILE_SEQ")
  @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
  private String fileSeq;

  @Column(name = "FILE_TYP")
  @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
  private String fileTyp;

  @Column(name = "FILE_SER")
  @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
  @NumericField
  private Integer fileSer;

  @Column(name = "FILE_NBR")
  @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
  @NumericField(forField = "fileNbr")
  @Field(name = "fileNbrStr",
          index= Index.YES,
          analyze = Analyze.NO,
          store = Store.YES,
          bridge = @FieldBridge(impl = IntegerBridge.class),
          normalizer = @Normalizer(definition = "sortNormalizer"))
  private Integer fileNbr;

}
