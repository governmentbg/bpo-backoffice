package bg.duosoft.ipas.core.service.impl.logging;

import bg.duosoft.ipas.core.mapper.logging.FileLogChangesMapper;
import bg.duosoft.ipas.core.mapper.logging.UserdocLogChangesMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.logging.CFileLogChanges;
import bg.duosoft.ipas.core.model.logging.CUserdocLogChanges;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.impl.ServiceBaseImpl;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.logging.LogChangesService;
import bg.duosoft.ipas.persistence.model.entity.IpFileLogChanges;
import bg.duosoft.ipas.persistence.model.entity.file.IpLogChangesPK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkLogChanges;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentLogChanges;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocLogChanges;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocLogChangesPK;
import bg.duosoft.ipas.persistence.repository.entity.IpLogChangesRepositoryBase;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkLogChangesRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentLogChangesRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocLogChangesRepository;
import bg.duosoft.ipas.util.file.FileTypeUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 30.1.2019 г.
 * Time: 12:06
 */
@Service
@Transactional
public class LogChangesServiceImpl extends ServiceBaseImpl implements LogChangesService{
    @Autowired
    private IpMarkLogChangesRepository ipMarkLogChangesRepository;

    @Autowired
    private IpPatentLogChangesRepository ipPatentLogChangesRepository;
    @Autowired
    private IpUserdocLogChangesRepository ipUserdocLogChangesRepository;
    @Autowired
    private FileLogChangesMapper fileLogChangesMapper;
    @Autowired
    private UserdocLogChangesMapper userdocLogChangesMapper;



    @Override
    public void insertMarkLogChanges(CMark oldMark, CMark newMark) {
        Optional<String> difference = getDifference(oldMark, newMark);
        if (difference.isEmpty()) {
            return;
        }
        difference.ifPresent(diff -> saveLogChangesRecord(oldMark.getFile().getFileId(), null, diff, "CMARK"));
    }
    @Override
    public void insertPatentLogChanges(CPatent oldPatent, CPatent newPatent) {
        Optional<String> difference = getDifference(oldPatent, newPatent);
        difference.ifPresent(d -> saveLogChangesRecord(oldPatent.getFile().getFileId(), null, d, "CPATENT"));
    }
    @Override
    public void insertUserdocLogChanges(CUserdoc oldUserdoc, CUserdoc newUserdoc) {
        Optional<String> difference = getDifference(oldUserdoc, newUserdoc);
        difference.ifPresent(d -> saveUserdocLogChangesRecord(oldUserdoc.getDocumentId(), d, "CUSERDOC"));
    }

    public void insertDesignLogChanges(CPatent oldMasterDesign, CPatent newMasterDesign, List<CPatent> oldSingleDesigns, List<CPatent> newSingleDesigns) {
        List<String> difference = new ArrayList<>();
        getDifference(oldMasterDesign, newMasterDesign).ifPresent(d -> addDesignDifference(oldMasterDesign.getFile().getFileId(), d, difference));
        Map<CFileId, CPatent> oldSingleDesignsPerFileId = oldSingleDesigns.stream().collect(Collectors.toMap(r -> r.getFile().getFileId(), r -> r));
        Map<CFileId, CPatent> newSingleDesignsPerFileId = newSingleDesigns.stream().collect(Collectors.toMap(r -> r.getFile().getFileId(), r -> r));

        oldSingleDesignsPerFileId.entrySet().stream().forEach(osd -> getDifference(osd.getValue(), newSingleDesignsPerFileId.get(osd.getKey())).ifPresent(d -> addDesignDifference(osd.getKey(), d, difference)));//iterates through the old designs and checks if the newDesigns contains the same design and if it is changed
        newSingleDesignsPerFileId.entrySet().stream().filter(nsd -> !oldSingleDesignsPerFileId.containsKey(nsd.getKey())).forEach(nsd -> getDifference(null, nsd.getValue()).ifPresent(d -> addDesignDifference(nsd.getKey(), d, difference)));//adds all the newDesigns that do not exist in the old designs list!!!

        if (difference.size() > 0) {
            saveLogChangesRecord(oldMasterDesign.getFile().getFileId(), null, StringUtils.join(difference, "\n"), "CPATENT");
        }
    }
    private void addDesignDifference(CFileId fileId, String difference, List<String> differences) {
        differences.add(fileId.createFilingNumber() + "\n" + difference);
    }

    public void insertObjectUserdocLogChanges(CFileId fileId, CDocumentId documentId, String userdocType) {
        saveLogChangesRecord(fileId, documentId, null, userdocType);
    }

