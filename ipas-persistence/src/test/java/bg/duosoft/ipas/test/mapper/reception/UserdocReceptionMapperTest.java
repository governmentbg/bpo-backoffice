package bg.duosoft.ipas.test.mapper.reception;

import bg.duosoft.ipas.core.mapper.reception.userdoc.UserdocReceptionDocMapper;
import bg.duosoft.ipas.core.mapper.reception.userdoc.UserdocReceptionUserdocMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * User: Georgi
 * Date: 8.6.2020 г.
 * Time: 16:33
 */
public class UserdocReceptionMapperTest extends ReceptionMapperTestBase {
    @Autowired
    private UserdocReceptionDocMapper userdocReceptionDocMapper;
    @Autowired
    private UserdocReceptionUserdocMapper userdocReceptionUserdocMapper;

    public static CReception createUserdocReception(boolean relatedToFile) {
        CReception r = createReceptionBase();
        r.setUserdoc(new CReceptionUserdoc());
        r.getUserdoc().setUserdocType("ОК");
        if (relatedToFile) {
            r.getUserdoc().setFileId(createFileId());
        } else {
            r.getUserdoc().setDocumentId(createDocumentId());
        }

        return r;
    }

    private static CFileId createFileId() {
        return new CFileId("BG", "N", 2013, 130130);
    }

    private static CDocumentId createDocumentId() {
        return new CDocumentId("BG", "E", 2003, 815872);
    }

    @Test
    public void testDocMapping() {
        CReception r = createUserdocReception(true);
        IpDoc entity = userdocReceptionDocMapper.toEntity(r);
        compareDocBase(r, entity);
        assertEquals(r.getNotes(), entity.getNotes());
        assertEquals("N", entity.getIndNotAllFilesCapturedYet());
        assertEquals("PE", entity.getReceptionWcode());

    }

    @Test
    public void testUserdocMapping() {
        CReception r = createUserdocReception(true);
        IpUserdoc entity = userdocReceptionUserdocMapper.toEntity(r);
        assertEquals(r.getNotes(), entity.getIpDoc().getNotes());
        assertEquals((Object) 1, entity.getRowVersion());
        assertEquals(SecurityUtils.getLoggedUserId(), entity.getCaptureUser().getUserId());

    }

}
