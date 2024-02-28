package bg.duosoft.ipas.persistence.model.entity.mark;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "ENOTIF_MARK", schema = "EXT_CORE")
@Cacheable(value = false)
public class EnotifMark implements Serializable {

    @EmbeddedId
    private EnotifMarkPK pk;

    @Column(name = "transaction_code")
    private String transaction;

    @Column(name = "transaction_type")
    private String transcationType;

    @Column(name = "ORIGINAL_LANG")
    private String originalLanguage;

    @Column(name = "ORIGINAL_COUNTRY")
    private String originalCountry;

    @Column(name = "BASIC_REGISTRATION_NUMBER")
    private String basicRegistrationNumber;

    @Column(name = "DESIGNATION_TYPE")
    private String designationType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "GAZNO", referencedColumnName = "GAZNO")
    private Enotif enotif;

}