    private void saveLogChangesRecord(CFileId fileId, CDocumentId documentId, String difference, String dataCode) {
        IpFileLogChanges logChangeRec = createLogChangesRecord(fileId.getFileType());
        IpLogChangesRepositoryBase repo = getLogChangesRepository(fileId.getFileType());
        logChangeRec.setPk(new IpLogChangesPK());
        logChangeRec.setChangeUserId(SecurityUtils.getLoggedUserId());
        logChangeRec.setDataCode(dataCode);
        logChangeRec.setRowVersion(1);
        logChangeRec.setDataVersionWcode(documentId == null ? "B" : "A");
        logChangeRec.setChangeDate(new Timestamp(new java.util.Date().getTime()));
        int maxLogNbr = (int) getLogChangesRepository(fileId.getFileType()).getMaxLogChangeNumber(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr()).orElse(0);
        maxLogNbr++;
        logChangeRec.getPk().setLogChangeNbr(maxLogNbr);
        logChangeRec.getPk().setFileSeq(fileId.getFileSeq());
        logChangeRec.getPk().setFileTyp(fileId.getFileType());
        logChangeRec.getPk().setFileSer(fileId.getFileSeries());
        logChangeRec.getPk().setFileNbr(fileId.getFileNbr());
        logChangeRec.setDataValue(difference);
        if (documentId != null) {
            logChangeRec.setChangeUserdocRef(documentId.createFilingNumber());
        }
        repo.save(logChangeRec);
    }

    private void saveUserdocLogChangesRecord(CDocumentId documentId, String difference, String dataCode) {
        IpUserdocLogChanges logChangeRec = new IpUserdocLogChanges();
        logChangeRec.setPk(new IpUserdocLogChangesPK());
        logChangeRec.setChangeUserId(SecurityUtils.getLoggedUserId());
        logChangeRec.setDataCode(dataCode);
        logChangeRec.setRowVersion(1);
        logChangeRec.setDataVersionWcode("A");
        logChangeRec.setChangeDate(new Timestamp(new java.util.Date().getTime()));
        int maxLogNbr = ipUserdocLogChangesRepository.getMaxLogChangeNumber(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr()).orElse(0);
        maxLogNbr++;
        logChangeRec.getPk().setLogChangeNbr(maxLogNbr);
        logChangeRec.getPk().setDocOri(documentId.getDocOrigin());
        logChangeRec.getPk().setDocLog(documentId.getDocLog());
        logChangeRec.getPk().setDocSer(documentId.getDocSeries());
        logChangeRec.getPk().setDocNbr(documentId.getDocNbr());
        logChangeRec.setDataValue(difference);

        ipUserdocLogChangesRepository.save(logChangeRec);
    }


    private IpFileLogChanges createLogChangesRecord(String fileType) {
        if (FileTypeUtils.isMarkFileType(fileType)) {
            return new IpMarkLogChanges();
        } else if (FileTypeUtils.isPatentFileType(fileType)) {
            return new IpPatentLogChanges();
        } else {
            throw new RuntimeException("Unknown file type "  + fileType);
        }
    }

    private IpLogChangesRepositoryBase getLogChangesRepository(String fileType) {
        if (FileTypeUtils.isPatentFileType(fileType)) {
            return ipPatentLogChangesRepository;
        } else if (FileTypeUtils.isMarkFileType(fileType)) {
            return ipMarkLogChangesRepository;
        } else {
            throw new RuntimeException("Unknown file type" + fileType);
        }
    }

    private <T extends Object> Optional<String> getDifference(T oldObject, T newObject) {
        DiffGenerator difGen = DiffGenerator.create(oldObject, newObject);
        if (!difGen.isChanged()) {
            return Optional.empty();
        }
        return Optional.of(difGen.getResult());

    }

    @Override
    public List<CFileLogChanges> getFileLogChanges(CFileId fileId, boolean addData) {
        IpLogChangesRepositoryBase repo = getLogChangesRepository(fileId.getFileType());
        List<IpFileLogChanges> res = repo.getLogChanges(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        if (res != null) {
            return res.stream().map(r -> fileLogChangesMapper.toCore(r, addData)).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CUserdocLogChanges> getUserdocLogChanges(CDocumentId documentId, boolean addData) {
        List<IpUserdocLogChanges> res = ipUserdocLogChangesRepository.getLogChanges(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
        if (res != null) {
            return res.stream().map(r -> userdocLogChangesMapper.toCore(r, addData)).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public CFileLogChanges getFileLogChange(CFileId fileId, int logChangeNumber) {
        IpFileLogChanges res = getLogChangesRepository(fileId.getFileType()).getLogChange(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), logChangeNumber);
        return res == null ? null : fileLogChangesMapper.toCore(res, true);
    }

    @Override
    public CUserdocLogChanges getUserdocLogChange(CDocumentId documentId, int logChangeNumber) {
        IpUserdocLogChanges res = ipUserdocLogChangesRepository.getLogChange(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr(), logChangeNumber);
        return res == null ? null : userdocLogChangesMapper.toCore(res, true);
    }
}
