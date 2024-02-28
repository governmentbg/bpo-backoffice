package bg.duosoft.ipas.test.mapper.reception;

import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionDocFilesMapper;
import bg.duosoft.ipas.core.mapper.reception.file.FileReceptionDocMapper;
import bg.duosoft.ipas.core.mapper.reception.file.FileReceptionFileMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionFile;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: Georgi
 * Date: 27.5.2020 г.
 * Time: 15:55
 */
public class FileReceptionMapperTest extends ReceptionMapperTestBase {
    @Autowired
    private FileReceptionDocMapper fileReceptionDocMapper;
    @Autowired
    private FileReceptionFileMapper fileReceptionFileMapper;
    @Autowired
    private ReceptionDocFilesMapper receptionDocFilesMapper;
    @Autowired
    private FileIdMapper fileIdMapper;

    private CReception createFileReception() {
        CReception receptionForm = createReceptionBase();
        receptionForm.setFile(new CReceptionFile());
        receptionForm.getFile().setApplicationType("НМ");
        receptionForm.getFile().setTitle("test");
        return receptionForm;
    }

    private CFileId createCFileId() {
        return new CFileId("BG", "N", 2020, 123456);
    }

    @Test
    public void testDocMapping() {
        CReception receptionForm = createFileReception();
        IpDoc entity = fileReceptionDocMapper.toEntity(receptionForm);
        compareDocBase(receptionForm, entity);

        assertNotNull(entity.getApplTyp());
        assertEquals("НМ", entity.getApplTyp());
        assertEquals("notes", entity.getNotes());
        assertEquals("SC", entity.getReceptionWcode());

    }

    @Test
    public void testFileMapping() {
        CReception receptionForm = createFileReception();
        CFileId fileId = createCFileId();
//        receptionForm.setFileData(new CReceptionFileData());
        receptionForm.getFile().setFileId(fileId);
        IpFile entity = fileReceptionFileMapper.toEntity(receptionForm);
        assertEquals(receptionForm.getEntryDate(), entity.getFilingDate());
        assertEquals(fileIdMapper.toEntity(fileId), entity.getPk());
        assertEquals((Object) 1, entity.getRowVersion());
        assertEquals("НМ", entity.getApplTyp());
        assertEquals("НМ", entity.getIpDoc().getApplTyp());
        assertEquals((Object) 1, entity.getLawCode());
        assertEquals("test", entity.getTitle());
        assertEquals("ИМ", entity.getApplSubtyp());
        assertEquals("ИМ", entity.getIpDoc().getApplSubtyp());
        assertEquals(entity, entity.getIpDoc().getFile());
    }

    @Test
    public void testReceptionDocFilesMapper() {
        CFileId f = new CFileId("BG", "N", 2012, 1234);
        CDocumentId d = new CDocumentId("BG", "E", 2000, 456);
        IpDocFiles res = receptionDocFilesMapper.toEntity(f, d);
        assertEquals(f.getFileSeq(), res.getPk().getFileSeq());
        assertEquals(f.getFileType(), res.getPk().getFileTyp());
        assertEquals(f.getFileSeries(), res.getPk().getFileSer());
        assertEquals(f.getFileNbr(), res.getPk().getFileNbr());
        assertEquals(d.getDocOrigin(), res.getPk().getDocOri());
        assertEquals(d.getDocLog(), res.getPk().getDocLog());
        assertEquals(d.getDocSeries(), res.getPk().getDocSer());
        assertEquals(d.getDocNbr(), res.getPk().getDocNbr());
    }
}
