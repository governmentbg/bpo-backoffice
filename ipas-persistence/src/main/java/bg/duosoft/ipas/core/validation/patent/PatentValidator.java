package bg.duosoft.ipas.core.validation.patent;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.file.IpObjectFileValidator;
import bg.duosoft.ipas.core.validation.ipobject.*;
import bg.duosoft.ipas.core.validation.patent.attachments.CPatentDetailsValidator;
import bg.duosoft.ipas.core.validation.patent.drawing.CDrawingValidator;
import bg.duosoft.ipas.core.validation.patent.ipcs.CPatentIpcPkValidator;
import bg.duosoft.ipas.core.validation.patent.spc.SpcValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 15.3.2019 г.
 * Time: 16:22
 */
@Component
public class PatentValidator implements IpasValidator<CPatent> {

    @Autowired
    private PatentValidateApplicationTypeSubTypeAndLaw patentValidateApplicationTypeSubTypeAndLaw;
    @Autowired
    private EntitlementDateValidator entitlementDateValidator;
    @Autowired
    private ExpirationDateValidator expirationDateValidator;
    @Autowired
    private PatentExhibitionValidator patentExhibitionValidator;
    @Autowired
    private CDrawingValidator drawingValidator;
    @Autowired
    private CPatentIpcPkValidator cPatentIpcPkValidator;
    @Autowired
    private CPatentDetailsValidator cPatentDetailsValidator;
    @Autowired
    private RelationshipsValidator relationshipsValidator;
    @Autowired
    private PrioritiesValidator prioritiesValidator;
    @Autowired
    private SpcValidator spcValidator;
    @Autowired
    private PctValidator pctValidator;
    @Autowired
    private PatentRelationshipExtendedValidator patentTransformationValidator;
    @Autowired
    private IpObjectEFilingDataValidator patentIpObjectEFilingDataValidator;
    @Autowired
    private IpObjectFileValidator ipObjectFileValidator;

    @Override
    public List<ValidationError> validate(CPatent obj, Object... additionalArgs) {
        List<List<ValidationError>> allValidation = new ArrayList<>();
        List<ValidationError> drawingValidations =
                obj.getTechnicalData()
                        .getDrawingList()
                        .stream()
                        .filter(r -> r.isLoaded())
                        .map(d -> drawingValidator.validate(d, obj.getTechnicalData().getDrawingList()))
                        .filter(Objects::nonNull)
                        .flatMap(r -> r.stream())
                        .collect(Collectors.toList());


        List<ValidationError> ipcsValidations = obj.getTechnicalData().getIpcClassList().stream()
                .map(d -> cPatentIpcPkValidator.validate(d, obj.getTechnicalData().getIpcClassList())).filter(Objects::nonNull)
                .flatMap(r -> r.stream())
                .collect(Collectors.toList());

        allValidation.add(drawingValidations);
        allValidation.add(ipcsValidations);
        allValidation.add(ipObjectFileValidator.validate(obj.getFile(), additionalArgs));
        allValidation.add(cPatentDetailsValidator.validate(obj.getPatentDetails(), additionalArgs));
        allValidation.add(patentValidateApplicationTypeSubTypeAndLaw.validate(obj,additionalArgs));
        allValidation.add(entitlementDateValidator.validate(obj.getFile(),additionalArgs));
        allValidation.add(expirationDateValidator.validate(obj.getFile(),additionalArgs));
        allValidation.add(patentExhibitionValidator.validate(obj,additionalArgs));
        allValidation.add(relationshipsValidator.validate(obj.getFile(), additionalArgs));
        allValidation.add(spcValidator.validate(obj,additionalArgs));
        allValidation.add(prioritiesValidator.validate(obj.getFile(),additionalArgs));
        allValidation.add(pctValidator.validate(obj, additionalArgs));
        allValidation.add(patentIpObjectEFilingDataValidator.validate(obj.getPatentEFilingData(), additionalArgs));
        allValidation.add(patentTransformationValidator.validate(obj));

        //TODO:Other validations....
        return allValidation.stream().filter(r -> !CollectionUtils.isEmpty(r)).flatMap(r -> r.stream()).collect(Collectors.toList());
    }
}
