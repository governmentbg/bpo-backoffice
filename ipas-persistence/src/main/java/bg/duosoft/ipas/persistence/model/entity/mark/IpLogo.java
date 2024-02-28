package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_LOGO", schema = "IPASPROD")
@Cacheable(value = false)
public class IpLogo implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "IMAGE_FORMAT_WCODE")
    private Long imageFormatWcode;

    @Column(name = "COLOUR_DESCRIPTION")
    private String colourDescription;

    @Column(name = "IND_BASE64")
    private String indBase64;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "LOGO_DATA")
    private byte[] logoData;

    @Column(name = "COLOUR_DESCR_LANG2")
    private String colourdDescrLang2;

    @OneToMany(mappedBy = "logo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IpLogoViennaClasses> ipLogoViennaClassesCollection;

}
