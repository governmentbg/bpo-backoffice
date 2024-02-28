package bg.duosoft.ipas.test.mapper.reception;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * User: Georgi
 * Date: 8.6.2020 г.
 * Time: 16:46
 */
public abstract class ReceptionMapperTestBase extends TestBase {
    @Autowired
    private PersonService personService;
    protected void compareDocBase(CReception r, IpDoc entity) {
        assertEquals((Object)1, entity.getRowVersion());
        assertEquals(r.getEntryDate(), entity.getFilingDate());
        assertEquals(LocalDate.now(), DateUtils.convertToLocalDate(entity.getReceptionDate()));
        assertEquals(r.getOriginalExpected(), MapperHelper.getTextAsBoolean(entity.getIndFaxReception()));
        assertEquals("externalSystemId", entity.getExternalSystemId());
    }
    public static CReception createReceptionBase() {
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

        o.setPerson(new CPerson());
        o.getPerson().setPersonNbr(397892);
        o.getPerson().setAddressNbr(1);
        o.getPerson().setPersonName("ЕЛ ЕНД ДЖИ ФУУДС ООД");
        o.getPerson().setResidenceCountryCode("BG");
        o.getPerson().setNationalityCountryCode("BG");
        o.getPerson().setTelephone("Mobile Phone:0882280713");
        o.getPerson().setIndCompany(true);
        o.getPerson().setEmail("pavlina.nikolova@lsa-bg.com");
        o.getPerson().setAddressStreet("ул. Александър Стамболийски № 62А");
        o.getPerson().setCityName("Костинброд");
        o.getPerson().setZipCode("2230");
        r.getOwnershipData().getOwnerList().add(o);
        r.setRepresentationData(new CRepresentationData());
        r.getRepresentationData().setRepresentativeList(new ArrayList<>());
        CRepresentative rep = new CRepresentative();
        rep.setRepresentativeType("AG");
        rep.setPerson(new CPerson());
        rep.getPerson().setAgentCode("123");
        r.getRepresentationData().getRepresentativeList().add(rep);
        r.setExternalSystemId("externalSystemId");
        r.setNotes("notes");
        return r;
    }
}
