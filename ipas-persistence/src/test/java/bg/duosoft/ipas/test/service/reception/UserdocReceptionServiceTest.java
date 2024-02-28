package bg.duosoft.ipas.test.service.reception;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFilesPK;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.UserdocReceptionRelation;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.UserdocReceptionRelationPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocProcs;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocTypes;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocTypesPK;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocFilesRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUserdocTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.DocSequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.UserdocReceptionRelationRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocProcsRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocTypesRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.test.mapper.reception.ReceptionMapperTestBase;
import bg.duosoft.ipas.test.mapper.reception.UserdocReceptionMapperTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

/**
 * User: Georgi
 * Date: 8.6.2020 г.
 * Time: 18:03
 */
public class UserdocReceptionServiceTest extends TestBase {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReceptionService receptionService;
    @Autowired
    private IpUserdocRepository ipUserdocRepository;
    @Autowired
    private DailyLogService dailyLogService;
    @Autowired
    private SequenceRepository sequenceRepository;
    @Autowired
    private IpDocFilesRepository ipDocFilesRepository;
    @Autowired
    private FileIdMapper fileIdMapper;
    @Autowired
    private IpProcRepository ipProcRepository;
    @Autowired
    private IpUserdocProcsRepository ipUserdocProcsRepository;
    @Autowired
    private CfUserdocTypeRepository cfUserdocTypeRepository;
    @Test
    @Transactional
    public void createUserdocFileReceptionWithoutDocflowRegistration() {
        CReception receptionForm = UserdocReceptionMapperTest.createUserdocReception(true);
        CReceptionResponse resp = receptionService.createReception(receptionForm);
        entityManager.flush();
        entityManager.clear();
        validate(receptionForm, resp);
    }
    @Test
    @Transactional
    public void createUserdocUserdocReceptionWithoutDocflowRegistration() {
        CReception receptionForm = UserdocReceptionMapperTest.createUserdocReception(false);
        CReceptionResponse resp = receptionService.createReception(receptionForm);
        entityManager.flush();
        entityManager.clear();
        validate(receptionForm, resp);
    }
    @Test
    @Transactional
    public void createEVUserdocUserdocReceptionWithoutDocflowRegistration() {
        CReception receptionForm = ReceptionMapperTestBase.createReceptionBase();
        receptionForm.setUserdoc(new CReceptionUserdoc());
        receptionForm.getUserdoc().setUserdocType("ЕПИВ");
        receptionForm.setExternalSystemId("ЕПИВ-2019/000001");
        receptionForm.getUserdoc().setFileId(new CFileId("BG", "T", 2015, 15185050));


        CReceptionResponse resp = receptionService.createReception(receptionForm);
        entityManager.flush();
        entityManager.clear();
        validate(receptionForm, resp);
        assertEquals(1, (int)resp.getDocSeqId().getDocSeqNbr());
        assertEquals(2019, (int)resp.getDocSeqId().getDocSeqSeries());
    }
    private void validate(CReception receptionForm, CReceptionResponse resp) {
        IpUserdoc userdoc = ipUserdocRepository.getOne(new IpDocPK(resp.getDocId().getDocOrigin(), resp.getDocId().getDocLog(), resp.getDocId().getDocSeries(), resp.getDocId().getDocNbr()));
        assertEquals(receptionForm.getNotes(), userdoc.getIpDoc().getNotes());
        if (receptionForm.getUserdoc().isRelatedToUserdoc()) {
            CDocumentId affectedUd = receptionForm.getUserdoc().getDocumentId();
            assertEquals(affectedUd.getDocOrigin(), userdoc.getAffectedDocOri());
            assertEquals(affectedUd.getDocLog(), userdoc.getAffectedDocLog());
            assertEquals(affectedUd.getDocSeries(), userdoc.getAffectedDocSer());
            assertEquals(affectedUd.getDocNbr(), userdoc.getAffectedDocNbr());
        } else {
            assertNull(userdoc.getAffectedDocOri());
            assertNull(userdoc.getAffectedDocLog());
            assertNull(userdoc.getAffectedDocSer());
            assertNull(userdoc.getAffectedDocNbr());
        }

        IpDoc doc = userdoc.getIpDoc();
        assertEquals(dailyLogService.getWorkingDate(), doc.getIpDailyLog().getPk().getDailyLogDate());
        assertEquals(receptionForm.getEntryDate(), doc.getFilingDate());
        assertEquals("PE", doc.getReceptionWcode());
        assertEquals(receptionForm.getOriginalExpected(), MapperHelper.getTextAsBoolean(doc.getIndFaxReception()));
        assertEquals("N", doc.getIndNotAllFilesCapturedYet());
        assertNotNull(doc.getDocSeqTyp());
        assertEquals(cfUserdocTypeRepository.getOne(receptionForm.getUserdoc().getUserdocType()).getDocSeqTyp(), doc.getDocSeqTyp().getDocSeqTyp());
        assertNotNull(doc.getDocSeqSeries());
        assertNotNull(doc.getDocSeqNbr());
        assertEquals(sequenceRepository.getNextDocSequenceNumber(doc.getDocSeqTyp().getDocSeqTyp(), doc.getDocSeqSeries()), (Object)(doc.getDocSeqNbr() + 1));
        assertEquals(receptionForm.getExternalSystemId(), doc.getExternalSystemId());

        assertNotNull(userdoc.getIpUserdocTypes());
        assertEquals(1, userdoc.getIpUserdocTypes().size());
        IpUserdocTypes udt = userdoc.getIpUserdocTypes().get(0);
        assertEquals("S", udt.getIndProcessFirst());
        assertEquals(receptionForm.getUserdoc().getUserdocType(), udt.getPk().getUserdocTyp());
        assertEquals(doc.getPk().getDocOri(), udt.getPk().getDocOri());
        assertEquals(doc.getPk().getDocLog(), udt.getPk().getDocLog());
        assertEquals(doc.getPk().getDocSer(), udt.getPk().getDocSer());
        assertEquals(doc.getPk().getDocNbr(), udt.getPk().getDocNbr());
        assertNull(udt.getProcNbr());
        assertNull(udt.getProcTyp());

        CDocumentId dId = receptionForm.getUserdoc().getDocumentId();
        IpFilePK filePk = receptionForm.getUserdoc().isRelatedToUserdoc() ? ipDocFilesRepository.selectMainObjectIdOfUserdoc(dId.getDocOrigin(), dId.getDocLog(), dId.getDocSeries(), dId.getDocNbr()) : fileIdMapper.toEntity(receptionForm.getUserdoc().getFileId());

        validateDocFiles(doc.getPk(), filePk);
        if (receptionForm.getUserdoc().isRelatedToFile()) {
            validateFileProcess(receptionForm, resp);
        } else {
            validateUserdocProcess(receptionForm, resp);
        }
    }
    private void validateDocFiles(IpDocPK ipDocPK, IpFilePK ipFilePK) {
        IpDocFiles ipDocFile = ipDocFilesRepository.findById(new IpDocFilesPK(ipDocPK.getDocOri(), ipDocPK.getDocLog(), ipDocPK.getDocSer(), ipDocPK.getDocNbr(), ipFilePK.getFileSeq(), ipFilePK.getFileTyp(), ipFilePK.getFileSer(), ipFilePK.getFileNbr())).orElse(null);
        assertNotNull(ipDocFile);
        assertEquals((Object)1, ipDocFile.getRowVersion());
        assertNull(ipDocFile.getPriorExpirationDate());
        assertEquals(ipDocPK.getDocOri(), ipDocFile.getPk().getDocOri());
        assertEquals(ipDocPK.getDocLog(), ipDocFile.getPk().getDocLog());
        assertEquals(ipDocPK.getDocSer(), ipDocFile.getPk().getDocSer());
        assertEquals(ipDocPK.getDocNbr(), ipDocFile.getPk().getDocNbr());
        assertEquals(ipFilePK.getFileSeq(), ipDocFile.getPk().getFileSeq());
        assertEquals(ipFilePK.getFileTyp(), ipDocFile.getPk().getFileTyp());
        assertEquals(ipFilePK.getFileSer(), ipDocFile.getPk().getFileSer());
        assertEquals(ipFilePK.getFileNbr(), ipDocFile.getPk().getFileNbr());

    }
    private void validateFileProcess(CReception reception, CReceptionResponse resp) {
        IpProc process = getProcess(resp);
        CFileId fileId = reception.getUserdoc().getFileId();
        validateBaseProcessFields(reception, resp, process, reception.getUserdoc().getFileId());
        IpProcPK fileProcessId = ipProcRepository.selectFileProcessId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        assertNotNull(process.getUpperProc());
        assertEquals(fileProcessId.getProcTyp(), process.getUpperProc().getPk().getProcTyp());
        assertEquals(fileProcessId.getProcNbr(), process.getUpperProc().getPk().getProcNbr());
        validateIpUserdocProcs(reception, resp, fileId, process);
    }
    private IpProc getProcess(CReceptionResponse resp) {
        IpProcPK processId = ipProcRepository.selectUserdocProcessId(resp.getDocId().getDocOrigin(), resp.getDocId().getDocLog(), resp.getDocId().getDocSeries(), resp.getDocId().getDocNbr());
        IpProc process = ipProcRepository.getOne(processId);
        return process;
    }
    private void validateUserdocProcess(CReception reception, CReceptionResponse resp) {
        IpProc process = getProcess(resp);
        CDocumentId affectedDocId = reception.getUserdoc().getDocumentId();
        CDocumentId udDocId = resp.getDocId();
        CFileId fileId = fileIdMapper.toCore(ipProcRepository.selectTopProcessFileId(process.getPk().getProcTyp(), process.getPk().getProcNbr()));
        validateBaseProcessFields(reception, resp, process, fileId);

        IpProcPK upperProcPk = ipProcRepository.selectUserdocProcessId(affectedDocId.getDocOrigin(), affectedDocId.getDocLog(), affectedDocId.getDocSeries(), affectedDocId.getDocNbr());
        assertNotNull(process.getUpperProc());
        assertEquals(upperProcPk.getProcTyp(), process.getUpperProc().getPk().getProcTyp());
        assertEquals(upperProcPk.getProcNbr(), process.getUpperProc().getPk().getProcNbr());
        validateIpUserdocProcs(reception, resp, fileId, process);
    }
    private void validateBaseProcessFields(CReception reception, CReceptionResponse resp, IpProc process, CFileId fileId) {
        assertNotNull(process);
        assertNull(process.getResponsibleUser());
        assertNull(process.getExpirationDate());
        IpProcPK fileProcessId = ipProcRepository.selectFileProcessId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());

