package bg.duosoft.ipas.integration.decisiondesktop.mapper;

import bg.duosoft.ipas.core.model.decisiondesktop.CDecisionTemplate;
import bg.duosoft.ipas.integration.decisiondesktop.model.admintool.TreeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 27.04.2021
 * Time: 15:15
 */
@Mapper(componentModel = "spring")
public abstract class DecisionTemplateMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "status.value", target = "status")
    public abstract  CDecisionTemplate toCDecisionTemplate(TreeDTO treeDTO);

}
