package bg.duosoft.ipas.test.repository.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.person.IpPerson;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNull;

public class IpMarkPersonServiceTest extends TestBase {


    @Autowired
    private IpMarkRepository ipMarkRepository;

    private IpMark getMark() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 1991, 15595);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);
        return ipMark;

    }


    @Test
    @Transactional
    public void removeServicePerson() {
        IpMark ipMark = getMark();
        ipMark.setServicePerson(null);
        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertNull(updated.getServicePerson());

    }

    @Test
    @Transactional
    public void updateServicePerson() {
        IpMark ipMark = getMark();
        IpPersonAddresses servicePerson = ipMark.getServicePerson();
        Assert.assertNotNull(servicePerson);
        servicePerson.getIpPerson().setPersonName("Test");
        servicePerson.getIpPerson().setNationalityCountryCode("FR");
        servicePerson.setCityName("София");

        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        _assertEquals(servicePerson, updated.getServicePerson(), IpPersonAddresses::getCityName);
        _assertEquals(servicePerson.getIpPerson(), updated.getServicePerson().getIpPerson(), IpPerson::getPersonName);
        _assertEquals(servicePerson.getIpPerson(), updated.getServicePerson().getIpPerson(), IpPerson::getNationalityCountryCode);
    }


}
