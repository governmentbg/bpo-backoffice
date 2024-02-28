package bg.duosoft.ipas.test.diff;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.test.TestBase;
import liquibase.pro.packaged.A;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.GregorianCalendar;

/**
 * User: ggeorgiev
 * Date: 22.11.2021
 * Time: 16:41
 */
public class UserdocChangesLogTest extends TestBase {
    private static CDocumentId DOCUMENT_ID = new CDocumentId("BG", "E", 2004, 813406);
    @Autowired
    private UserdocService userdocService;
    @Autowired
    private PersonService personService;
    @Autowired
    private UserdocTypesService userdocTypesService;

    @Test
    @Transactional
    public void testGenerateLogChange() {
        CUserdoc original = userdocService.findUserdoc(DOCUMENT_ID.getDocOrigin(), DOCUMENT_ID.getDocLog(), DOCUMENT_ID.getDocSeries(), DOCUMENT_ID.getDocNbr());
        CUserdoc changed = userdocService.findUserdoc(DOCUMENT_ID.getDocOrigin(), DOCUMENT_ID.getDocLog(), DOCUMENT_ID.getDocSeries(), DOCUMENT_ID.getDocNbr());
        CUserdocPerson udp = new CUserdocPerson();
        udp.setNotes("notes");
        udp.setRole(UserdocPersonRole.REPRESENTATIVE);
        udp.setRepresentativeType("AG");
        udp.setPerson(personService.selectPersonByPersonNumberAndAddressNumber(116741, 1));
        changed.getUserdocPersonData().getPersonList().add(udp);
        original.getUserdocPersonData().getPersonList().add(udp);


        udp = new CUserdocPerson();
        udp.setNotes("notes2");
        udp.setRole(UserdocPersonRole.REPRESENTATIVE);
        udp.setRepresentativeType("AG");
        udp.setPerson(personService.selectPersonByPersonNumberAndAddressNumber(116742, 1));
        changed.getUserdocPersonData().getPersonList().add(udp);
        udp.getPerson().setPersonName("alabala");
        changed.setNotes("userdocNotes");
        changed.setUserdocType(userdocTypesService.selectUserdocTypeById("Лиц"));

        String result = DiffGenerator.create(original, changed).getResult();
        System.out.println(result);
    }
}
