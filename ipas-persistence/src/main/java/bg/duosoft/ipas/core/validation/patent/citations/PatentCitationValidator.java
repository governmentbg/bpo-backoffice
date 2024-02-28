package bg.duosoft.ipas.core.validation.patent.citations;


import bg.duosoft.ipas.core.model.patent.CPatentCitation;
import bg.duosoft.ipas.core.validation.config.DefaultValidation;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PatentCitationValidator implements IpasTwoArgsValidator<CPatentCitation, List<CPatentCitation>>, DefaultValidation {

    @Override
    public List<ValidationError> validate(CPatentCitation citation, List<CPatentCitation> citationList, Object... additionalArgs) {
        Integer oldRefNumber = (Integer) additionalArgs[0];
        List<ValidationError> errors = new ArrayList<>();

        Integer refNumber = citation.getRefNumber();
        rejectIfEmpty(errors, refNumber, "citation.ref.number", "required.field");
        rejectIfNotPositiveNumber(errors, refNumber, "citation.ref.number", "patent.citation.ref.number.positive");

        String refDescription = citation.getRefDescription();
        rejectIfEmptyString(errors, refDescription, "citation.ref.description", "required.field");
        if (StringUtils.hasText(refDescription)) {
            rejectIfTrue(errors, refDescription.length() > 254, "citation.ref.description", "validation.charCount.invalid.254");
        }

        String refClaims = citation.getRefClaims();
        if (StringUtils.hasText(refClaims)) {
            rejectIfTrue(errors, refClaims.length() > 30, "citation.ref.claims", "validation.charCount.invalid.30");
        }

        if (validateUniqueRefNumber(refNumber, oldRefNumber)) {
            List<Integer> duplicatedRefNumbers = citationList.stream().map(CPatentCitation::getRefNumber).filter(currentRefNumber -> Objects.equals(refNumber, currentRefNumber)).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(duplicatedRefNumbers)) {
                errors.add(ValidationError.builder().pointer("citation.ref.number").messageCode("patent.citation.duplicated.ref.number").invalidValue(refNumber).build());
            }
        }

        return errors;
    }

    private boolean validateUniqueRefNumber(Integer refNumber, Integer oldRefNumber) {
        return Objects.isNull(oldRefNumber) || !oldRefNumber.equals(refNumber);
    }
}