        assertEquals(fileProcessId.getProcTyp(), process.getFileProcTyp());
        assertEquals(fileProcessId.getProcNbr(), process.getFileProcNbr());
//        assertNotNull(process.getUpperProc());
//        assertEquals(fileProcessId.getProcTyp(), process.getUpperProc().getPk().getProcTyp());
//        assertEquals(fileProcessId.getProcNbr(), process.getUpperProc().getPk().getProcNbr());
        assertNotNull(process.getUserdocIpDoc());
        assertEquals(resp.getDocId().getDocOrigin(), process.getUserdocIpDoc().getPk().getDocOri());
        assertEquals(resp.getDocId().getDocLog(), process.getUserdocIpDoc().getPk().getDocLog());
        assertEquals(resp.getDocId().getDocSeries(), process.getUserdocIpDoc().getPk().getDocSer());
        assertEquals(resp.getDocId().getDocNbr(), process.getUserdocIpDoc().getPk().getDocNbr());
        assertNotNull(process.getUserdocTyp());
        assertEquals(reception.getUserdoc().getUserdocType(), process.getUserdocTyp().getUserdocTyp());
        assertNotNull(process.getUserdocFile());
        assertEquals(process.getUserdocFile().getPk().getFileSeq(), fileId.getFileSeq());
        assertEquals(process.getUserdocFile().getPk().getFileTyp(), fileId.getFileType());
        assertEquals(process.getUserdocFile().getPk().getFileSer(), fileId.getFileSeries());
        assertEquals(process.getUserdocFile().getPk().getFileNbr(), fileId.getFileNbr());
    }
    private void validateIpUserdocProcs(CReception reception, CReceptionResponse resp,  CFileId masterFileId, IpProc process) {
        IpUserdocProcs ipUserdocProcs = ipUserdocProcsRepository.findByPk_DocOriAndPk_DocLogAndPk_DocSerAndPk_DocNbr(resp.getDocId().getDocOrigin(), resp.getDocId().getDocLog(), resp.getDocId().getDocSeries(), resp.getDocId().getDocNbr());
        assertNotNull(ipUserdocProcs);
        assertEquals(reception.getUserdoc().getUserdocType(), ipUserdocProcs.getPk().getUserdocTyp());
        assertEquals(masterFileId.getFileSeq(), ipUserdocProcs.getPk().getUserdocFileSeq());
        assertEquals(masterFileId.getFileType(), ipUserdocProcs.getPk().getUserdocFileTyp());
        assertEquals(masterFileId.getFileSeries(), ipUserdocProcs.getPk().getUserdocFileSer());
        assertEquals(masterFileId.getFileNbr(), ipUserdocProcs.getPk().getUserdocFileNbr());
        assertEquals(process.getPk().getProcTyp(), ipUserdocProcs.getProcTyp());
        assertEquals(process.getPk().getProcNbr(), ipUserdocProcs.getProcNbr());
    }
}
