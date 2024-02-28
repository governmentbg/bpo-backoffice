package bg.duosoft.ipas.rest.client.test.search;

import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.rest.client.SearchRestClient;
import bg.duosoft.ipas.rest.client.test.TestBase;
import bg.duosoft.ipas.rest.custommodel.search.RSearchResponse;
import bg.duosoft.ipas.rest.model.search.RSearchParam;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

/**
 * User: Georgi
 * Date: 17.9.2020 г.
 * Time: 23:01
 */
public class SearchClientTest extends TestBase {
    @Autowired
    private SearchRestClient searchRestClient;
    @Test
    @Ignore
    public void searchIpObjects() {
        RSearchParam rq = new RSearchParam();
        rq.setFileTypes(FileType.getMarkFileTypes().stream().map(r -> r.code()).collect(Collectors.toList()));
        rq.setFromFileNbr("130000");
        rq.setToFileNbr("140000");
        RSearchResponse rs = searchRestClient.searchIpObjects(rq);
        System.out.println(rs.getTotalElements());
    }
}
