package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.mapper.process.ProcessIdMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionDocFilesMapper;
import bg.duosoft.ipas.core.mapper.reception.userdoc.UserdocReceptionProcMapper;
import bg.duosoft.ipas.core.mapper.reception.userdoc.UserdocReceptionUserdocMapper;
import bg.duosoft.ipas.core.mapper.reception.userdoc.UserdocReceptionUserdocProcsMapper;
import bg.duosoft.ipas.core.mapper.reception.userdoc.UserdocReceptionUserdocTypeMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.*;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocProcs;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.dailylog.DailyLogRepository;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocFilesRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfProcessTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUserdocTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocProcsRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocTypesRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Objects;

/**
 * User: Georgi
 * Date: 8.6.2020 г.
 * Time: 16:24
 *
 * Service that adds data to the original IPAS tables - IP_USERDOC, IP_DOC, IP_DOC_FILES, IP_USERDOC_TYPES, IP_PROC, IP_USERDOC_PROCS
 */
@Slf4j
@Service
@Transactional
@LogExecutionTime
class LocalUserdocReceptionServiceImpl extends BaseReceptionServiceImpl {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private UserdocReceptionUserdocMapper userdocReceptionUserdocMapper;
    @Autowired
    private SequenceRepository sequenceRepository;
    @Autowired
    private DocumentIdMapper documentIdMapper;
    @Autowired
    private CfUserdocTypeRepository cfUserdocTypeRepository;
    @Autowired
    private UserdocReceptionUserdocTypeMapper userdocReceptionUserdocTypeMapper;
    @Autowired
    private ProcessService processService;
    @Autowired
    private ReceptionDocFilesMapper receptionDocFilesMapper;
    @Autowired
    private IpDocFilesRepository ipDocFilesRepository;
    @Autowired
    private UserdocReceptionProcMapper userdocReceptionProcMapper;
    @Autowired
    private CfProcessTypeRepository cfProcessTypeRepository;
    @Autowired
    private FileIdMapper fileIdMapper;
    @Autowired
    private IpProcRepository ipProcRepository;
    @Autowired
    private UserdocReceptionUserdocProcsMapper userdocReceptionUserdocProcsMapper;
    @Autowired
    private IpUserdocProcsRepository ipUserdocProcsRepository;

    public synchronized CReceptionResponse insertUserdocReception(CReception receptionForm) {
        IpUserdoc ud = userdocReceptionUserdocMapper.toEntity(receptionForm);
        CDocumentId docId = generateDocId(receptionForm);
        CDocumentSeqId docSeqId = receptionForm.getDocumentSeqId() == null ? generateDocSeqId(docId, receptionForm) : receptionForm.getDocumentSeqId();

        CFileId masterFileId = receptionForm.getUserdoc().isRelatedToFile() ? receptionForm.getUserdoc().getFileId() : processService.selectTopProcessFileId(processService.selectUserdocProcessId(receptionForm.getUserdoc().getDocumentId()));
        prepareDoc(ud.getIpDoc(), docId, docSeqId);

        prepareUserdoc(receptionForm, ud, docId);
        em.persist(ud);//persisting through the entityManager, otherwise the spring generates a SELECT to the IP_USERDOC_TYPES before the insertion to the IP_DOC which throws EntityNotFoundException
        IpDocFiles ipDocFiles = createIpDocFiles(docId, masterFileId);
        ipDocFilesRepository.save(ipDocFiles);


        IpProc proc = userdocReceptionProcMapper.toEntity(receptionForm);
        CProcessId processId = new CProcessId(cfUserdocTypeRepository.getOne(receptionForm.getUserdoc().getUserdocType()).getGenerateProcTyp(), sequenceRepository.getNextSequenceValue(SequenceRepository.SEQUENCE_NAME.SEQUENCE_NAME_PROC_NBR));
        prepareProc(receptionForm, proc, ud, masterFileId, processId);
        ipProcRepository.save(proc);
        IpUserdocProcs ipUserdocProcs = createIpUserdocProcs(docId, processId, masterFileId, receptionForm);
        ipUserdocProcsRepository.save(ipUserdocProcs);
        em.flush();
        em.refresh(ud);//refresh-va se IpUserdoc-a, zashtoto inache nqma popylne IpProcSimple object. Qvno kato se vikne obekta ot entity manager-a, ako toj e managed, se vry6ta direktno, t.e. bez da se popylvat lipsvashtite danni (v sluchaq IpProcSimple)

        return new CReceptionResponse(docId, docSeqId, null);
    }
    private CDocumentSeqId generateDocSeqId(CDocumentId docId, CReception r) {
        CfUserdocType udt = cfUserdocTypeRepository.getOne(r.getUserdoc().getUserdocType());
        String docSeqType = udt.getDocSeqTyp();
        return generateDocSeqId(docId.getDocOrigin(), docSeqType);
    }

    private void prepareUserdoc(CReception r, IpUserdoc ud, CDocumentId docId) {
        if (r.getUserdoc().isRelatedToUserdoc()) {
            CDocumentId affectedDoc = r.getUserdoc().getDocumentId();
            ud.setAffectedDocOri(affectedDoc.getDocOrigin());
            ud.setAffectedDocLog(affectedDoc.getDocLog());
            ud.setAffectedDocSer(affectedDoc.getDocSeries());
            ud.setAffectedDocNbr(affectedDoc.getDocNbr());
        }
        ud.setPk(documentIdMapper.toEntity(docId));
        addUserdocTypes(r, ud, docId);
    }

    private void addUserdocTypes(CReception r, IpUserdoc ud, CDocumentId docId) {
        ud.setIpUserdocTypes(new ArrayList<>());
        ud.getIpUserdocTypes().add(userdocReceptionUserdocTypeMapper.toEntity(r, docId));
    }

    private IpDocFiles createIpDocFiles(CDocumentId docId, CFileId masterFileId) {
        return receptionDocFilesMapper.toEntity(masterFileId, docId);
    }

    private IpUserdocProcs createIpUserdocProcs(CDocumentId docId, CProcessId processId, CFileId masterFileId, CReception reception) {
        return userdocReceptionUserdocProcsMapper.toEntity(reception, masterFileId, docId, processId);
    }

    private void prepareProc(CReception reception, IpProc proc, IpUserdoc ipUserdoc, CFileId masterFileId, CProcessId processId) {
        CfProcessType processType = cfProcessTypeRepository.getOne(processId.getProcessType());
        if (!Objects.equals(processType.getRelatedToWcode(), 2)) {
            throw new RuntimeException("Incorrect RelatedToWcode for the given processType:" + processId.getProcessType());
        }

        CProcessId fileProcessId = processService.selectFileProcessId(masterFileId);
        CProcessId upperProcessId = reception.getUserdoc().isRelatedToFile() ? fileProcessId : processService.selectUserdocProcessId(reception.getUserdoc().getDocumentId());

        basePrepareProc(proc, processId, upperProcessId, fileProcessId);

        proc.setUserdocIpDoc(ipUserdoc.getIpDoc());
        proc.setUserdocFile(new IpFile());
        proc.getUserdocFile().setPk(fileIdMapper.toEntity(masterFileId));

    }

}
