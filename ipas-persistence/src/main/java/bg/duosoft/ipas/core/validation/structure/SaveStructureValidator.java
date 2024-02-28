package bg.duosoft.ipas.core.validation.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.StructureNode;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 18.7.2019 г.
 * Time: 15:39
 */
@Component
public class SaveStructureValidator {
    @Autowired
    protected SimpleUserService userService;


    protected void validateStructureBase(StructureNode structureNode, List<ValidationError> result) {
        if (StringUtils.isEmpty(structureNode.getName())) {
            result.add(ValidationError.builder().pointer("name").messageCode("missing.structure.name").build());
        }
        if (structureNode.getSignatureUser() != null && structureNode.getSignatureUser().getUserId() != null) {
            CUser user = userService.findSimpleUserById(structureNode.getSignatureUser().getUserId());
            if (user == null) {
                result.add(ValidationError.builder().pointer("signatureUser").messageCode("unknown.user.id").build());
            }

        }

    }
}
