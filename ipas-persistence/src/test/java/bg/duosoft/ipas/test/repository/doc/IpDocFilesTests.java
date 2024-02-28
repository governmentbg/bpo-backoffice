package bg.duosoft.ipas.test.repository.doc;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFilesPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class IpDocFilesTests extends TestBase {
    @Autowired
    private IpMarkRepository ipMarkRepository;

    private IpMark getMark() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 2016, 140109);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);
        return ipMark;
    }
    @Test
    @Transactional
    public void deleteDocFile() {
        IpMark ipMark = getMark();
        List<IpDocFiles> ipDocFilesCollection = ipMark.getFile().getIpDocFilesCollection();
        int oldSize = ipDocFilesCollection.size();
        IpDocFiles docFile = ipDocFilesCollection.remove(ipDocFilesCollection.size() - 1);
        Assert.assertNotNull(docFile);
        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals(oldSize - 1, updated.getFile().getIpDocFilesCollection().size());
    }

    @Test
    @Transactional
    public void createNewDocFile() {
        IpMark ipMark = getMark();
        IpFilePK ipFilePK = ipMark.getPk();
        IpDocFilesPK ipDocFilesPK = new IpDocFilesPK();
        ipDocFilesPK.setFileNbr(ipFilePK.getFileNbr());
        ipDocFilesPK.setFileSeq(ipFilePK.getFileSeq());
        ipDocFilesPK.setFileTyp(ipFilePK.getFileTyp());
        ipDocFilesPK.setFileSer(ipFilePK.getFileSer());
        ipDocFilesPK.setDocOri("BG");
        ipDocFilesPK.setDocLog("E");
        ipDocFilesPK.setDocSer(1998);
        ipDocFilesPK.setDocNbr(968873);

        IpDocFiles ipDocFiles = new IpDocFiles();
        ipDocFiles.setPk(ipDocFilesPK);
        ipDocFiles.setRowVersion(1);

        int oldSize = ipMark.getFile().getIpDocFilesCollection().size();
        ipMark.getFile().getIpDocFilesCollection().add(ipDocFiles);

        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals(oldSize + 1, updated.getFile().getIpDocFilesCollection().size());
    }
}
