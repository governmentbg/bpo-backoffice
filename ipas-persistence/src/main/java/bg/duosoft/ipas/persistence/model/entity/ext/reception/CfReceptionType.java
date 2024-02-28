package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_RECEPTION_TYPE", schema = "EXT_RECEPTION")
@Cacheable(value = false)
public class CfReceptionType implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "SHORT_TITLE")
    private String shortTitle;

    @Column(name = "TITLE_EN")
    private String titleEn;

    @Column(name = "SHORT_TITLE_EN")
    private String shortTitleEn;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "APPL_TYP")
    private String applTyp;

    @Column(name = "RECEPTION_ON_COUNTER")
    private Boolean receptionOnCounter;

    @Column(name = "RECEPTION_FROM_EXISTING_DOCUMENT")
    private Boolean receptionFromExistingDocument;

    @OrderBy(value = "seqNbr ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RECEPTION_TYPE", referencedColumnName = "ID", insertable = false, updatable = false)
    private List<ReceptionTypeAdditionalUserdoc> additionalUserdocs;
}
