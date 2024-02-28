package bg.duosoft.ipas.rest.client.test.userdoc;

import bg.duosoft.ipas.rest.client.UserdocRestClient;
import bg.duosoft.ipas.rest.client.test.TestBase;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.userdoc.RUserdocHierarchyNode;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

/**
 * User: ggeorgiev
 * Date: 29.01.2021
 * Time: 13:16
 */
public class UserdocClientTest extends TestBase {
    @Autowired
    private UserdocRestClient userdocRestClient;
    @Test
    @Ignore
    public void testFileUserdocHierarchy() {
        List<RUserdocHierarchyNode> res = userdocRestClient.getFileUserdocHierarchy(new RFileId("BG", "N", 2013, 130109), false);
        printNodes(res, 0);

    }

    @Test
    @Ignore
    public void testGetUserdocHierarchy() {
        List<RUserdocHierarchyNode> hierarchy = userdocRestClient.getUserdocUserdocHierarchy(new RDocumentId("BG", "E", 2021, 428), false);
        printNodes(hierarchy, 0);
    }

    private void printNodes(List<RUserdocHierarchyNode> hierarchyNodes, int level) {
        hierarchyNodes.forEach(hierarchyNode -> {
            IntStream.rangeClosed(0, level).forEach(i -> System.out.print("\t"));
            System.out.println(hierarchyNode.getExternalSystemId() + " ...." + hierarchyNode.getProcessId() + "...." + hierarchyNode.getDocumentId());
            if (hierarchyNode.getChildren() != null) {
                printNodes(hierarchyNode.getChildren(), level + 1);
            }
        });

    }
}
