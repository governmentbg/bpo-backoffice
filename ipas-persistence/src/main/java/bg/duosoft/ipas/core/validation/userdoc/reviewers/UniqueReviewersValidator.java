package bg.duosoft.ipas.core.validation.userdoc.reviewers;


import bg.duosoft.ipas.core.model.userdoc.reviewers.CUserdocReviewer;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UniqueReviewersValidator implements IpasTwoArgsValidator<CUserdocReviewer, List<CUserdocReviewer>> {
    @Override
    public List<ValidationError> validate(CUserdocReviewer obj, List<CUserdocReviewer> arg, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        if (!CollectionUtils.isEmpty(arg) && !Objects.isNull(obj) && !Objects.isNull(obj.getUser()) && !Objects.isNull(obj.getUser().getUserId())){
            for (CUserdocReviewer reviewerGroup:arg) {
               if(obj!=reviewerGroup && obj.getUser().getUserId().equals(reviewerGroup.getUser().getUserId())){
                   errors.add(ValidationError.builder().pointer("reviewers.errors").messageCode("non.unique.user.validation").build());
               }

            }
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
