package bg.duosoft.ipas.core.service.impl.person;

import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CPersonId;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.model.nonentity.PersonUsageData;
import bg.duosoft.ipas.persistence.model.nonentity.SimplePersonAddressResult;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonAdressesRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.filter.MergePersonFilter;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 22.3.2019 г.
 * Time: 13:06
 */
@Slf4j
@Service
@Transactional
@LogExecutionTime
public class PersonServiceImpl implements PersonService {

    @Autowired
    private IpPersonAdressesRepository personAdressesRepository;

    @Autowired
    private PersonAddressMapper personAddressMapper;

    @Override
    public List<CPerson> findPersons(CCriteriaPerson criteria) {
        List<IpPersonAddresses> persons = personAdressesRepository.findPersons(criteria);
        return persons.stream().map(p -> personAddressMapper.toCore(p)).collect(Collectors.toList());
    }

    @Override
    public CPerson mergeOrInsertPersonAddress(CPerson cPerson) {
        if (Objects.isNull(cPerson))
            return null;

        IpPersonAddresses ipPersonAddresses = personAddressMapper.toEntity(cPerson);
        if (Objects.isNull(ipPersonAddresses))
            return null;

        IpPersonAddresses processedPerson = personAdressesRepository.mergeOrInsertPersonAddress(ipPersonAddresses);
        if (Objects.isNull(processedPerson))
            return null;

        return personAddressMapper.toCore(processedPerson);
    }

    @Override
    public CPerson selectPersonByPersonNumberAndAddressNumber(Integer personNumber, Integer addressNumber) {
        IpPersonAddressesPK pk = new IpPersonAddressesPK(personNumber, addressNumber);
        IpPersonAddresses ipPersonAddresses = personAdressesRepository.findById(pk).orElse(null);
        if (Objects.isNull(ipPersonAddresses)) {
            return null;
        }
        return personAddressMapper.toCore(ipPersonAddresses);
    }

    @Override
    public long count() {
        return personAdressesRepository.count();
    }

    @Override
    public List<CPersonId> selectIdentifiersOfAllNotMainPersonAddresses() {
        return personAdressesRepository.selectIdentifiersOfAllNotMainPersonAddresses();
    }

    @Override
    public CPersonId splitIpPerson(Integer personNumber, Integer addressNumber) {
        IpPersonAddressesPK result = personAdressesRepository.splitIpPerson(personNumber, addressNumber);
        if (Objects.isNull(result))
            return null;

        return new CPersonId(result.getPersonNbr(), result.getAddrNbr());
    }

    @Override
    public CPerson selectLastVersionOfPerson(Integer personNumber) {
        Integer lastPersonVersion = selectPersonNumberOfLastVersionOfPerson(personNumber);
        IpPersonAddresses result = personAdressesRepository.findById(new IpPersonAddressesPK(lastPersonVersion, DefaultValue.FIRST_PERSON_ADDRESS_NUMBER)).orElse(null);
        if (Objects.isNull(result))
            return null;

        return personAddressMapper.toCore(result);
    }

    @Override
    public Integer selectPersonNumberOfLastVersionOfPerson(Integer personNumber) {
        Integer lastPersonVersion = personAdressesRepository.selectLastPersonVersion(personNumber);
        if (Objects.isNull(lastPersonVersion))
            throw new RuntimeException("Cannot find last person version ! Person number: " + personNumber);

        return lastPersonVersion;
    }

    @Override
    public PersonUsageData selectPersonUsageData(Integer personNumber, Integer addressNumber) {
        return personAdressesRepository.selectPersonUsageData(personNumber, addressNumber);
    }

    @Override
    public List<SimplePersonAddressResult> selectAllSimple(Pageable pageable) {
        return personAdressesRepository.selectAllSimple(pageable);
    }

    @Override
    public List<SimplePersonAddressResult> selectMergeSimple(MergePersonFilter filter) {
        return personAdressesRepository.selectMergeSimple(filter);
    }

    @Override
    public boolean deletePerson(Integer personNumber, Integer addressNumber, boolean deleteOnlyNotUsedPerson) {
        return personAdressesRepository.deletePerson(personNumber, addressNumber, deleteOnlyNotUsedPerson);
    }

    @Override
    public boolean replacePerson(Integer oldPersonNumber, Integer oldAddressNumber, Integer newPersonNumber, Integer newAddressNumber) {
        return personAdressesRepository.replaceIpPerson(oldPersonNumber, oldAddressNumber, newPersonNumber, newAddressNumber);
    }
}
