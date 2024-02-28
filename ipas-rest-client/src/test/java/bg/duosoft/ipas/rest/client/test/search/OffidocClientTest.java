package bg.duosoft.ipas.rest.client.test.search;

import bg.duosoft.ipas.rest.client.OffidocRestClient;
import bg.duosoft.ipas.rest.client.test.TestBase;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocAbdocsDataResponse;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocUpdateNotificationReadDateRequest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class OffidocClientTest extends TestBase {

    @Autowired
    private OffidocRestClient offidocRestClient;

    @Test
    @Ignore
    public void selectOffidoc() {
        ROffidocAbdocsDataResponse response = offidocRestClient.selectOffidocByHashKey("c221b45bc5cadb998d7d23c46c4a3e0ec0a1b28efb42226ae31d546c5890c9d7");
        offidocRestClient.updateEmailNotificationReadDate(ROffidocUpdateNotificationReadDateRequest.builder().date(new Date()).abdocsDocumentId(602000).build());
        offidocRestClient.updatePortalNotificationReadDate(ROffidocUpdateNotificationReadDateRequest.builder().date(new Date()).abdocsDocumentId(602000).build());
    }
}
