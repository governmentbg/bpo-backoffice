package bg.duosoft.ipas.test.service.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.mark.MarkSignDataAttachmentUtils;
import bg.duosoft.ipas.util.mark.MarkSignTypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 15.1.2019 г.
 * Time: 13:33
 */
public class MarkServiceTest extends TestBase {
    @Autowired
    private MarkService markService;

    @Test
    @Transactional
    public void testReadMarkWithoutLogo() {
        CMark mark = markService.findMark("BG", "N", 2013, 130109, false);
        List<CMarkAttachment> attachments = mark.getSignData().getAttachments();
        assertFalse(CollectionUtils.isEmpty(attachments));
        CMarkAttachment logo = MarkSignDataAttachmentUtils.selectFirstImageAttachment(mark.getSignData());
        assertNotNull(logo);
        assertEquals(logo.getData(), null);
        assertEquals(mark.getIndReadAttachments(), false);
    }

    @Test
    @Transactional
    public void testReadMarkWithLogo() {
        CMark mark = markService.findMark("BG", "N", 2013, 130109, true);
        List<CMarkAttachment> attachments = mark.getSignData().getAttachments();
        assertFalse(CollectionUtils.isEmpty(attachments));
        CMarkAttachment logo = MarkSignDataAttachmentUtils.selectFirstImageAttachment(mark.getSignData());
        assertNotNull(logo);
        assertNotEquals(logo.getData(), null);
        assertEquals(mark.getIndReadAttachments(), true);
    }


    @Test
    @Transactional
    public void removeAllMarkListData() {
        CMark mark = markService.findMark("BG", "N", 1960, 205, true);
        mark.getFile().getRepresentationData().setRepresentativeList(null);
//        mark.getFile().getOwnershipData().setOwnerList(null);//the mark should contain at least one owner, so the owners cannot be removed!!!!

        List<CMarkAttachment> attachments = mark.getSignData().getAttachments();
        assertFalse(CollectionUtils.isEmpty(attachments));
        CMarkAttachment logo = MarkSignDataAttachmentUtils.selectFirstImageAttachment(mark.getSignData());
        assertNotNull(logo);

        logo.setViennaClassList(null);
//        mark.getProtectionData().setNiceClassList(null);//the mark should contain at least one nice class!
        mark.getFile().getPriorityData().setParisPriorityList(null);
        markService.updateMark(mark);
    }
}
