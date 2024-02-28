package bg.duosoft.ipas.core.validation.design;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.ipobject.IpObjectEFilingDataValidator;
import bg.duosoft.ipas.core.validation.patent.PatentValidateApplicationTypeSubTypeAndLaw;
import bg.duosoft.ipas.core.validation.patent.PatentValidator;
import bg.duosoft.ipas.core.validation.patent.drawing.CDrawingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DesignValidator implements IpasValidator<CPatent> {

    @Autowired
    private CDrawingValidator drawingValidator;

    @Autowired
    private PatentValidator patentValidator;

    @Autowired
    private PatentValidateApplicationTypeSubTypeAndLaw patentValidateApplicationTypeSubTypeAndLaw;

    @Autowired
    private SingleDesignTitleValidator singleDesignTitleValidator;

    @Autowired
    private SingleDesignMandatoryLocarnoClassesValidator singleDesignMandatoryLocarnoClassesValidator;

    @Autowired
    private SingleDesignUniqueLocarnoClassValidator singleDesignUniqueLocarnoClassValidator;

    @Autowired
    private IpObjectEFilingDataValidator designIpObjectEFilingDataValidator;

    @Override
    public List<ValidationError> validate(CPatent obj, Object... additionalArgs) {

        List<List<ValidationError>> allValidation = new ArrayList<>();
        List<CPatent> singleDesigns = (List<CPatent>) additionalArgs[0];

        //Validate main design
        List<ValidationError> patentValidations = patentValidator.validate(obj);
        List<ValidationError> applicationTypeSupTypeAndLawValidations = patentValidateApplicationTypeSubTypeAndLaw.validate(obj);


        if (CollectionUtils.isEmpty(singleDesigns)){
            allValidation.add(List.of(ValidationError.builder().pointer("singleDesigns.data").messageCode("single.designs.required").build()));
        }

        //Validate single designs
        singleDesigns.stream().forEach(singleDesign->{
            List<ValidationError> drawingValidations =
                    singleDesign.getTechnicalData()
                            .getDrawingList()
                            .stream()
                            .filter(r -> r.isLoaded())
                            .map(d -> drawingValidator.validate(d, singleDesign.getTechnicalData().getDrawingList()))
                            .filter(Objects::nonNull)
                            .flatMap(r -> r.stream())
                            .collect(Collectors.toList());

            List<ValidationError> applicationTypeSupTypeAndLawForSingleDesignValidations = patentValidateApplicationTypeSubTypeAndLaw.validate(singleDesign);
            List<ValidationError> singleDesignTitleValidations = singleDesignTitleValidator.validate(singleDesign);
            List<ValidationError> singleDesignMandatoryLocarnoClassesValidations = singleDesignMandatoryLocarnoClassesValidator.validate(singleDesign);
            List<ValidationError> singleDesignUniqueLocarnoClassValidations = null;
            if (!Objects.isNull(singleDesign.getTechnicalData().getLocarnoClassList())){
                singleDesignUniqueLocarnoClassValidations = singleDesign.getTechnicalData().getLocarnoClassList().stream()
                        .map(d -> singleDesignUniqueLocarnoClassValidator.validate(d, singleDesign.getTechnicalData().getLocarnoClassList())).filter(Objects::nonNull)
                        .flatMap(r -> r.stream())
                        .collect(Collectors.toList());
            }
            allValidation.add(singleDesignUniqueLocarnoClassValidations);
            allValidation.add(applicationTypeSupTypeAndLawForSingleDesignValidations);
            allValidation.add(drawingValidations);
            allValidation.add(singleDesignTitleValidations);
            allValidation.add(singleDesignMandatoryLocarnoClassesValidations);
        });
        allValidation.add(patentValidations);
        allValidation.add(applicationTypeSupTypeAndLawValidations);
        allValidation.add(designIpObjectEFilingDataValidator.validate(obj.getPatentEFilingData(), additionalArgs));

        return allValidation.stream().filter(r -> !CollectionUtils.isEmpty(r)).flatMap(r -> r.stream()).collect(Collectors.toList());
    }
}
