package bg.duosoft.ipas.persistence.model.entity.userdoc.grounds;


import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocRootGroundsPK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_USERDOC_ROOT_GROUNDS", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpUserdocRootGrounds implements Serializable {

    @EmbeddedId
    private IpUserdocRootGroundsPK pk;

    @Column(name = "MOTIVES")
    private String motives;

    @Column(name = "GROUND_COMMON_TEXT")
    private String groundCommonText;

    @ManyToOne
    @JoinColumn(name = "APPLICANT_AUTHORITY_ID", referencedColumnName = "id")
    private CfApplicantAuthority applicantAuthority;

    @ManyToOne
    @JoinColumn(name = "EARLIER_RIGHT_TYPE_ID", referencedColumnName = "id")
    private CfEarlierRightTypes earlierRightType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ROOT_GROUND_ID", referencedColumnName = "ROOT_GROUND_ID", insertable = false, updatable = false)
    })
    private List<IpUserdocSubGrounds> userdocSubGrounds;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ROOT_GROUND_ID", referencedColumnName = "ROOT_GROUND_ID", insertable = false, updatable = false)
    })
    private List<IpSingleDesignGroundData> singleDesignGroundData;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "rootGround"
    )
    private IpMarkGroundData markGroundData;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "rootGround"
    )
    private IpPatentGroundData patentGroundData;
}
