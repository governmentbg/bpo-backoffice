package bg.duosoft.ipas.rest.client.test.sql;

import bg.duosoft.ipas.rest.client.IpasRestServiceException;
import bg.duosoft.ipas.rest.client.SqlRestClient;
import bg.duosoft.ipas.rest.client.test.TestBase;
import bg.duosoft.ipas.rest.model.file.RFileId;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: Georgi
 * Date: 11.9.2020 г.
 * Time: 16:00
 */
public class SqlClientTest extends TestBase {
    @Autowired
    private SqlRestClient sqlRestClient;
    @Test
    @Ignore
    public void testExecuteSql() {
        List<RFileId> res = sqlRestClient.selectRows("SELECT top 1 FILE_SEQ, FILE_TYP AS FILE_TYPE, FILE_SER AS FILE_SERIES, FILE_NBR from IP_MARK", null, RFileId.class);
        assertEquals(1, res.size());
        System.out.println(res);
    }


    @Test
    @Ignore
    public void testExecuteSqlReturnSimpleDateType() {
        List<Date> res = sqlRestClient.selectRows("SELECT top 1 FILING_DATE from IP_MARK order by FILE_NBR desc", null, Date.class);

        assertEquals(1, res.size());
        System.out.println(res.get(0));
    }
    @Test
    @Ignore
    public void testExecuteSqlReturnSimpleIntegerType() {
        List<Integer> res = sqlRestClient.selectRows("SELECT top 1 FILE_NBR from IP_MARK order by FILE_NBR desc", null, Integer.class);

        assertEquals(1, res.size());
        System.out.println(res.get(0));
    }
    @Test
    @Ignore
    public void testInternalServerError() {
        try {
            List<RFileId> res = sqlRestClient.selectRows("", null, RFileId.class);
            assertTrue(1 == 2);
        } catch (IpasRestServiceException e) {
            e.printStackTrace();
        }
    }
}
