package bg.duosoft.ipas.test.service.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.userdoc.CUserdocHierarchyNode;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

/**
 * User: ggeorgiev
 * Date: 28.01.2021
 * Time: 14:03
 */
public class UserdocServiceTest extends TestBase {
    @Autowired
    private UserdocService userdocService;
    @Test
    public void testGetFileHierarchy() {
        List<CUserdocHierarchyNode> hierarchy = userdocService.getFileUserdocHierarchy(new CFileId("BG", "N", 2013, 130109), false);
        printNodes(hierarchy, 0);
    }
    @Test
    public void testGetUserdocHierarchy() {
        List<CUserdocHierarchyNode> hierarchy = userdocService.getUserdocUserdocHierarchy(new CDocumentId("BG", "E", 2021, 428), false);
        printNodes(hierarchy, 0);
    }
    private void printNodes(List<CUserdocHierarchyNode> hierarchyNodes, int level) {
        hierarchyNodes.forEach(hierarchyNode -> {
            IntStream.rangeClosed(0, level).forEach(i -> System.out.print("\t"));
            System.out.println(hierarchyNode.getExternalSystemId() + " ...." + hierarchyNode.getProcessId() + "...." + hierarchyNode.getDocumentId());
            if (hierarchyNode.getChildren() != null) {
                printNodes(hierarchyNode.getChildren(), level + 1);
            }
        });

    }
}
