package bg.duosoft.ipas.test.repository.sql;

import bg.duosoft.ipas.persistence.repository.nonentity.SqlRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 14:31
 */
public class SqlRepositoryTest extends TestBase {
    @Autowired
    private SqlRepository sqlRepository;
    @Test
    @Transactional
    public void testReadAllApplicationTypesArray() {
        List<Object[]> res = sqlRepository.selectRowsAsObjectArray("SELECT * from CF_APPLICATION_TYPE", null);
        assertNotNull(res);
    }
    @Test
    @Transactional
    public void testReadApplicationTypeAsArray() {
        List<Object[]> res = sqlRepository.selectRowsAsObjectArray("SELECT * from CF_APPLICATION_TYPE where APPL_TYP = :appl_typ", Map.of("appl_typ", "НМ"));
        assertEquals(1, res.size());
        assertEquals(BigDecimal.ONE, res.get(0)[0]);
        assertNotNull(res);
//        System.out.println(res.size());
    }

    @Test
    @Transactional
    public void testReadApplicationTypesMap() {
        List<Map<String, Object>> res = sqlRepository.selectRowsAsMap("SELECT * from CF_APPLICATION_TYPE where APPL_TYP = :appl_typ", Map.of("appl_typ", "НМ"));
        assertEquals(1, res.size());
        assertEquals("НМ", res.get(0).get("APPL_TYP"));
//        System.out.println(res.size());
    }
    @Test
    @Transactional
    public void testExecuteProcedure() {
        Map<String, Object> keyMap = Map.of("fileSeq", "BG", "fileTyp", "P", "fileSer", 1995, "fileNbr", 99999);
        String selectFileNbr = "SELECT FILE_NBR from IP_PATENT where FILE_SEQ = :fileSeq and FILE_TYP = :fileTyp and FILE_SER = :fileSer and FILE_NBR = :fileNbr";
        List<Map<String, Object>> rows = sqlRepository.selectRowsAsMap(selectFileNbr, keyMap);
        assertEquals(1, rows.size());
        int res = sqlRepository.execute("exec.deletePatentDetails @fileSeq=:fileSeq, @fileTyp=:fileTyp, @fileSer=:fileSer, @fileNbr=:fileNbr", keyMap);
        rows = sqlRepository.selectRowsAsMap(selectFileNbr, keyMap);
        assertEquals(0, rows.size());
    }
    @Test
    @Transactional
    public void testExecuteUpdateSql() {
        int res = sqlRepository.execute("UPDATE IP_FILE SET TITLE = :title where FILE_SEQ = :fileSeq AND FILE_TYP = :fileTyp AND FILE_SER = :fileSer and  FILE_NBR = :fileNbr ", Map.of("title", "test", "fileSeq", "BG", "fileTyp", "N", "fileSer", 2013, "fileNbr", 130109));
        assertEquals(1, res);
    }
}
