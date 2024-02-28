package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_OFFIDOC_TYPE_TEMPLATE", schema = "EXT_CORE")
public class CfOffidocTypeTemplate implements Serializable {

    @EmbeddedId
    private CfOffidocTypeTemplatePK pk;

    @Column(name = "NAME_FILE_CONFIG")
    private String nameFileConfig;

}
