package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.model.nonentity.PersonUsageData;
import bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult;
import bg.duosoft.ipas.util.filter.MergePersonFilter;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;

import java.util.List;

public interface IpPersonAddressRepositoryCustom {

    IpPersonAddresses mergeOrInsertPersonAddress(IpPersonAddresses pa);

    List<IpPersonAddresses> findPersons(CCriteriaPerson criteria);

    IpPersonAddressesPK splitIpPerson(Integer personNumber, Integer addressNumber);

    PersonUsageData selectPersonUsageData(Integer personNumber, Integer addressNumber);

    boolean deletePerson(Integer personNumber, Integer addressNumber, boolean deleteOnlyNotUsedPerson);

    List<SimplePersonAddressResult> selectMergeSimple(MergePersonFilter filter);

    boolean replaceIpPerson(Integer oldPersonNumber, Integer oldAddressNumber, Integer newPersonNumber, Integer newAddressNumber);

    Integer selectLastPersonVersion(Integer personNumber);

}
