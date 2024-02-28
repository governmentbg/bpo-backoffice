package bg.duosoft.ipas.core.service.person;

import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CPersonId;
import bg.duosoft.ipas.persistence.model.nonentity.PersonUsageData;
import bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult;
import bg.duosoft.ipas.util.filter.MergePersonFilter;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 22.3.2019 г.
 * Time: 13:05
 */
public interface PersonService {
    List<CPerson> findPersons(CCriteriaPerson criteria);

    CPerson mergeOrInsertPersonAddress(CPerson cPerson);

    CPerson selectPersonByPersonNumberAndAddressNumber(Integer personNumber, Integer addressNumber);

    CPerson selectLastVersionOfPerson(Integer personNumber);

    Integer selectPersonNumberOfLastVersionOfPerson(Integer personNumber);

    long count();

    List<CPersonId> selectIdentifiersOfAllNotMainPersonAddresses();

    CPersonId splitIpPerson(Integer personNumber, Integer addressNumber);

    PersonUsageData selectPersonUsageData(Integer personNumber, Integer addressNumber);

    List<SimplePersonAddressResult> selectAllSimple(Pageable pageable);

    List<SimplePersonAddressResult> selectMergeSimple(MergePersonFilter filter);

    boolean deletePerson(Integer personNumber, Integer addressNumber, boolean deleteOnlyNotUsedPerson);

    boolean replacePerson(Integer oldPersonNumber, Integer oldAddressNumber, Integer newPersonNumber, Integer newAddressNumber);

}
