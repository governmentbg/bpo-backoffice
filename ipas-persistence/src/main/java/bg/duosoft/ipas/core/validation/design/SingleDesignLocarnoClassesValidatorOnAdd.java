package bg.duosoft.ipas.core.validation.design;

import bg.duosoft.ipas.core.model.design.CLocarnoClasses;
import bg.duosoft.ipas.core.model.design.CPatentLocarnoClasses;
import bg.duosoft.ipas.core.service.nomenclature.LocarnoClassesService;
import bg.duosoft.ipas.core.validation.config.IpasTwoArgsValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SingleDesignLocarnoClassesValidatorOnAdd implements IpasTwoArgsValidator<CPatentLocarnoClasses, List<CPatentLocarnoClasses>> {

    @Autowired
    private SingleDesignUniqueLocarnoClassValidator singleDesignUniqueLocarnoClassValidator;

    @Autowired
    private LocarnoClassesService locarnoClassesService;

    private final String locarnoClassPattern="^(\\d+-){1}\\d+$";

    @Override
    public List<ValidationError> validate(CPatentLocarnoClasses obj, List<CPatentLocarnoClasses> arg, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        if (Objects.isNull(obj.getLocarnoClassCode()) || obj.getLocarnoClassCode().isEmpty() || !obj.getLocarnoClassCode().matches(locarnoClassPattern)){
            errors.add(ValidationError.builder().pointer("locarnoClassesData").messageCode("invalid.locarno.classes.format").invalidValue(obj).build());
            return errors;
        }

        if (!locarnoClassesService.isLocarnoClassExist(obj.getLocarnoClassCode())){
            errors.add(ValidationError.builder().pointer("locarnoClassesData").messageCode("locarno.class.not.found").invalidValue(obj).build());
            return errors;
        }

        List<ValidationError> uniqueLocarnoClassValidatorList = singleDesignUniqueLocarnoClassValidator.validate(obj, arg);
        if (Objects.nonNull(uniqueLocarnoClassValidatorList)){
            errors.addAll(uniqueLocarnoClassValidatorList);
            return errors;
        }

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }
}
