package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.util.search.IpPatentSummaryPKBridge;
import bg.duosoft.ipas.util.search.IpasAnalyzer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_PATENT_SUMMARY", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpPatentSummary implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @FieldBridge(impl = IpPatentSummaryPKBridge.class)
    @IndexedEmbedded
    private IpPatentSummaryPK pk;

    @Column(name = "SUMMARY")
    @Field( analyze = Analyze.YES, index = Index.YES, store = Store.YES, indexNullAs = "", analyzer = @Analyzer(definition = "WordAnalyzer"))
    @Field(name = "summaryExact", analyze = Analyze.NO, index = Index.YES, store = Store.NO, indexNullAs = "", norms = Norms.NO)
    @Field(name = "summaryCustom", analyze = Analyze.YES, index = Index.YES, store = Store.NO, indexNullAs = "", analyzer = @Analyzer(definition = "FullTextAnalyzer"))
    private String summary;
}
