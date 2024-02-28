package bg.duosoft.ipas.test.repository.file;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IpFileRelationshipTest extends TestBase {
    @Autowired
    private IpFileRepository ipFileRepository;

    private IpFile getIpFile() {
        IpFilePK ipFilePK = new IpFilePK("BG", "Д", 2000, 5394);
        IpFile file = ipFileRepository.findById(ipFilePK).orElse(null);
        return file;
    }
    private IpFile getIpFile2() {
        IpFilePK ipFilePK = new IpFilePK("BG", "P", 1973, 24326);
        IpFile file = ipFileRepository.findById(ipFilePK).orElse(null);
        return file;
    }

    @Test
    @Transactional
    public void removeFileRelation1() {
        checkRemoveRelation(getIpFile(), IpFile::getIpFileRelationships1);
    }


    @Test
    @Transactional
    public void removeFileRelation2() {
        checkRemoveRelation(getIpFile2(), IpFile::getIpFileRelationships2);
    }

    void checkRemoveRelation(IpFile file, Function<IpFile, List<IpFileRelationship>> function) {
        int originalSize = function.apply(file).size();
        IpFileRelationship removed = function.apply(file).remove(function.apply(file).size() - 1);
        IpFile updated = ipFileRepository.saveAndFlush(file);
        assertTrue(originalSize > 0);
        assertEquals(originalSize - 1, function.apply(updated).size());
        assertEquals(0, function.apply(updated).stream().filter(r -> Objects.equals(r.getPk(), removed.getPk())).count());
    }

    @Test
    @Transactional
    public void createFileRelation1() {
        IpFile file = getIpFile();
        IpFileRelationshipPK pk = new IpFileRelationshipPK(file.getPk().getFileSeq(), file.getPk().getFileTyp(), file.getPk().getFileSer(), file.getPk().getFileNbr(), "BG", "D", 2014, 91467, "РНП");
        checkCreateFileRelation(file, pk, IpFile::getIpFileRelationships1);
    }
    @Test
    @Transactional
    public void createFileRelation2() {
        IpFile file = getIpFile2();
        IpFileRelationshipPK pk = new IpFileRelationshipPK("BG", "D", 2014, 91467, file.getPk().getFileSeq(), file.getPk().getFileTyp(), file.getPk().getFileSer(), file.getPk().getFileNbr(),  "РНП");
        checkCreateFileRelation(file, pk, IpFile::getIpFileRelationships2);
    }
    void checkCreateFileRelation(IpFile file, IpFileRelationshipPK pk, Function<IpFile, List<IpFileRelationship>> function) {
        int originalSize = function.apply(file).size();
        IpFileRelationship ipFileRelationship = new IpFileRelationship();
        ipFileRelationship.setRowVersion(1);
        ipFileRelationship.setPk(pk);
        function.apply(file).add(ipFileRelationship);
        IpFile updated = ipFileRepository.saveAndFlush(file);
        assertEquals(originalSize + 1, function.apply(updated).size());
        assertEquals(1, function.apply(updated).stream().filter(r -> Objects.equals(r.getPk(), pk)).count());
    }
}
