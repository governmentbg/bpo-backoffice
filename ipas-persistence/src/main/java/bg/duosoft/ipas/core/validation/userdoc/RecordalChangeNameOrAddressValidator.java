package bg.duosoft.ipas.core.validation.userdoc;

import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class RecordalChangeNameOrAddressValidator implements IpasValidator<CUserdoc> {

    @Autowired
    private SearchApplicationService searchApplicationService;

    @Autowired
    private PersonService personService;

    @Override
    public List<ValidationError> validate(CUserdoc userdoc, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();
        String pointer = Objects.isNull(additionalArgs) ? null : (String) additionalArgs[0];

        List<CUserdocPerson> newOwners = UserdocPersonUtils.selectNewOwners(userdoc.getUserdocPersonData());
        rejectIfEmptyCollection(errors, newOwners, pointer, "newowners.empty.collection");
        if (!CollectionUtils.isEmpty(newOwners)) {
            CProcessParentData userdocParentData = userdoc.getUserdocParentData();
            CFileId fileId = UserdocUtils.selectUserdocMainObject(userdocParentData);
            IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(fileId);
            rejectIfEmpty(errors, ipasApplicationSearchResult, pointer, "cannot.find.userdoc.main.object.in.ipas");
            if (Objects.nonNull(ipasApplicationSearchResult)) {
                List<CPerson> ipObjectOwners = ipasApplicationSearchResult.getOwners();
                rejectIfEmptyCollection(errors, ipObjectOwners, pointer, "userdoc.main.object.owners.are.empty");
                if (!CollectionUtils.isEmpty(ipObjectOwners)) {
                    int ipObjectOwnersSize = ipObjectOwners.size();
                    int newOwnersSize = newOwners.size();
                    if (ipObjectOwnersSize != newOwnersSize) {
                        errors.add(ValidationError.builder().pointer(pointer).messageCode("main.object.owners.count.is.different.from.newowners").build());
                    } else {
                        for (CUserdocPerson newOwner : newOwners) {
                            if (CollectionUtils.isEmpty(errors)) {
                                CPerson newOwnerPerson = newOwner.getPerson();
                                boolean match = isNewOwnerMatch(newOwnerPerson, ipObjectOwners);
                                rejectIfFalse(errors, match, pointer, "newowners.do.not.match.to.ipobject.owners");
                            }
                        }
                    }
                }
            }
        }

        return CollectionUtils.isEmpty(errors) ? null : errors;
    }

    private boolean isNewOwnerMatch(CPerson newOwner, List<CPerson> mainObjectOwners) {
        String newOwnerAgentCode = newOwner.getAgentCode();
        Integer newOwnerPersonNumber = newOwner.getPersonNbr();
        Integer newOwnerAddressNumber = newOwner.getAddressNbr();

        if (!StringUtils.isEmpty(newOwnerAgentCode)) {
            CPerson matchedAgent = mainObjectOwners.stream()
                    .filter(person -> {
                        boolean equals = newOwnerAgentCode.equals(person.getAgentCode());
                        boolean equals1 = person.getPersonNbr().equals(newOwnerPersonNumber);
                        boolean equals2 = person.getAddressNbr().equals(newOwnerAddressNumber);
                        return equals && equals1 && equals2;
                    })
                    .findAny()
                    .orElse(null);

            return Objects.nonNull(matchedAgent);
        } else {
            boolean isMatch = false;
            CPerson lastVersionOfNewOwner = personService.selectLastVersionOfPerson(newOwnerPersonNumber);
            for (CPerson owner : mainObjectOwners) {
                Integer ownerPersonNbr = owner.getPersonNbr();
                CPerson lastVersionOfOldOwner = personService.selectLastVersionOfPerson(ownerPersonNbr);
                if (lastVersionOfNewOwner.getPersonNbr().equals(lastVersionOfOldOwner.getPersonNbr())) {
                    isMatch = true;
                    break;
                }
            }
            return isMatch;
        }
    }


}
