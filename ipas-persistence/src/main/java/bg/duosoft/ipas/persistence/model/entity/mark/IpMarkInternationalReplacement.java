package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_MARK_INTL_REPLACEMENT", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpMarkInternationalReplacement {

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "REGISTRATION_NBR")
    private Integer registrationNumber;

    @Column(name = "REGISTRATION_DUP")
    private String registrationDup;

    @Column(name = "REPLACEMENT_FILING_NUMBER")
    private String replacementFilingNumber;

    @Column(name = "ALL_SERVICES")
    private Boolean isAllServices;

    @OrderBy(value = "pk.niceClassCode ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<IpMarkIntlReplacementNiceClasses> replacementNiceClasses;
}
