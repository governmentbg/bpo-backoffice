package bg.duosoft.ipas.test.repository.userdoc;

import bg.duosoft.ipas.persistence.model.nonentity.UserdocHierarchyChildNode;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: ggeorgiev
 * Date: 22.01.2021
 * Time: 15:14
 */
public class UserdocRepositoryTest extends TestBase {
    @Autowired
    private IpUserdocRepository ipUserdocRepository;
    @Test
    public void testGetFileUserdocHierarchy() {
        List<UserdocHierarchyChildNode> res = ipUserdocRepository.getFileUserdocHierarchy("BG", "N", 2013, 130109);
        res.forEach(System.out::println);
        System.out.println(res.size());
    }

    @Test
    public void testGetUserdocUserdocHierarchy() {
        List<UserdocHierarchyChildNode> res = ipUserdocRepository.getUserdocUserdocHierarchy("BG", "E", 2021, 428);
        res.forEach(System.out::println);
        System.out.println(res.size());
    }
    @Test
    public void testIsMainUserdocRepository() {
        assertTrue(ipUserdocRepository.isMainEpoPatentRequestForValidation("BG", "E", 2014, 1196563));
        assertFalse(ipUserdocRepository.isMainEpoPatentRequestForValidation("BG", "E", 2017, 1178461));
    }
}
