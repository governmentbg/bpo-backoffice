package bg.duosoft.ipas.core.service.impl.file;

import bg.duosoft.ipas.core.mapper.file.FileRecordalMapper;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraDataValue;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.service.UserdocExtraDataService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileRecordalService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.UserdocExtraDataTypeCode;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpFileRecordal;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpFileRecordalPK;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRecordalRepository;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@LogExecutionTime
public class FileRecordalServiceImpl implements FileRecordalService {

    @Autowired
    private IpFileRecordalRepository ipFileRecordalRepository;

    @Autowired
    private FileRecordalMapper fileRecordalMapper;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private UserdocExtraDataService userdocExtraDataService;

    @Autowired
    private DocService docService;

    @Override
    public CFileRecordal insertInvalidationOfRecordal(CDocumentId documentId, CActionId actionId, Date invalidationDate) {
        CUserdoc userdoc = userdocService.findUserdoc(documentId);
        if (Objects.isNull(userdoc)) {
            throw new RuntimeException("Cannot find userdoc with id = " + documentId);
        }

        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdoc.getUserdocParentData());
        CDocumentId recordalUserdocId = UserdocUtils.selectRecordalUserdocIdByInvalidationUserdoc(userdoc);
        CFileRecordal existingRecordal = selectRecordal(recordalUserdocId, fileId);
        if (Objects.isNull(existingRecordal)) {
            throw new RuntimeException("Cannot find recordal with PK: " + userdoc.getDocumentId().toString() + fileId.toString());
        }

        existingRecordal.setInvalidationDocumentId(userdoc.getDocumentId());
        existingRecordal.setInvalidationDate(invalidationDate);
        existingRecordal.setInvalidationActionId(actionId);
        userdocExtraDataService.save(recordalUserdocId, UserdocExtraDataTypeCode.INVALIDATION_DATE.name(), CUserdocExtraDataValue.builder().dateValue(invalidationDate).build());
        return updateRecordal(existingRecordal);
    }

    @Override
    public CFileRecordal insertNewRecordal(CDocumentId documentId, CActionId actionId, Date recordalDate) {
        CUserdoc userdoc = userdocService.findUserdoc(documentId);
        if (Objects.isNull(userdoc)) {
            throw new RuntimeException("Cannot find userdoc with id = " + documentId);
        }

        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdoc.getUserdocParentData());
        CFileRecordal newRecordalObject = createRecordalObject(documentId, userdoc, actionId, recordalDate, fileId);
        userdocExtraDataService.save(documentId, UserdocExtraDataTypeCode.EFFECTIVE_DATE.name(), CUserdocExtraDataValue.builder().dateValue(recordalDate).build());
        return updateRecordal(newRecordalObject);
    }

    @Override
    public CFileRecordal updateRecordal(CFileRecordal fileRecordal) {
        if (Objects.isNull(fileRecordal))
            return null;

        IpFileRecordal ipFileRecordal = fileRecordalMapper.toEntity(fileRecordal);
        IpFileRecordal saved = ipFileRecordalRepository.save(ipFileRecordal);
        return fileRecordalMapper.toCore(saved);
    }

    @Override
    public CFileRecordal selectRecordal(CDocumentId documentId, CFileId fileId) {
        IpFileRecordalPK pk = new IpFileRecordalPK();
        pk.setDocOri(documentId.getDocOrigin());
        pk.setDocLog(documentId.getDocLog());
        pk.setDocSer(documentId.getDocSeries());
        pk.setDocNbr(documentId.getDocNbr());
        pk.setFileSeq(fileId.getFileSeq());
        pk.setFileTyp(fileId.getFileType());
        pk.setFileSer(fileId.getFileSeries());
        pk.setFileNbr(fileId.getFileNbr());
        IpFileRecordal ipFileRecordal = ipFileRecordalRepository.findById(pk).orElse(null);
        if (Objects.isNull(ipFileRecordal))
            return null;

        return fileRecordalMapper.toCore(ipFileRecordal);
    }

    //TODO Maybe we should increase main object row version number after update ?
    @Override
    public void syncUserdocEffectiveAndInvalidationDate(CUserdoc userdoc) {
        if (Objects.nonNull(userdoc)) {
            CDocumentId id = userdoc.getDocumentId();
            IpFileRecordal recordal = ipFileRecordalRepository.selectByRecordalUserdoc(id.getDocOrigin(), id.getDocLog(), id.getDocSeries(), id.getDocNbr());
            if (Objects.nonNull(recordal)) {
                recordal.setDate(UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.EFFECTIVE_DATE.name(), userdoc.getUserdocExtraData()));
                if (Objects.nonNull(recordal.getInvalidationDocOri()) && Objects.nonNull(recordal.getInvalidationDocLog()) && Objects.nonNull(recordal.getInvalidationDocSer()) && Objects.nonNull(recordal.getInvalidationDocNbr())) {
                    recordal.setInvalidationDate(UserdocExtraDataUtils.selectDate(UserdocExtraDataTypeCode.INVALIDATION_DATE.name(), userdoc.getUserdocExtraData()));
                }
                ipFileRecordalRepository.save(recordal);
            }
        }
    }

    @Override
    public CFileRecordal selectRecordalByActionId(CActionId id) {
        if (Objects.isNull(id))
            return null;

        List<IpFileRecordal> ipFileRecordals = ipFileRecordalRepository.selectByRecordalActionId(id.getProcessId().getProcessType(), id.getProcessId().getProcessNbr(), id.getActionNbr());
        if (CollectionUtils.isEmpty(ipFileRecordals))
            return null;

        if (ipFileRecordals.size() > 1) {
            throw new RuntimeException("There are more than one recordals with action id " + id);
        }

        return fileRecordalMapper.toCore(ipFileRecordals.get(0));
    }

    @Override
    public CFileRecordal selectRecordalByUserdocId(CDocumentId id) {
        if (Objects.isNull(id))
            return null;

        IpFileRecordal recordal = ipFileRecordalRepository.selectByRecordalUserdoc(id.getDocOrigin(), id.getDocLog(), id.getDocSeries(), id.getDocNbr());
        if (Objects.isNull(recordal))
            return null;

        return fileRecordalMapper.toCore(recordal);
    }

    @Override
    public CFileRecordal selectRecordalByInvalidationUserdocId(CDocumentId id) {
        if (Objects.isNull(id))
            return null;

        IpFileRecordal recordal = ipFileRecordalRepository.selectByInvalidationUserdoc(id.getDocOrigin(), id.getDocLog(), id.getDocSeries(), id.getDocNbr());
        if (Objects.isNull(recordal))
            return null;

        return fileRecordalMapper.toCore(recordal);
    }

    private CFileRecordal createRecordalObject(CDocumentId documentId, CUserdoc userdoc, CActionId actionId, Date recordalDate, CFileId fileId) {
        if (Objects.isNull(recordalDate))
            throw new RuntimeException("Recordal date is empty for userdoc " + documentId.createFilingNumber());

        CUserdocPanel recordalPanel = UserdocUtils.selectRecordalPanel(userdoc.getUserdocType().getPanels());
        if (Objects.isNull(recordalPanel))
            throw new RuntimeException("Recordal panel is empty for userdoc " + documentId.createFilingNumber());

        CFileRecordal fileRecordal = new CFileRecordal();
        fileRecordal.setDate(recordalDate);
        fileRecordal.setDocumentId(documentId);
        fileRecordal.setActionId(actionId);
        fileRecordal.setFileId(fileId);
        fileRecordal.setType(recordalPanel.getPanel());
        return fileRecordal;
    }
}
