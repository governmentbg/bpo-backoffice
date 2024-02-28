package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.userdoc.*;
import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserdocPersonValidator implements IpasValidator<CUserdoc> {

    @Autowired
    private SearchApplicationService searchApplicationService;

    @Autowired
    private ActionService actionService;

    @Override
    public List<ValidationError> validate(CUserdoc userdoc, Object... additionalArgs) {
        boolean isAuthorizingValidation = (boolean) additionalArgs[0];

        List<ValidationError> errors = new ArrayList<>();
        validateRepresentativeLawyers(userdoc, errors);
        validateAgentAndPartnershipIfValidType(userdoc, errors);
        validateRepresentativesOfTheOwner(userdoc, errors);

        if (isAuthorizingValidation) {
            rejectIfEmptyCollection(errors, UserdocPersonUtils.selectApplicants(userdoc.getUserdocPersonData()), "userdocPersonData.applicantList");
        }

        if (UserdocUtils.isRecordalForChangeNameOrAddress(userdoc)) {
            boolean isAuthorized = actionService.isUserdocAuthorizationActionsExists(userdoc.getProcessSimpleData().getProcessId());
            if (!isAuthorized) {
                List<CUserdocPerson> newOwners = UserdocPersonUtils.selectNewOwners(userdoc.getUserdocPersonData());
                if (!CollectionUtils.isEmpty(newOwners)) {
                    List<CPerson> mainObjectOwners = selectMainObjectOwners(userdoc);
                    rejectIfEmptyCollection(errors, mainObjectOwners, "userdocPersonData.newOwnersList", "newowners.empty.main.object.owners");
                    if (!CollectionUtils.isEmpty(mainObjectOwners)) {
                        boolean isSizeMatch = mainObjectOwners.size() == newOwners.size();
                        rejectIfFalse(errors, isSizeMatch, "userdocPersonData.newOwnersList", "main.object.owners.count.is.different.from.newowners");
                        if (isSizeMatch) {
                            rejectIfFalse(errors, isExistChangeInNewOwnersList(newOwners, mainObjectOwners), "userdocPersonData.newOwnersList", "newowners.list.not.changed");
                        }
                    }
                }
            }
        }

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

    private List<CPerson> selectMainObjectOwners(CUserdoc userdoc) {
        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdoc.getUserdocParentData());
        IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(fileId);
        if (Objects.isNull(ipasApplicationSearchResult)) {
            throw new RuntimeException("Cannot find userdoc main object " + fileId);
        }
        return ipasApplicationSearchResult.getOwners();
    }

    private boolean isExistChangeInNewOwnersList(List<CUserdocPerson> newOwners, List<CPerson> mainObjectOwners) {
        for (CUserdocPerson newOwner : newOwners) {
            CPerson person = newOwner.getPerson();
            CPerson foundPerson = mainObjectOwners.stream().filter(cPerson -> cPerson.getPersonNbr().equals(person.getPersonNbr())).findFirst().orElse(null);
            if (Objects.isNull(foundPerson)) {
                foundPerson = mainObjectOwners.stream().filter(cPerson -> cPerson.getPersonNbr().equals(person.getGralPersonIdNbr())).findFirst().orElse(null);
            }
            if (Objects.nonNull(foundPerson)) {
                boolean isNotChanged = (person.getPersonName().equals(foundPerson.getPersonName())
                        && (Objects.isNull(person.getAddressStreet()) ? Objects.isNull(foundPerson.getAddressStreet()) : person.getAddressStreet().equals(foundPerson.getAddressStreet()))
                        && (Objects.isNull(person.getCityName()) ? Objects.isNull(foundPerson.getCityName()) : person.getCityName().equals(foundPerson.getCityName()))
                        && (Objects.isNull(person.getNationalityCountryCode()) ? Objects.isNull(foundPerson.getNationalityCountryCode()) : person.getNationalityCountryCode().equals(foundPerson.getNationalityCountryCode()))
                        && (Objects.isNull(person.getZipCode()) ? Objects.isNull(foundPerson.getZipCode()) : person.getZipCode().equals(foundPerson.getZipCode()))
                        && (Objects.isNull(person.getEmail()) ? Objects.isNull(foundPerson.getEmail()) : person.getEmail().equals(foundPerson.getEmail()))
                        && (Objects.isNull(person.getTelephone()) ? Objects.isNull(foundPerson.getTelephone()) : person.getTelephone().equals(foundPerson.getTelephone()))
                );
                if (!isNotChanged) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }


    private void validateAgentAndPartnershipIfValidType(CUserdoc userdoc, List<ValidationError> errors) {
        if (Objects.nonNull(userdoc)) {
            CUserdocPersonData personData = userdoc.getUserdocPersonData();
            if (Objects.nonNull(personData)) {
                List<CUserdocPerson> personList = personData.getPersonList();
                if (!CollectionUtils.isEmpty(personList)) {
                    rejectIfFalse(errors, UserdocPersonUtils.checkAgentAndPartnershipIfValidType(personList), "representativeList", "not.valid.type.for.agent.or.partnership.error");
                }
            }
        }
    }

    private void validateRepresentativeLawyers(CUserdoc userdoc, List<ValidationError> errors) {
        if (Objects.nonNull(userdoc)) {
            CUserdocPersonData personData = userdoc.getUserdocPersonData();
            if (Objects.nonNull(personData)) {
                List<CUserdocPerson> personList = personData.getPersonList();
                if (!CollectionUtils.isEmpty(personList)) {
                    rejectIfFalse(errors, UserdocPersonUtils.areAllLawyersPhysicalPersons(personList), "representativeList", "lawyer.not.physical.person.error");
                }
            }
        }
    }

    private void validateRepresentativesOfTheOwner(CUserdoc userdoc, List<ValidationError> errors) {
        if (Objects.nonNull(userdoc)) {
            List<CUserdocPersonRole> roles = userdoc.getUserdocType().getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                CUserdocPersonRole existingRole = roles.stream().filter(role -> role.getRole() == UserdocPersonRole.REPRESENTATIVE_OF_THE_OWNER).findFirst().orElse(null);
                if (Objects.nonNull(existingRole)) {
                    CUserdocPersonData personData = userdoc.getUserdocPersonData();
                    if (Objects.nonNull(personData)) {
                        List<CUserdocPerson> personList = personData.getPersonList();
                        if (!CollectionUtils.isEmpty(personList)) {
                            List<CUserdocPerson> collect = personList.stream().filter(person -> person.getRole() == UserdocPersonRole.REPRESENTATIVE_OF_THE_OWNER).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(collect)) {
                                rejectIfTrue(errors, collect.size() > 1, "userdocPersonData.representativesOfTheOwnerList", "multiple.representativesOfTheOwner.error");
                            }
                        }
                    }

                }
            }
        }
    }

}
