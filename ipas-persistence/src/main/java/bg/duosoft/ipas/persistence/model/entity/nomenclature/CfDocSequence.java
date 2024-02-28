package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Table(name = "CF_DOC_SEQUENCE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfDocSequence implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;
    public CfDocSequence(String docSeqTyp) {
        this.docSeqTyp = docSeqTyp;
    }
    @Id
    @Column(name = "DOC_SEQ_TYP")
    @Field(name = "docSeqTypeCustom", index= Index.YES, analyze = Analyze.NO, store = Store.YES, indexNullAs = "")
    private String docSeqTyp;

    @Column(name = "DOC_SEQ_NAME")
    private String docSeqName;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @Column(name = "IND_ANNUAL_SERIES")
    private String indAnnualSeries;

    @Column(name = "FIXED_SERIES")
    private Integer fixedSeries;

    @Column(name = "IND_CONT_NBR_FOR_FILE")
    private String indContNbrForFile;

}
