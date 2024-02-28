package bg.duosoft.ipas.persistence.model.entity.ext.plant;


import bg.duosoft.ipas.util.search.IpasAnalyzer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PLANT_TAXON_NOMENCLATURE", schema = "EXT_CORE")
@Getter
@Setter
@Cacheable(value = false)
public class PlantTaxonNomenclature implements Serializable {

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "TAXON_CODE")
  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "")
  @Analyzer(impl = IpasAnalyzer.class)
  private String taxonCode;

  @Column(name = "COMMON_CLASSIFY_BUL")
  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "", analyzer = @Analyzer(impl = IpasAnalyzer.class))
  @Field(name = "commonClassifyBulCustom", analyze = Analyze.YES, index = Index.YES, store = Store.NO, indexNullAs = "", analyzer = @Analyzer(definition = "FullTextAnalyzer"))
  private String commonClassifyBul;

  @Column(name = "COMMON_CLASSIFY_ENG")
  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "", analyzer = @Analyzer(impl = IpasAnalyzer.class))
  @Field(name = "commonClassifyEngCustom", analyze = Analyze.YES, index = Index.YES, store = Store.NO, indexNullAs = "", analyzer = @Analyzer(definition = "FullTextAnalyzer"))
  private String commonClassifyEng;

  @Column(name = "LATIN_CLASSIFY")
  @Field(index= Index.YES, analyze = Analyze.YES, store = Store.YES, indexNullAs = "", analyzer = @Analyzer(impl = IpasAnalyzer.class))
  @Field(name = "latinClassifyCustom", analyze = Analyze.YES, index = Index.YES, store = Store.NO, indexNullAs = "", analyzer = @Analyzer(definition = "FullTextAnalyzer"))
  private String latinClassify;
}
