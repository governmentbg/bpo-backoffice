package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.core.model.structure.StructureNode;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeStructureEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

/**
 * User: ggeorgiev
 * Date: 18.7.2019 г.
 * Time: 17:44
 */
public abstract class OfficeStructureBaseMapper {
    @AfterMapping
    public void afterToEntityMapping(StructureNode source, @MappingTarget CfOfficeStructureEntity entity) {
        if (entity.getSignatureUser() != null && entity.getSignatureUser().getUserId() == null) {
            entity.setSignatureUser(null);
        }
    }
}
