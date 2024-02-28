package bg.duosoft.ipas.test.repository.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogo;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClassesPK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class IpMarkLogoTest extends TestBase {

    @Autowired
    private IpMarkRepository ipMarkRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private IpMark getMarkWithLogo() {
        IpFilePK ipFilePK = new IpFilePK("BG", "D", 2003, 23473);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);
        return ipMark;
    }
    private IpMark getMarkWithLogo2() {
        IpFilePK ipFilePK = new IpFilePK("BG", "D", 2003, 23474);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);
        return ipMark;
    }

    @Test
    @Transactional
    public void testReadMarkWithLogo() {
        IpMark ipMark = getMarkWithLogo();
        IpMark ipMark2 = getMarkWithLogo2();
        assertNotNull(ipMark.getLogo());
        Assert.assertNotNull(ipMark.getLogo().getLogoData());
        assertNotNull(ipMark2.getLogo());
        Assert.assertNotNull(ipMark2.getLogo().getLogoData());

//        createNewLogo(ipFilePK, ipMark);
//        deleteLogo(ipMark);
//        updateLogo(ipMark);
    }
    @Test
    @Transactional
    public void testDeleteLogo() {
        IpMark ipMark = getMarkWithLogo();
        ipMark.setLogo(null);
        IpMark saved = ipMarkRepository.save(ipMark);
        entityManager.flush();
        assertNull(saved.getLogo());
    }

    @Test
    @Transactional
    public void testReplaceLogo() {
        IpMark ipMark = getMarkWithLogo();
        IpMark ipMark2 = getMarkWithLogo2();
        IpLogo logo = new IpLogo();
        logo.setPk(ipMark.getPk());
        logo.setLogoData(ipMark2.getLogo().getLogoData());
        logo.setColourDescription("alabala");
        logo.setColourdDescrLang2("alabala in english");
        logo.setIndBase64("N");
        logo.setImageFormatWcode(ipMark2.getLogo().getImageFormatWcode());
        logo.setIpLogoViennaClassesCollection(new ArrayList<>());
        logo.setRowVersion(1);
        ipMark.setLogo(logo);

        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);

        IpLogo updatedLogo = updated.getLogo();
        assertArrayEquals(logo.getLogoData(), updatedLogo.getLogoData());
        _assertEquals(logo, updatedLogo, IpLogo::getColourDescription);
        _assertEquals(logo, updatedLogo, IpLogo::getColourdDescrLang2);
        _assertEquals(logo, updatedLogo, IpLogo::getImageFormatWcode);
        _assertEquals(logo, updatedLogo, IpLogo::getIndBase64);
        _assertEquals(logo, updatedLogo, IpLogo::getImageFormatWcode);

    }

    @Test
    @Transactional
    public void testRemoveAllViennaClasses() {
        IpMark ipMark = getMarkWithLogo();
        ipMark.getLogo().getIpLogoViennaClassesCollection().clear();
        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals(0, updated.getLogo().getIpLogoViennaClassesCollection().size());
    }


    @Test
    @Transactional
    public void testAddViennaClass() {
        IpMark ipMark = getMarkWithLogo();
        IpLogoViennaClasses vc = new IpLogoViennaClasses();
        vc.setPk(new IpLogoViennaClassesPK());
        vc.getPk().setFileSeq(ipMark.getPk().getFileSeq());
        vc.getPk().setFileTyp(ipMark.getPk().getFileTyp());
        vc.getPk().setFileSer(ipMark.getPk().getFileSer());
        vc.getPk().setFileNbr(ipMark.getPk().getFileNbr());
        vc.getPk().setViennaClassCode(27l);
        vc.getPk().setViennaGroupCode(5l);
        vc.getPk().setViennaElemCode(4l);
        vc.setRowVersion(1);
        vc.setViennaEditionCode("0");
        ipMark.getLogo().getIpLogoViennaClassesCollection().add(vc);
        int newSize = ipMark.getLogo().getIpLogoViennaClassesCollection().size();
        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);

        assertEquals(newSize, updated.getLogo().getIpLogoViennaClassesCollection().size());
    }

}
