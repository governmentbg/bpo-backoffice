package bg.duosoft.ipas.test.service.logging;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.service.logging.LogChangesService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentLogChanges;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkLogChangesRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentLogChangesRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocLogChangesRepository;
import bg.duosoft.ipas.test.TestBase;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.text.html.parser.Entity;

import static org.junit.Assert.assertEquals;

/**
 * User: Georgi
 * Date: 4.9.2020 г.
 * Time: 14:54
 */
@Transactional
public class LogChangeServiceTest extends TestBase {
    @Autowired
    private MarkService markService;
    @Autowired
    private PatentService patentService;
    @Autowired
    private LogChangesService logChangesService;
    @Autowired
    private UserdocService userdocService;
    @Autowired
    private IpMarkLogChangesRepository ipMarkLogChangesRepository;
    @Autowired
    private IpPatentLogChangesRepository ipPatentLogChangesRepository;
    @Autowired
    private IpUserdocLogChangesRepository ipUserdocLogChangesRepository;

    private static CFileId PATENT_FILE_ID = new CFileId("BG", "P", 2018, 112748);
    private static CFileId MARK_FILE_ID = new CFileId("BG", "N", 2013, 130109);
    private static CDocumentId DOCUMENT_ID = new CDocumentId("BG", "E", 2004, 813406);

    private CMark getMark() {
        return markService.findMark(MARK_FILE_ID, true);
    }
    private CPatent getPatent() {
        return patentService.findPatent(PATENT_FILE_ID, true);
    }
    private CUserdoc getUserdoc() {
        return userdocService.findUserdoc(DOCUMENT_ID);
    }
    @Test
    public void testInsertMarkLogChanges() {
        CMark oldMark = getMark();
        CMark newMark = getMark();
        newMark.getFile().setNotes("Alabala");
        Integer oldLogChangeNumber = ipMarkLogChangesRepository.getMaxLogChangeNumber(MARK_FILE_ID.getFileSeq(), MARK_FILE_ID.getFileType(), MARK_FILE_ID.getFileSeries(), MARK_FILE_ID.getFileNbr()).orElse(0);
        logChangesService.insertMarkLogChanges(oldMark, newMark);
        Integer newLogChangeNumber = ipMarkLogChangesRepository.getMaxLogChangeNumber(MARK_FILE_ID.getFileSeq(), MARK_FILE_ID.getFileType(), MARK_FILE_ID.getFileSeries(), MARK_FILE_ID.getFileNbr()).orElse(0);
        assertEquals( oldLogChangeNumber + 1, (Object) newLogChangeNumber);
    }

    @Test
    public void testInsertPatentLogChanges() {
        CPatent oldPatent = getPatent();
        CPatent newPatent = getPatent();
        newPatent.getFile().setNotes("Alabala");
        Integer oldLogChangeNumber = ipPatentLogChangesRepository.getMaxLogChangeNumber(PATENT_FILE_ID.getFileSeq(), PATENT_FILE_ID.getFileType(), PATENT_FILE_ID.getFileSeries(), PATENT_FILE_ID.getFileNbr()).orElse(0);
        logChangesService.insertPatentLogChanges(oldPatent, newPatent);
        Integer newLogChangeNumber = ipPatentLogChangesRepository.getMaxLogChangeNumber(PATENT_FILE_ID.getFileSeq(), PATENT_FILE_ID.getFileType(), PATENT_FILE_ID.getFileSeries(), PATENT_FILE_ID.getFileNbr()).orElse(0);
        assertEquals( oldLogChangeNumber + 1, (Object) newLogChangeNumber);
    }


    @Test
    public void testInsertPatentUserdocLogChanges() {
        Integer oldLogChangeNumber = ipPatentLogChangesRepository.getMaxLogChangeNumber(PATENT_FILE_ID.getFileSeq(), PATENT_FILE_ID.getFileType(), PATENT_FILE_ID.getFileSeries(), PATENT_FILE_ID.getFileNbr()).orElse(0);
        logChangesService.insertObjectUserdocLogChanges(PATENT_FILE_ID, new CDocumentId("BG", "E", 2013, 123456), "UpdateOwners");
        Integer newLogChangeNumber = ipPatentLogChangesRepository.getMaxLogChangeNumber(PATENT_FILE_ID.getFileSeq(), PATENT_FILE_ID.getFileType(), PATENT_FILE_ID.getFileSeries(), PATENT_FILE_ID.getFileNbr()).orElse(0);
        assertEquals( oldLogChangeNumber + 1, (Object) newLogChangeNumber);
    }
    @Test
    public void testInsertMarkUserdocLogChanges() {
        Integer oldLogChangeNumber = ipMarkLogChangesRepository.getMaxLogChangeNumber(MARK_FILE_ID.getFileSeq(), MARK_FILE_ID.getFileType(), MARK_FILE_ID.getFileSeries(), MARK_FILE_ID.getFileNbr()).orElse(0);
        logChangesService.insertObjectUserdocLogChanges(MARK_FILE_ID, new CDocumentId("BG", "E", 2013, 123456), "UpdateOwners");
        Integer newLogChangeNumber = ipMarkLogChangesRepository.getMaxLogChangeNumber(MARK_FILE_ID.getFileSeq(), MARK_FILE_ID.getFileType(), MARK_FILE_ID.getFileSeries(), MARK_FILE_ID.getFileNbr()).orElse(0);
        assertEquals( oldLogChangeNumber + 1, (Object) newLogChangeNumber);
    }
    @Test
    public void testInsertUserdocLogChanges() {
        CUserdoc oldUserdoc = getUserdoc();
        CUserdoc newUserdoc = getUserdoc();
        newUserdoc.setApplicantNotes("alabala");
        Integer oldLogChangeNumber = ipUserdocLogChangesRepository.getMaxLogChangeNumber(DOCUMENT_ID.getDocOrigin(), DOCUMENT_ID.getDocLog(), DOCUMENT_ID.getDocSeries(), DOCUMENT_ID.getDocNbr()).orElse(0);
        logChangesService.insertUserdocLogChanges(oldUserdoc, newUserdoc);
        Integer newLogChangeNumber = ipUserdocLogChangesRepository.getMaxLogChangeNumber(DOCUMENT_ID.getDocOrigin(), DOCUMENT_ID.getDocLog(), DOCUMENT_ID.getDocSeries(), DOCUMENT_ID.getDocNbr()).orElse(0);
        assertEquals( oldLogChangeNumber + 1, (Object) newLogChangeNumber);

    }
}
