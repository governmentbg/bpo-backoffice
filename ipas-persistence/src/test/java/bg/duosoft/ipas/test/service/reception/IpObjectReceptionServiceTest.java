package bg.duosoft.ipas.test.service.reception;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionFile;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocRepository;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfApplicationTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLawApplicationSubTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfProcessTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * User: Georgi
 * Date: 29.5.2020 г.
 * Time: 16:35
 */
public class IpObjectReceptionServiceTest extends TestBase {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReceptionService receptionService;
    @Autowired
    private PersonService personService;
    @Autowired
    private ReceptionRequestService receptionRequestService;
    //    @Autowired
//    private FileService fileService;
//    @Autowired
//    private ProcessService processService;
    @Autowired
    private IpFileRepository fileRepository;
    @Autowired
    private IpProcRepository procRepository;
    @Autowired
    private CfApplicationTypeRepository cfApplicationTypeRepository;
    @Autowired
    private CfProcessTypeRepository cfProcessTypeRepository;
    @Autowired
    private CfLawApplicationSubTypeRepository cfLawApplicationSubTypeRepository;
    @Autowired
    private IpDocRepository docRepository;

    @Test
    @Transactional
    public void testReceiveSingleDesignWithoutDocflowRegistration() {
        CReception receptionForm = new CReception();
        receptionForm.setFile(new CReceptionFile());
        receptionForm.getFile().setFileId(new CFileId("BG", "Е", 2018, 11553009));
        receptionForm.getFile().setApplicationType("ЕД");
        receptionForm.getFile().setApplicationSubType("И");
//        receptionForm.setExternalSystemId("externalSystemId");
        receptionForm.setEntryDate(DateUtils.convertToDate(LocalDate.now().atTime(1, 0, 0)));
        receptionForm.setSubmissionType(1);
        receptionForm.setOriginalExpected(false);
        receptionForm.getFile().setTitle("тест  тест");
        receptionForm.setOwnershipData(new COwnershipData());
        receptionForm.setRegisterReceptionRequest(false);
        CPerson person = personService.selectPersonByPersonNumberAndAddressNumber(397892, 1);
        COwner owner = new COwner();
        owner.setPerson(person);
        owner.setOrderNbr(1);
        receptionForm.setOwnershipData(new COwnershipData());
        receptionForm.getOwnershipData().setOwnerList(new ArrayList<>());
        receptionForm.setNotes("notes notes");
//        receptionForm.getOwnershipData().getOwnerList().add(owner);
        receptionService.createReception(receptionForm);
        entityManager.flush();
        entityManager.clear();
        CFileId fileId = receptionForm.getFile().getFileId();
        validateBaseResult(fileId, receptionForm);
    }


