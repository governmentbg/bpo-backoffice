package bg.duosoft.ipas.persistence.model.entity.patent_data;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
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
@Table(name = "IP_USERDOC_PATENT_DATA", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpUserdocPatentData implements Serializable {

    @EmbeddedId
    private IpDocPK pk;

    @Column(name = "TITLE_BG")
    private String titleBg;

    @Column(name = "DESCRIPTION_PAGES_COUNT")
    private Integer descriptionPagesCount;

    @Column(name = "CLAIMS_COUNT")
    private Integer claimsCount;

    @Column(name = "DRAWINGS_COUNT")
    private Integer drawingsCount;
}
