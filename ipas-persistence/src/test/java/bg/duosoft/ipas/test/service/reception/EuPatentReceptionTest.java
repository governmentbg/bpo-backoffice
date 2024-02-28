package bg.duosoft.ipas.test.service.reception;/**
 * User: Georgi
 * Date: 18.6.2020 г.
 * Time: 14:46
 */

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionEuPatent;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;

public class EuPatentReceptionTest extends TestBase {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PersonService personService;

    @Autowired
    private ReceptionService receptionService;

    @Test
    @Transactional
    public void testReceiveApplication() {
        //CANNOT Write such a test, because there are inserts through the IPAS Api, which is not connected to the ipasunittests database!!!!
        //        CReception receptionForm = createReceptionRequest();
//        CReceptionResponse res = receptionService.createReception(receptionForm);



    }

    private CReception createReceptionRequest() {
        CReception r = new CReception();
        r.setEntryDate(DateUtils.convertToDate(LocalDate.now().atTime(10, 10)));
        r.setSubmissionType(1);
        r.setOriginalExpected(false);
        r.setRegisterInDocflowSystem(false);
        r.setRegisterReceptionRequest(false);
        r.setOwnershipData(new COwnershipData());
        r.getOwnershipData().setOwnerList(new ArrayList<>());
        COwner o = new COwner();
        o.setOrderNbr(1);
        o.setPerson(personService.selectPersonByPersonNumberAndAddressNumber(397892, 1));
        r.getOwnershipData().getOwnerList().add(o);
        CCriteriaPerson criteria = new CCriteriaPerson();
        criteria.setAgentCode("12");
        CPerson repPerson = personService.findPersons(criteria).get(0);

        r.setRepresentationData(new CRepresentationData());
        r.getRepresentationData().setRepresentativeList(new ArrayList<>());
        CRepresentative rep = new CRepresentative();
        rep.setRepresentativeType("AG");
        rep.setPerson(repPerson);
        r.getRepresentationData().getRepresentativeList().add(rep);
        r.getFile().setTitle("test");
        r.setNotes("userdoc notes");
        r.setEuPatent(new CReceptionEuPatent());
        r.getEuPatent().setUserdocType("ЕПИВ");
        r.getEuPatent().setObjectNumber(13005042);

        return r;
    }
}
