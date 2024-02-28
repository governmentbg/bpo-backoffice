package bg.duosoft.ipas.core.validation.userdoc.reviewers;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.reviewers.CUserdocReviewer;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainReviewerValidator implements IpasValidator<CUserdoc> {
    @Override
    public List<ValidationError> validate(CUserdoc obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (!CollectionUtils.isEmpty(obj.getReviewers())){
            int mainReviewers=0;
            for (CUserdocReviewer reviewer: obj.getReviewers()) {
                if (reviewer.isMain()){
                    mainReviewers++;
                }
            }
            if (mainReviewers==0){
                errors.add(ValidationError.builder().pointer("reviewers.errors").messageCode("userdoc.reviewer.main.not.selected").build());
            }
            if (mainReviewers>1){
                errors.add(ValidationError.builder().pointer("reviewers.errors").messageCode("userdoc.reviewer.main.not.unique").build());
            }
        }
        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
