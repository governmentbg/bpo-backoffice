package bg.duosoft.ipas.test.service;

import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.core.model.person.CPerson;

import java.util.Random;

import static bg.duosoft.ipas.test.TestUtils.generateRandomString;
import static org.junit.Assert.assertEquals;

public abstract class ServiceTestBase extends TestBase {

    protected void compareAddressAndPersonDataOriginAfterUpdate(CPerson personForUpdate, CPerson personAfterUpdate) {

        assertEquals(personForUpdate.getPersonName(), personAfterUpdate.getPersonName());
        assertEquals(personForUpdate.getAddressStreet(), personAfterUpdate.getAddressStreet());
        assertEquals(personForUpdate.getLegalNature(), personAfterUpdate.getLegalNature());
        assertEquals(personForUpdate.getZipCode(), personAfterUpdate.getZipCode());
        assertEquals(personForUpdate.getStateName(), personAfterUpdate.getStateName());
        assertEquals(personForUpdate.getAddressZone(), personAfterUpdate.getAddressZone());
        assertEquals(personForUpdate.getEmail(), personAfterUpdate.getEmail());
        assertEquals(personForUpdate.getTelephone(), personAfterUpdate.getTelephone());
        assertEquals(personForUpdate.getCityName(), personAfterUpdate.getCityName());
        assertEquals(personForUpdate.getCityCode(), personAfterUpdate.getCityCode());


    }

    private CPerson createRandomPerson() {
        CPerson person = new CPerson();
        person.setIndCompany(false);
        person.setAddressStreet(generateRandomString(20));
        person.setCityName(generateRandomString(5));
        person.setResidenceCountryCode("BG");
        person.setStateName(generateRandomString(5));
        person.setPersonName(generateRandomString(30));
        person.setNationalityCountryCode("BG");
        person.setZipCode(new Random().nextInt(1000) + "");
        return person;
    }

    protected COwner createRandomCOwner() {
        CPerson randomPerson = createRandomPerson();
        COwner owner = new COwner();
        owner.setPerson(randomPerson);
        owner.setOrderNbr(10);
        owner.setOwnershipNotes("Test Notes");

        return owner;
    }

    protected CAuthor createRandomCAuthor() {
        CPerson randomPerson = createRandomPerson();
        CAuthor author = new CAuthor();
        author.setAuthorSeq(Long.valueOf(3));
        author.setPerson(randomPerson);
        return author;
    }


    private void comparePersons(CPerson person, CPerson insertedPerson) {
        _assertEquals(person, insertedPerson, CPerson::getPersonName);
        _assertEquals(person, insertedPerson, CPerson::getIndCompany);
        _assertEquals(person, insertedPerson, CPerson::getCityName);
        _assertEquals(person, insertedPerson, CPerson::getResidenceCountryCode);
        _assertEquals(person, insertedPerson, CPerson::getStateName);
        _assertEquals(person, insertedPerson, CPerson::getNationalityCountryCode);
        _assertEquals(person, insertedPerson, CPerson::getZipCode);
    }


    protected void compareCAuthors(CAuthor author, CAuthor insertedAuthor) {
        comparePersons(author.getPerson(), insertedAuthor.getPerson());
        _assertEquals(author, insertedAuthor, CAuthor::getAuthorSeq);
    }

    protected void compareCOwners(COwner owner, COwner insertedOwner) {
        comparePersons(owner.getPerson(), insertedOwner.getPerson());
        _assertEquals(owner, insertedOwner, COwner::getOrderNbr);
        _assertEquals(owner, insertedOwner, COwner::getOwnershipNotes);
    }


}
