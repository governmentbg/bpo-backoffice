package bg.duosoft.ipas.persistence.model.entity.vw.ind;

import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.util.search.IpasAnalyzer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 12.03.2021
 * Time: 16:55
 */
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class VwPlantIndex {
    @Column(name = "PROPOSED_DENOMINATION")
    private String proposedDenomination;

    @Column(name = "PROPOSED_DENOMINATION_ENG")
    private String proposedDenominationEng;

    @Column(name = "PUBL_DENOMINATION")
    private String publDenomination;

    @Column(name = "PUBL_DENOMINATION_ENG")
    private String publDenominationEng;

    @Column(name = "APPR_DENOMINATION")
    private String apprDenomination;

    @Column(name = "APPR_DENOMINATION_ENG")
    private String apprDenominationEng;

    @Column(name = "REJ_DENOMINATION")
    private String rejDenomination;

    @Column(name = "REJ_DENOMINATION_ENG")
    private String rejDenominationEng;

    @Column(name = "FEATURES")
    private String features;

    @Column(name = "STABILITY")
    private String stability;

    @Column(name = "TESTING")
    private String testing;

    @Column(name = "TAXON_CODE")
    private String taxonCode;

    @Column(name = "COMMON_CLASSIFY_BUL")
    private String commonClassifyBul;

    @Column(name = "COMMON_CLASSIFY_ENG")
    private String commonClassifyEng;

    @Column(name = "LATIN_CLASSIFY")
    private String latinClassify;
}
