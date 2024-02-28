package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRegistrationData;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RecordalRenewalValidator implements IpasValidator<CUserdoc> {

    @Autowired
    private SearchApplicationService searchApplicationService;

    @Override
    public List<ValidationError> validate(CUserdoc userdoc, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        String pointer = Objects.isNull(additionalArgs) ? null : (String) additionalArgs[0];

        CProcessParentData userdocParentData = userdoc.getUserdocParentData();
        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdocParentData);
        IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(fileId);
        if (Objects.isNull(ipasApplicationSearchResult)) {
            throw new RuntimeException("Cannot find userdoc main object" + fileId);
        }

        CRegistrationData registrationData = ipasApplicationSearchResult.getRegistrationData();
        if (Objects.isNull(registrationData)) {
            errors.add(ValidationError.builder().pointer(pointer).messageCode("userdoc.renewal.ipobject.empty.regData").build());
        } else {
            rejectIfEmpty(errors, registrationData.getEntitlementDate(), pointer,"userdoc.renewal.ipobject.empty.entitlementDate");
            rejectIfEmpty(errors, registrationData.getExpirationDate(), pointer,"userdoc.renewal.ipobject.empty.expirationDate");
            rejectIfEmpty(errors, registrationData.getRegistrationDate(), pointer,"userdoc.renewal.ipobject.empty.regDate");
        }

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

}
