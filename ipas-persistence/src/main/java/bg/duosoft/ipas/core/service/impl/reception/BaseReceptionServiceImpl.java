package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.mapper.document.DocumentIdMapper;
import bg.duosoft.ipas.core.mapper.process.ProcessIdMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfDocSequence;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfProcessType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatusPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.repository.entity.custom.SequenceRepository;
import bg.duosoft.ipas.persistence.repository.entity.dailylog.DailyLogRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfProcessTypeRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@LogExecutionTime
public abstract class BaseReceptionServiceImpl {

    @Autowired
    private SequenceRepository sequenceRepository;
    @Autowired
    private DocumentIdMapper documentIdMapper;
    @Autowired
    private DailyLogRepository dailyLogRepository;
    @Autowired
    private ProcessIdMapper processIdMapper;
    @Autowired
    private CfProcessTypeRepository cfProcessTypeRepository;

    protected CDocumentId generateDocId(CReception receptionForm) {
        CDocumentId docId = new CDocumentId(receptionForm.getDocOri(), DefaultValue.DEFAULT_DOC_LOG, sequenceRepository.getDocSeries(receptionForm.getDocOri()), sequenceRepository.getNextDocNumber(receptionForm.getDocOri()));
        return docId;
    }
    protected CDocumentSeqId generateDocSeqId(String docOri, String docSeqType) {
        Integer docSeqSeries = sequenceRepository.getDocSeqSeries(docOri, docSeqType);
        Integer docSeqNbr = sequenceRepository.getNextDocSequenceNumber(docSeqType, docSeqSeries);
        return new CDocumentSeqId(docSeqType, docSeqSeries, docSeqNbr);
    }

    protected void prepareDoc(IpDoc doc, CDocumentId docId, CDocumentSeqId documentSeqId) {

        doc.setPk(documentIdMapper.toEntity(docId));

        doc.setIpDailyLog(dailyLogRepository.getOpenedDailyLog(docId.getDocOrigin()).get());


        doc.setDocSeqNbr(documentSeqId.getDocSeqNbr());
        doc.setDocSeqTyp(new CfDocSequence(documentSeqId.getDocSeqType()));
        doc.setDocSeqSeries(documentSeqId.getDocSeqSeries());

    }

    protected void basePrepareProc(IpProc proc, CProcessId processId, CProcessId upperProcessId, CProcessId fileProcessId) {
        CfProcessType processType = cfProcessTypeRepository.getOne(processId.getProcessType());

        proc.setPk(processIdMapper.toEntity(processId));
        if (upperProcessId != null) {
            proc.setUpperProc(new IpProc());
            proc.getUpperProc().setPk(processIdMapper.toEntity(upperProcessId));
        }
        if (fileProcessId != null) {
            proc.setFileProcNbr(fileProcessId.getProcessNbr());
            proc.setFileProcTyp(fileProcessId.getProcessType());
        }
        proc.setStatusCode(new CfStatus(new CfStatusPK(processId.getProcessType(), processType.getPrimaryIniStatusCode())));
    }

}