    @Test
    @Transactional
    public void testRegisterMarkInDocflowSystem() {
        CReception receptionForm = new CReception();
        receptionForm.setFile(new CReceptionFile());
//        receptionForm.getFile().setFileId(new CFileId("BG", "Е", 2018, 11553009));
        receptionForm.getFile().setApplicationType("НМ");
        receptionForm.getFile().setApplicationSubType("ИМ");
//        receptionForm.setExternalSystemId("externalSystemId");
        receptionForm.setEntryDate(DateUtils.convertToDate(LocalDate.now().atTime(1, 0, 0)));
        receptionForm.setSubmissionType(1);
        receptionForm.setOriginalExpected(false);
        receptionForm.getFile().setTitle("тест  тест");
        receptionForm.setOwnershipData(new COwnershipData());
        receptionForm.setRegisterInDocflowSystem(true);
        receptionForm.setRegisterReceptionRequest(true);
        CPerson person = personService.selectPersonByPersonNumberAndAddressNumber(397892, 1);
        COwner owner = new COwner();
        owner.setPerson(person);
        owner.setOrderNbr(1);
        receptionForm.setOwnershipData(new COwnershipData());
        receptionForm.getOwnershipData().setOwnerList(new ArrayList<>());
        receptionForm.getOwnershipData().getOwnerList().add(owner);
        receptionForm.setNotes("notes notes");
        CReceptionResponse resp = receptionService.createReception(receptionForm);
        entityManager.flush();
        entityManager.clear();
        CFileId fileId = resp.getFileId();
        validateBaseResult(fileId, receptionForm);
        CReceptionRequest receptionRequest = receptionRequestService.selectReceptionByFileId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        assertNotNull(receptionRequest);
        assertEquals(resp.getDocflowDocumentId(), receptionRequest.getExternalId());
        assertEquals(receptionForm.getFile().getTitle(), receptionRequest.getName());
        assertEquals(receptionForm.getOriginalExpected(), receptionRequest.getOriginalExpected());
        assertEquals(receptionForm.getEntryDate(), receptionRequest.getFilingDate());
        assertEquals(receptionForm.getSubmissionType(), receptionRequest.getSubmissionType().getId());
    }
    void validateBaseResult(CFileId fileId, CReception receptionForm) {
        IpFile file = fileRepository.getOne(new IpFilePK(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr()));

//        CFile file = fileService.findById(fileId);
        assertEquals(receptionForm.getFile().getApplicationType(), file.getApplTyp());
        assertEquals(receptionForm.getFile().getApplicationSubType(), file.getApplSubtyp());
        assertEquals(fileId.getFileSeq(), file.getPk().getFileSeq());
        assertEquals(fileId.getFileType(), file.getPk().getFileTyp());
        assertEquals(fileId.getFileSeries(), file.getPk().getFileSer());
        assertEquals(fileId.getFileNbr(), file.getPk().getFileNbr());
        assertNotNull(file.getFilingDate());
        assertEquals(receptionForm.getEntryDate(), file.getFilingDate());
        assertEquals(receptionForm.getFile().getTitle(), file.getTitle());
        assertEquals(file.getLawCode(), cfLawApplicationSubTypeRepository.findByApplicationTypeAndSubtype(file.getApplTyp(), file.getApplSubtyp()).getPk().getLawCode());
        assertEquals(receptionForm.getNotes(), file.getIpDoc().getNotes());

        String processType = cfApplicationTypeRepository.getOne(file.getApplTyp()).getGenerateProcTyp();
        assertEquals(processType, file.getProcTyp());

        IpProc process = procRepository.getOne(new IpProcPK(file.getProcTyp(), file.getProcNbr()));
        assertNotNull(process);
        assertNotNull(process.getFile());
        assertEquals(receptionForm.getFile().getApplicationType(), process.getApplTyp());
        assertEquals(processType, process.getFileProcTyp());
        assertEquals(process.getPk().getProcNbr(), process.getFileProcNbr());

        assertEquals(fileId.getFileSeq(), process.getFile().getPk().getFileSeq());
        assertEquals(fileId.getFileType(), process.getFile().getPk().getFileTyp());
        assertEquals(fileId.getFileSeries(), process.getFile().getPk().getFileSer());
        assertEquals(fileId.getFileNbr(), process.getFile().getPk().getFileNbr());
        assertNull(process.getUpperProc());
        entityManager.clear();
        IpDoc doc = docRepository.getOne(file.getIpDoc().getPk());
        assertNotNull(doc);
        assertNotNull(doc.getFile());
        assertEquals(fileId.getFileSeq(), doc.getFile().getPk().getFileSeq());
        assertEquals(fileId.getFileType(), doc.getFile().getPk().getFileTyp());
        assertEquals(fileId.getFileSeries(), doc.getFile().getPk().getFileSer());
        assertEquals(fileId.getFileNbr(), doc.getFile().getPk().getFileNbr());
        assertEquals(fileId.getFileSeries(), doc.getDocSeqSeries());
        assertEquals(fileId.getFileNbr(), doc.getDocSeqNbr());
        assertEquals(SecurityUtils.getLoggedUserId(), doc.getReceptionUserId());
        assertEquals(receptionForm.getFile().getApplicationType(), doc.getApplTyp());
        assertEquals(receptionForm.getFile().getApplicationSubType(), doc.getApplSubtyp());
        if (receptionForm.getExternalSystemId()!= null) {
            assertEquals(receptionForm.getExternalSystemId(), doc.getExternalSystemId());
            assertEquals(receptionForm.getExternalSystemId(), doc.getExternalSystemId());
        }

        assertNotNull(doc.getFilingDate());
        assertEquals(receptionForm.getEntryDate(), doc.getFilingDate());
        assertEquals(receptionForm.getOriginalExpected(), MapperHelper.getTextAsBoolean(doc.getIndFaxReception()));
        assertEquals("SC", doc.getReceptionWcode());

//        boolean exists = fileService.isFileExist("BG", "Е", 2018, 11553009);
//        assertTrue(exists);
    }
}
