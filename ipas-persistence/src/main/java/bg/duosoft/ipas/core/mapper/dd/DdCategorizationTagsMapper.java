package bg.duosoft.ipas.core.mapper.dd;

import bg.duosoft.ipas.core.model.decisiondesktop.CDdCategorizationTags;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfDdCategorizationTags;
import org.mapstruct.Mapper;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 01.06.2021
 * Time: 16:33
 */
@Mapper(componentModel = "spring")
public abstract class DdCategorizationTagsMapper {

    public abstract CDdCategorizationTags toCore(CfDdCategorizationTags ddConfigEntry);
}
