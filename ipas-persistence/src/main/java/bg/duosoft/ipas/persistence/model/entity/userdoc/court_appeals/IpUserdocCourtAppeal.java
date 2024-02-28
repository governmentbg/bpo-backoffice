package bg.duosoft.ipas.persistence.model.entity.userdoc.court_appeals;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocCourtAppealPK;
import bg.duosoft.ipas.persistence.model.entity.ext.legal.Courts;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfJudicialActType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_USERDOC_COURT_APPEAL", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpUserdocCourtAppeal implements Serializable {

    @EmbeddedId
    private IpUserdocCourtAppealPK pk;

    @Column(name = "COURT_CASE_NBR")
    private String courtCaseNbr;

    @Column(name = "COURT_CASE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date courtCaseDate;

    @Column(name = "JUDICIAL_ACT_NBR")
    private String judicialActNbr;

    @Column(name = "JUDICIAL_ACT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date judicialActDate;

    @Column(name = "COURT_LINK")
    private String courtLink;

    @ManyToOne
    @JoinColumn(name = "COURT_ID", referencedColumnName = "CourtId")
    private Courts court;

    @ManyToOne
    @JoinColumn(name = "JUDICIAL_ACT_TYPE_ID", referencedColumnName = "id")
    private CfJudicialActType judicialActType;

}
