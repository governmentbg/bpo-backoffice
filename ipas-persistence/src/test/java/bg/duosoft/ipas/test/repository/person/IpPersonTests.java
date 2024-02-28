package bg.duosoft.ipas.test.repository.person;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;
import bg.duosoft.ipas.persistence.model.entity.person.IpPerson;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonAdressesRepository;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IpPersonTests extends TestBase {

    @Autowired
    private IpPersonRepository ipPersonRepository;
    @Autowired
    private SequenceRepository sequenceRepository;

    @Autowired
    private IpPersonAdressesRepository ipPersonAdressesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private IpPerson getIpPerson() {
        IpPerson ipPerson = ipPersonRepository.findById(270220).orElse(null);
        return ipPerson;
    }
    private IpPersonAddresses getIpPersonAddress() {
        IpPersonAddressesPK pk = new IpPersonAddressesPK(270220, 1);
        IpPersonAddresses ipPersonAddresses = ipPersonAdressesRepository.findById(pk).orElse(null);
        return ipPersonAddresses;
    }


    @Test
    @Transactional
    public void deletePersonAddress() {
        IpPerson ipPerson = getIpPerson();
        List<IpPersonAddresses> addresses = ipPerson.getAddresses();
        int originalSize = addresses.size();
        IpPersonAddresses ipPersonAddresses = addresses.get(0);
        ipPersonAddresses.setIpPerson(null);
        boolean remove = addresses.remove(ipPersonAddresses);
        Assert.assertTrue(remove);
        ipPersonRepository.saveAndFlush(ipPerson);
        assertEquals(originalSize - 1, ipPerson.getAddresses().size());

    }


    @Test
    @Transactional
    public void updatePerson() {
        IpPerson ipPerson = getIpPerson();
        List<IpPersonAddresses> addresses = ipPerson.getAddresses();
        IpPersonAddresses ipPersonAddresses = addresses.get(0);
        ipPersonAddresses.setStateName("TEST1");
        ipPerson.setLegalNature("TEST1");
        ipPerson.setPersonName("TEST1");
        IpPerson updated = ipPersonRepository.saveAndFlush(ipPerson);
        assertEquals("TEST1", updated.getLegalNature());
        assertEquals("TEST1", updated.getPersonName());
        assertEquals("TEST1", updated.getAddresses().get(0).getStateName());
    }
//

    @Test
    @Transactional
    public  void updatePersonAddresses() {
        IpPersonAddresses ipPersonAddresses = getIpPersonAddress();
        Assert.assertNotNull(ipPersonAddresses);

        IpPerson ipPerson = ipPersonAddresses.getIpPerson();
        ipPerson.setLegalNature("TEST23");
        ipPersonAddresses.setStateName("TEST23");

        IpPersonAddresses updated = ipPersonAdressesRepository.saveAndFlush(ipPersonAddresses);
        assertEquals("TEST23", updated.getIpPerson().getLegalNature());
        assertEquals("TEST23", updated.getStateName());
    }
//
    @Test
    @Transactional
    public  void insertPersonAndAddress() {
        IpPersonAddresses pa = new IpPersonAddresses();
        pa.setResidenceCountry(new CfGeoCountry());
        pa.getResidenceCountry().setCountryCode("BG");
        pa.setAddrStreet("Sofia2");
        pa.setZipcode("1000");
        pa.setCityName("Sofia");
        pa.setStateName("Sofia");

        IpPerson p = new IpPerson();
        p.setPersonName("Test 5--a-sdfa-sdfa-sdfa-sdf-asdf");
        p.setNationalityCountryCode("BG");
        p.setRowVersion(1);
        pa.setIpPerson(p);
        pa.setRowVersion(1);

        pa = ipPersonAdressesRepository.mergeOrInsertPersonAddress(pa);
        entityManager.flush();

        assertEquals(sequenceRepository.getNextSequenceValue(SequenceRepository.SEQUENCE_NAME.SEQUENCE_NAME_PERSON_NBR) - 1, (Object)pa.getPk().getPersonNbr());
        assertEquals(1, (Object)pa.getPk().getAddrNbr());
        assertEquals(p.getPersonName(), pa.getIpPerson().getPersonName());



    }
    @Test
    @Transactional
    public void insertPersonAddressToExistingPerson() {
        IpPerson p = getIpPerson();
        int oldPersonAddressesCnt = p.getAddresses().size();
        entityManager.detach(p);
        IpPersonAddresses pa = new IpPersonAddresses();
        pa.setResidenceCountry(new CfGeoCountry());
        pa.getResidenceCountry().setCountryCode("BG");
        pa.setAddrStreet("Sofia2");
        pa.setZipcode("10002");
        pa.setCityName("Sofia2");
        pa.setStateName("Sofia2");
        pa.setPk(new IpPersonAddressesPK(p.getPersonNbr(), null));
        pa.setRowVersion(1);

        pa.setIpPerson(p);

        pa = ipPersonAdressesRepository.mergeOrInsertPersonAddress(pa);
        entityManager.flush();

        assertEquals(p.getPersonNbr(), pa.getPk().getPersonNbr());
        assertEquals(2, (Object)pa.getPk().getAddrNbr());
//        assertEquals(oldPersonAddressesCnt +1, pa.getIpPerson().getAddresses().size());//TODO:FIX THIS!!!!!
//        System.out.println(pa.getPk().getAddrNbr());
//        System.out.println(pa.getPk().getPersonNbr());


    }

}
