package bg.duosoft.ipas.persistence.model.entity.mark;

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
@Table(name = "ENOTIF", schema = "EXT_CORE")
@Cacheable(value = false)
public class Enotif implements Serializable {
    @Id
    @Column(name = "GAZNO")
    private String gazno;

    @Column(name = "NOT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notDate;

    @Column(name = "PUB_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pubDate;

    @Column(name = "WEEKNO")
    private String weekno;

    @Column(name = "NOTLANG")
    private String notLang;

    @Column(name = "PAID_COUNT")
    private Integer paidCount;

    @Column(name = "LICENCE_NEWNAME_COUNT")
    private Integer licenceNewNameCount;

    @Column(name = "LICENCE_BIRTH_COUNT")
    private Integer licenceBirthCount;

    @Column(name = "CREATED_COUNT")
    private Integer createdCount;

    @Column(name = "PROCESSED_COUNT")
    private Integer processedCount;

    @Column(name = "CORRECTION_COUNT")
    private Integer correctionCount;

    @Column(name = "PROLONG_COUNT")
    private Integer prolongCount;

    @Column(name = "NEWBASE_COUNT")
    private Integer newBaseCount;

    @Column(name = "RESTRICT_COUNT")
    private Integer restrictCount;

    @Column(name = "NEWNAME_COUNT")
    private Integer newNameCount;

    @Column(name = "DEATH_COUNT")
    private Integer deathCount;

    @Column(name = "BIRTH_COUNT")
    private Integer birthCount;

}
