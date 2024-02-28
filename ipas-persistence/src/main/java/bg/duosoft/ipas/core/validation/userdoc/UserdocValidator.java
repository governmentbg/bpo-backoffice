package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.document.CExtraData;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraData;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.userdoc.grounds.RootGroundsValidator;
import bg.duosoft.ipas.core.validation.userdoc.reviewers.MainReviewerValidator;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserdocValidator implements IpasValidator<CUserdoc> {

    @Autowired
    private MainReviewerValidator mainReviewerValidator;

    @Autowired
    private UserdocPersonValidator userdocPersonValidator;

    @Autowired
    private ChangeUserdocTypeValidator changeUserdocTypeValidator;

    @Autowired
    private UserdocClaimValidator userdocClaimValidator;

    @Autowired
    private RootGroundsValidator rootGroundsValidator;

    @Autowired
    private RecordalValidator recordalValidator;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private UserdocEFilingDataValidator userdocEFilingDataValidator;

    @Autowired
    private UserdocServiceScopeValidator userdocServiceScopeValidator;

    @Override
    public List<ValidationError> validate(CUserdoc userdoc, Object... additionalArgs) {
        boolean isAuthorizingValidation = (boolean) additionalArgs[0];

        List<List<ValidationError>> validations = new ArrayList<>();

        CUserdoc originalUserdoc = selectOriginalUserdoc(userdoc);
        boolean isUserdocTypeChanged = UserdocUtils.isUserdocTypeChanged(originalUserdoc, userdoc);
        if (isUserdocTypeChanged) {
            boolean isUserdocDataChanged = DiffGenerator.create(cloneOriginalUserdoc(userdoc, originalUserdoc), userdoc).isChanged();
            if (isUserdocDataChanged) {
                validations.add(Collections.singletonList(ValidationError.builder().pointer("userdoc.userdocType").messageCode("userdoc.change.type.changed.data").build()));
            } else {
                validations.add(changeUserdocTypeValidator.validate(userdoc));
            }
        } else {
            validations.add(userdocPersonValidator.validate(userdoc, isAuthorizingValidation));
            validations.add(rootGroundsValidator.validate(userdoc));
            validations.add(userdocEFilingDataValidator.validate(userdoc));
            validations.add(mainReviewerValidator.validate(userdoc));
            validations.add(userdocClaimValidator.validate(userdoc));

//            TODO Temporary removed (IPAS-682)
//            ------------------------------------------------------------------
//            validations.add(recordalValidator.validate(userdoc));
//            validations.add(userdocServiceScopeValidator.validate(userdoc));
//            ------------------------------------------------------------------
        }
        return extractErrors(validations);
    }

    private List<ValidationError> extractErrors(List<List<ValidationError>> validations) {
        return validations.stream()
                .filter(r -> !CollectionUtils.isEmpty(r))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private CUserdoc cloneOriginalUserdoc(CUserdoc userdoc, CUserdoc originalUserdoc) {
        CUserdoc comparableOriginalUserdoc = (CUserdoc) SerializationUtils.clone(originalUserdoc);
        comparableOriginalUserdoc.setUserdocType(userdoc.getUserdocType());

        //Next checks are related to UserdocChangeTypeController updateSessionObject method
        CExtraData extraData = userdoc.getDocument().getExtraData();
        if (checkIfAllFieldsInExtraDataAreNull(extraData)) {
            comparableOriginalUserdoc.getDocument().setExtraData(extraData);
        }

        List<CUserdocExtraData> userdocExtraData = userdoc.getUserdocExtraData();
        if (CollectionUtils.isEmpty(userdocExtraData)) {
            comparableOriginalUserdoc.setUserdocExtraData(userdocExtraData);
        }

        Boolean indCompulsoryLicense = userdoc.getIndCompulsoryLicense();
        if (Objects.isNull(indCompulsoryLicense)) {
            comparableOriginalUserdoc.setIndCompulsoryLicense(indCompulsoryLicense);
        }

        Boolean indExclusiveLicense = userdoc.getIndExclusiveLicense();
        if (Objects.isNull(indExclusiveLicense)) {
            comparableOriginalUserdoc.setIndExclusiveLicense(indExclusiveLicense);
        }

        List<CNiceClass> niceClassList = userdoc.getProtectionData().getNiceClassList();
        if (CollectionUtils.isEmpty(niceClassList)) {
            comparableOriginalUserdoc.getProtectionData().setNiceClassList(niceClassList);
        }

        List<CUserdocRootGrounds> userdocRootGrounds = userdoc.getUserdocRootGrounds();
        if (CollectionUtils.isEmpty(userdocRootGrounds)) {
            comparableOriginalUserdoc.setUserdocRootGrounds(userdocRootGrounds);
        }

        UserdocUtils.removePersonsWithRolesWhichIsNotInConfiguration(comparableOriginalUserdoc, userdoc.getUserdocType());
        return comparableOriginalUserdoc;
    }

    private CUserdoc selectOriginalUserdoc(CUserdoc userdoc) {
        CUserdoc originalUserdoc = userdocService.findUserdoc(userdoc.getDocumentId());
        if (Objects.isNull(originalUserdoc))
            throw new RuntimeException("Cannot find userdoc " + userdoc.getDocumentId());
        return originalUserdoc;
    }

    public boolean checkIfAllFieldsInExtraDataAreNull(CExtraData extraData) {
        try {
            for (Field f : CExtraData.class.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(extraData) != null)
                    return false;
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
