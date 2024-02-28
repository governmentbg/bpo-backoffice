package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.file.IpObjectFileValidator;
import bg.duosoft.ipas.core.validation.ipobject.EntitlementDateValidator;
import bg.duosoft.ipas.core.validation.ipobject.ExpirationDateValidator;
import bg.duosoft.ipas.core.validation.ipobject.RegistrationDupValidator;
import bg.duosoft.ipas.enums.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MarkValidator implements IpasValidator<CMark> {
    @Autowired
    private ExpirationDateValidator expirationDateValidator;

    @Autowired
    private RegistrationDupValidator registrationDupValidator;

    @Autowired
    private EntitlementDateValidator entitlementDateValidator;

    @Autowired
    private MarkValidateApplicationTypeSubTypeAndLaw markValidateApplicationTypeSubTypeAndLaw;

    @Autowired
    private MarkTransformationValidator markTransformationValidator;

    @Autowired
    private MarkExhibitionValidator markExhibitionValidator;

    @Autowired
    private AcpValidator acpValidator;

    @Autowired
    private MarkEfilingDataValidator markEfilingDataValidator;

    @Autowired
    private MarkViennaClassesValidator markViennaClassesValidator;

    @Autowired
    private MarkOwnershipDataValidator markOwnershipDataValidator;

    @Autowired
    private MarkNiceClassValidator markNiceClassValidator;

    @Autowired
    private MarkSignTypeValidator markSignTypeValidator;

    @Autowired
    private MarkRelationshipsValidator markRelationshipsValidator;

    @Autowired
    private MarkPrioritiesValidator markPrioritiesValidator;

    @Autowired
    private IpObjectFileValidator ipObjectFileValidator;

    @Override
    public List<ValidationError> validate(CMark mark, Object... additionalArgs) {
        List<List<ValidationError>> validations = new ArrayList<>();

        validations.add(ipObjectFileValidator.validate(mark.getFile(), additionalArgs));
        validations.add(expirationDateValidator.validate(mark.getFile(), additionalArgs));
        validations.add(registrationDupValidator.validate(mark.getFile(), additionalArgs));

        if (!mark.getFile().getFileId().getFileType().equals(FileType.ACP.code())) {
            validations.add(entitlementDateValidator.validate(mark.getFile(), additionalArgs));
        }
        validations.add(markValidateApplicationTypeSubTypeAndLaw.validate(mark, additionalArgs));
        validations.add(markTransformationValidator.validate(mark, additionalArgs));
        validations.add(markExhibitionValidator.validate(mark, additionalArgs));
        validations.add(acpValidator.validate(mark, additionalArgs));
        validations.add(markEfilingDataValidator.validate(mark, additionalArgs));
        validations.add(markViennaClassesValidator.validate(mark, additionalArgs));
        validations.add(markOwnershipDataValidator.validate(mark, additionalArgs));
        validations.add(markNiceClassValidator.validate(mark, additionalArgs));
        validations.add(markSignTypeValidator.validate(mark, additionalArgs));
        validations.add(markRelationshipsValidator.validate(mark, additionalArgs));
        validations.add(markPrioritiesValidator.validate(mark, additionalArgs));

        return validations.stream()
                .filter(r -> !CollectionUtils.isEmpty(r))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
