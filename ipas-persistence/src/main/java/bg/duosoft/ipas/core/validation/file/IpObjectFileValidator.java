package bg.duosoft.ipas.core.validation.file;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.model.IpObjectAuthorizationValidationProperties;
import bg.duosoft.ipas.core.validation.model.IpObjectPartialInternalValidationProperties;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class IpObjectFileValidator implements IpasValidator<CFile> {

    @Override
    public List<ValidationError> validate(CFile file, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        if (!isIpObjectAuthorization(additionalArgs) && !isPartialInternalValidation(additionalArgs)) {
            validateServicePerson(errors, file);
        }
        validateRepresentativeLawyers(file, errors);
        validateAgentAndPartnershipIfValidType(file, errors);
        validateIntellectualPropertyRepresentatives(file, errors);
        return errors;
    }

    private boolean isIpObjectAuthorization(Object[] additionalArgs) {
        boolean isIpObjectAuthorization = false;
        if (additionalArgs.length > 0 && additionalArgs[0] instanceof IpObjectAuthorizationValidationProperties) {
            IpObjectAuthorizationValidationProperties authorizationProperties = (IpObjectAuthorizationValidationProperties) additionalArgs[0];
            isIpObjectAuthorization = authorizationProperties.isIpObjectAuthorization();
        }
        return isIpObjectAuthorization;
    }

    private boolean isPartialInternalValidation(Object[] additionalArgs) {
        boolean isPartialValidation = false;
        if (additionalArgs.length > 0 && additionalArgs[0] instanceof IpObjectPartialInternalValidationProperties) {
            IpObjectPartialInternalValidationProperties internalValidationProperties = (IpObjectPartialInternalValidationProperties) additionalArgs[0];
            isPartialValidation = internalValidationProperties.isInternalValidation();
        }
        return isPartialValidation;
    }

    private void validateServicePerson(List<ValidationError> errors, CFile file) {
        if (Objects.nonNull(file)) {
            String fileType = file.getFileId().getFileType();
            if (!StringUtils.isEmpty(fileType)) {
                FileType fileTypeEnum = FileType.selectByCode(fileType);
                switch (fileTypeEnum) {
                    case ACP:
                    case PATENT:
                    case UTILITY_MODEL:
                    case EU_PATENT:
                        break;
                    default:
                        rejectIfEmpty(errors, file.getServicePerson(), "file.servicePerson");
                        break;
                }
            }
        }
    }


    private void validateAgentAndPartnershipIfValidType(CFile file, List<ValidationError> errors) {
        CRepresentationData representationData = file.getRepresentationData();
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            if (!CollectionUtils.isEmpty(representativeList)) {
                rejectIfFalse(errors, RepresentativeUtils.checkAgentAndPartnershipIfValidType(representativeList), "representativeList", "not.valid.type.for.agent.or.partnership.error");
            }
        }
    }


    private void validateRepresentativeLawyers(CFile file, List<ValidationError> errors) {
        CRepresentationData representationData = file.getRepresentationData();
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            if (!CollectionUtils.isEmpty(representativeList)) {
                rejectIfFalse(errors, RepresentativeUtils.areAllLawyersPhysicalPersons(representativeList), "representativeList", "lawyer.not.physical.person.error");
            }
        }
    }

    private void validateIntellectualPropertyRepresentatives(CFile file, List<ValidationError> errors) {
        CRepresentationData representationData = file.getRepresentationData();
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            if (!CollectionUtils.isEmpty(representativeList)) {
                FileType fileType = FileType.selectByCode(file.getFileId().getFileType());
                switch (fileType){
                    case PLANTS_AND_BREEDS:
                        rejectIfFalse(errors, RepresentativeUtils.areAllAgentsForIntellectualProperty(representativeList), "representativeList", "agent.intellectual.property.error.yes");
                        break;
                    default:
                        rejectIfFalse(errors, RepresentativeUtils.areAllAgentsForIndustrialProperty(representativeList), "representativeList", "agent.industrial.property.error.no");
                        break;
                }
            }
        }
    }
}
