package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 01.06.2021
 * Time: 16:09
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_DD_CATEGORIZATION_TAGS", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfDdCategorizationTags implements Serializable {

    @Id
    private Integer id;

    @Column(name = "FILE_TYP")
    private String fileType;

    @Column(name = "USERDOC_TYP")
    private String userdocType;

    @Column(name = "DOSSIER_TYPE")
    private String dossierType;

    @Column(name = "CATEGORIES")
    private String categories;

    @Column(name = "TAGS")
    private String tags;

    @Column(name = "FETCH_FROM_PARENT")
    private Boolean fetchFromParent;

}
