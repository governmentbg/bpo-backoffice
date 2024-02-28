package bg.duosoft.ipas.integration.decisiondesktop.mapper;

import bg.duosoft.ipas.core.model.decisiondesktop.CDecisionContext;
import bg.duosoft.ipas.integration.decisiondesktop.model.drafteditor.DraftingContextDTO;
import org.mapstruct.Mapper;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 27.04.2021
 * Time: 15:44
 */
@Mapper(componentModel = "spring")
public abstract class DecisionContextMapper {

    public CDecisionContext toCDecisionContext(DraftingContextDTO draftingContextDTO){
        CDecisionContext context = new CDecisionContext();
        context.setId(draftingContextDTO.getContext());
        context.setLanguage(draftingContextDTO.getLanguage().getValue());
        if(draftingContextDTO.getVariables() != null && !draftingContextDTO.getVariables().isEmpty()) {
            Map<String, Object> templateVars = (Map)draftingContextDTO.getVariables().get("template_variables");
            context.setTemplateName((String)((Map)templateVars.get("value")).get("templateName"));
        }
        context.setTemplateId(draftingContextDTO.getTreeId());
        context.setCreator(draftingContextDTO.getCreatorUsername());
        return context;
    }

}
