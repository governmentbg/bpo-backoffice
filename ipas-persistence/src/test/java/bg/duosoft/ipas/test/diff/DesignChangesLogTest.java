package bg.duosoft.ipas.test.diff;

import bg.duosoft.ipas.core.model.design.CSingleDesignExtended;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.util.CDrawingExt;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

/**
 * User: Georgi
 * Date: 10.9.2020 г.
 * Time: 11:16
 */
@Transactional
public class DesignChangesLogTest extends TestBase {
    @Autowired
    private DesignService designService;
    @Autowired
    private PatentService patentService;
    @Test
    @Ignore
    public void testDesginChanges() {
        CPatent master = patentService.findPatent("BG", "Д", 2015, 10999, true);
        List<CPatent> children = designService.getAllSingleDesignsForIndustrialDesign(master, true);

        List<CPatent> changed = designService.getAllSingleDesignsForIndustrialDesign(master, true);
        changed.get(0).getFile().getFilingData().setApplicationSubtype("ГС");
//        changed.remove(2);
        DiffGenerator diff = DiffGenerator.create(children, changed);
        System.out.println(diff.getResult());
//        DiffGenerator diff = DiffGenerator.create(null, changed.get(0));
//        System.out.println(diff.getResult());
    }

    @Test
    public void compareDrawingAndExtDrawing() {
        CDrawing d = new CDrawing(1l, "alabala", new byte[0], new CSingleDesignExtended());
        CDrawingExt d2 = CDrawingExt.createCDrawingExtObject(d);
        d2.setDrawingData(new byte[0]);
        assertTrue(d.equals(d2));
        assertTrue(d2.equals(d));
    }


}
