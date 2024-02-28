package bg.duosoft.ipas.util.person;

import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.person.CPerson;
import org.springframework.util.StringUtils;

public class SearchPersonUtils {

    public static CCriteriaPerson createPersonSearchCriteria(CPerson cPerson) {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonName(cPerson.getPersonName());
        cCriteriaPerson.setStreet(cPerson.getAddressStreet());
        cCriteriaPerson.setZipCode(cPerson.getZipCode());
        cCriteriaPerson.setCity(cPerson.getCityName());
        cCriteriaPerson.setNationalityCountryCode(cPerson.getNationalityCountryCode());
        cCriteriaPerson.setEmail(cPerson.getEmail());
        cCriteriaPerson.setIndividualIdText(cPerson.getIndividualIdTxt());
        return cCriteriaPerson;
    }

    public static boolean isOnlyNameProvidedInSearchCriteria(CCriteriaPerson criteriaPerson) {
        if (StringUtils.isEmpty(criteriaPerson.getPersonName()))
            return false;

        return StringUtils.isEmpty(criteriaPerson.getStreet())
                && StringUtils.isEmpty(criteriaPerson.getCity())
                && StringUtils.isEmpty(criteriaPerson.getZipCode())
                && StringUtils.isEmpty(criteriaPerson.getEmail());
    }

}
