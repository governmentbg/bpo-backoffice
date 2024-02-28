package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.process.ProcessIdMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionDocFilesMapper;
import bg.duosoft.ipas.core.mapper.reception.file.FileReceptionProcMapper;
import bg.duosoft.ipas.core.mapper.reception.file.FileReceptionFileMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfDocSequence;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfProcessType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatusPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.dailylog.DailyLogRepository;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocRepository;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfApplicationTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfFileTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfProcessTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@LogExecutionTime
class IpObjectReceptionServiceImpl extends BaseReceptionServiceImpl {

    @Autowired
    private FileReceptionFileMapper fileReceptionFileMapper;
    @Autowired
    private FileReceptionProcMapper fileReceptionProcMapper;
    @Autowired
    private SequenceRepository sequenceRepository;
    @Autowired
    private ReceptionDocFilesMapper receptionDocFilesMapper;
    @Autowired
    private IpFileRepository ipFileRepository;
    @Autowired
    private CfApplicationTypeRepository cfApplicationTypeRepository;
    @Autowired
    private CfProcessTypeRepository cfProcessTypeRepository;
    @Autowired
    private IpProcRepository ipProcRepository;
    @Autowired
    private CfFileTypeRepository cfFileTypeRepository;
    @Autowired
    private IpDocRepository docRepository;
    public CReceptionResponse insertIpObjectReception(CReception receptionForm) {
        IpFile file = fileReceptionFileMapper.toEntity(receptionForm);
        CDocumentId docId = generateDocId(receptionForm);
        CDocumentSeqId documentSeqId = receptionForm.getDocumentSeqId() == null ? generateDocSeqId(docId, receptionForm.getFile().getFileId()) : receptionForm.getDocumentSeqId();

        prepareDoc(file.getIpDoc(), docId, documentSeqId);

        prepareFile(file, receptionForm.getFile().getFileId(), docId);
        file = ipFileRepository.save(file);

        IpProc proc = fileReceptionProcMapper.toEntity(receptionForm);
        CProcessId processId = new CProcessId(cfApplicationTypeRepository.getOne(file.getApplTyp()).getGenerateProcTyp(), sequenceRepository.getNextSequenceValue(SequenceRepository.SEQUENCE_NAME.SEQUENCE_NAME_PROC_NBR));
        prepareProc(proc, processId, file);
        ipProcRepository.save(proc);
        file.setProcNbr(processId.getProcessNbr());
        file.setProcTyp(processId.getProcessType());

        ipFileRepository.saveAndFlush(file);

        //tyj kato poradi nqkakva prichina ne se setvat fileId fields v ipDoc, te se update-vat nakraq. Qvno neshto ne srabotva kakto trqbva, zashtoto IpDoc v IpFile se map-va po docId, dokato IpFile v IpDoc se mapva po fileId
        docRepository.updateFileKey(file.getPk().getFileSeq(), file.getPk().getFileTyp(), file.getPk().getFileSer(), file.getPk().getFileNbr(),
                                    docId.getDocOrigin(), docId.getDocLog(), docId.getDocSeries(), docId.getDocNbr());
        return new CReceptionResponse(docId, documentSeqId, receptionForm.getFile().getFileId());
    }


    private void prepareFile(IpFile file, CFileId fileId, CDocumentId docId) {
        IpDocFiles docFiles = receptionDocFilesMapper.toEntity(fileId, docId);
        file.setIpDocFilesCollection(new ArrayList<>());
        file.getIpDocFilesCollection().add(docFiles);
    }

    private CDocumentSeqId generateDocSeqId(CDocumentId docId, CFileId fileId) {
        String docSeqType = cfFileTypeRepository.getOne(fileId.getFileType()).getDocSeqTyp();
        return generateDocSeqId(docId.getDocOrigin(), docSeqType);
    }

    private void prepareProc(IpProc proc, CProcessId processId, IpFile file) {
        CfProcessType processType = cfProcessTypeRepository.getOne(processId.getProcessType());
        if (!Objects.equals(processType.getRelatedToWcode(), 1)) {
            throw new RuntimeException("Incorrect RelatedToWcode for the given processType:" + processId.getProcessType());
        }
        proc.setFile(file);

        //za osnoven obekt nqma upperProcessId, a fileProcessId sochi kym sebe si
        basePrepareProc(proc, processId, null, processId);
    }


}
