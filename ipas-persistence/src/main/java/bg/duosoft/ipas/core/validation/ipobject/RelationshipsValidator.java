package bg.duosoft.ipas.core.validation.ipobject;


import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: Georgi
 * Date: 13.12.2019 г.
 * Time: 14:38
 */
@Component
public class RelationshipsValidator implements IpasValidator<CFile> {
    @Autowired
    private RelationshipValidator relationshipValidator;

    @Override
    public List<ValidationError> validate(CFile file, Object... additionalArgs) {
        if (file.getRelationshipList() != null) {
            List<ValidationError> res = file.getRelationshipList().stream().map(r -> relationshipValidator.validate(r, file)).filter(Objects::nonNull).flatMap(r -> r.stream()).collect(Collectors.toList());
            if (file.getRelationshipList().size() != new HashSet<>(file.getRelationshipList()).size()) {
                res.add(ValidationError.builder().pointer("relationship").messageCode("duplicate.relation").build());
            }
            return res;
        } else {
            return null;
        }

    }

}
