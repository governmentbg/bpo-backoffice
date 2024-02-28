package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_OFFIDOC_TYPE_STATIC_TEMPLATE", schema = "EXT_CORE")
public class CfOffidocTypeStaticTemplate implements Serializable {

    @EmbeddedId
    private CfOffidocTypeStaticTemplatePK pk;

}
