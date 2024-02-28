package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "CF_USERDOC_INVALIDATION_RELATION", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfUserdocInvalidationRelation implements Serializable {

    @EmbeddedId
    private CfUserdocInvalidationRelationPK pk;

}
