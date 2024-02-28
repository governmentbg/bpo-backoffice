package bg.duosoft.ipas.test.service.person;

import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.service.person.PersonService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 22.3.2019 г.
 * Time: 13:19
 */
public class PersonServiceTest extends TestBase {
    @Autowired
    private PersonService personService;
    @Test
    @Transactional
    public void findPartnershipRepresentative() {
        List<CPerson> persons = personService.findPersons(CCriteriaPerson.builder().agentCode("A1").build());
        assertEquals(1, persons.size());
        CPerson person = persons.get(0);
        assertEquals("ПЕТОШЕВИЧ България", person.getPersonName());
        assertEquals("A1", person.getAgentCode());
        assertEquals((Object)385156, person.getPersonNbr());
//        System.out.println(persons.size());
    }
    @Test
    @Transactional
    public void findAllRepresentatives() {
        List<CPerson> persons = personService.findPersons(CCriteriaPerson.builder().onlyAgent(true).build());
        assertTrue(persons.stream().filter(r -> r.getAgentCode().startsWith("A")).count() > 0);//exists associations!
        assertTrue(persons.stream().filter(r -> !r.getAgentCode().startsWith("A")).count() > 0);//exists normal representatives
        assertTrue(persons.stream().filter(r -> StringUtils.isEmpty(r.getAgentCode())).count() == 0);//normal persons do not exist!
        System.out.println(persons.size());
    }
    @Test
    @Transactional
    public void findPersonsByNameIvan() {
        List<CPerson> persons = personService.findPersons(CCriteriaPerson.builder().personNameContainsWords("Иван").build());
        assertTrue(persons.size() >= 1000);
    }
    @Test
    @Transactional
    public void findPartnershipByCodeAndName() {
        List<CPerson> persons = personService.findPersons(CCriteriaPerson.builder().onlyAgent(true).agentCode("A1").personNameContainsWords("ПЕТО").build());
        assertTrue(persons.size() == 1);
    }
    @Test
    @Transactional
    public void findAgentByCodeAndName() {
        List<CPerson> persons = personService.findPersons(CCriteriaPerson.builder().onlyAgent(true).agentCode("1").personNameContainsWords("тахта").build());
        assertTrue(persons.size() == 1);
        assertEquals(persons.get(0).getAgentCode(), "1");
    }
}
