package bg.duosoft.ipas.core.validation.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
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
public class MarkValidateApplicationTypeSubTypeAndLaw implements IpasValidator<CMark> {

    @Autowired
    private CfLawApplicationSubTypeRepository cfLawApplicationSubTypeRepository;

    @Override
    public List<ValidationError> validate(CMark mark, Object... additionalArgs) {
        Integer lawCode = mark.getFile().getFilingData().getLawCode();
        if (lawCode != null) {
            String appType = mark.getFile().getFilingData().getApplicationType();
            String applicationSubType = mark.getFile().getFilingData().getApplicationSubtype();
            Optional<CfLawApplicationSubtype> config = cfLawApplicationSubTypeRepository.findByLawCodeApplicationTypeAndSubtype(lawCode, appType, applicationSubType);
            if (config.isEmpty()) {
                return Arrays.asList(ValidationError.builder().pointer("file.filingData.lawCode").messageCode("incompatible.law.type.subtype").build());
            }

        }
        return null;
    }
}
