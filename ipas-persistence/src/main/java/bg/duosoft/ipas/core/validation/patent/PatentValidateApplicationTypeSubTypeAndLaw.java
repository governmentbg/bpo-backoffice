package bg.duosoft.ipas.core.validation.patent;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLawApplicationSubtype;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLawApplicationSubTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class PatentValidateApplicationTypeSubTypeAndLaw implements IpasValidator<CPatent> {

    @Autowired
    private CfLawApplicationSubTypeRepository cfLawApplicationSubTypeRepository;

    @Override
    public List<ValidationError> validate(CPatent patent, Object... additionalArgs) {
        Integer lawCode = patent.getFile().getFilingData().getLawCode();
        if (lawCode != null) {
            String appType = patent.getFile().getFilingData().getApplicationType();
            String applicationSubType = patent.getFile().getFilingData().getApplicationSubtype();
            Optional<CfLawApplicationSubtype> config = cfLawApplicationSubTypeRepository.findByLawCodeApplicationTypeAndSubtype(lawCode, appType, applicationSubType);
            if (config.isEmpty()) {
                return Arrays.asList(ValidationError.builder().pointer("file.filingData.lawCode").messageCode("incompatible.law.type.subtype").build());
            }

        }
        return null;
    }
}
