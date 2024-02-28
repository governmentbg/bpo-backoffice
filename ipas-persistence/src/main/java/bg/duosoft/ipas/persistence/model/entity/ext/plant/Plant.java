package bg.duosoft.ipas.persistence.model.entity.ext.plant;


import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PLANT", schema = "EXT_CORE")
@Getter
@Setter
@Cacheable(value = false)
public class Plant implements Serializable {

  @EmbeddedId
  private IpFilePK pk;

  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Column(name = "PROPOSED_DENOMINATION")
  private String proposedDenomination;

  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Column(name = "PROPOSED_DENOMINATION_ENG")
  private String proposedDenominationEng;

  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Column(name = "PUBL_DENOMINATION")
  private String publDenomination;

  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Column(name = "PUBL_DENOMINATION_ENG")
  private String publDenominationEng;

  @Column(name = "APPR_DENOMINATION")
  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  private String apprDenomination;

  @Column(name = "APPR_DENOMINATION_ENG")
  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  private String apprDenominationEng;

  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Column(name = "REJ_DENOMINATION")
  private String rejDenomination;

  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Column(name = "REJ_DENOMINATION_ENG")
  private String rejDenominationEng;

  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Column(name = "FEATURES")
  private String features;

  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Column(name = "STABILITY")
  private String stability;

  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Column(name = "TESTING")
  private String testing;

  @ManyToOne
  @JoinColumn(name = "PLANT_NUMENCLATURE_ID", referencedColumnName = "ID")
  @IndexedEmbedded
  private PlantTaxonNomenclature plantNumenclature;
}
