package bg.duosoft.ipas.test.mapper;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.userdoc.UserdocMapper;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocTypes;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRepository;
import bg.duosoft.ipas.util.DefaultValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.Assert.*;

public class UserdocMapperTest extends MapperTestBase {
    @Autowired
    private IpUserdocRepository ipUserdocRepository;

    @Autowired
    private UserdocMapper userdocMapper;

    private IpDocPK ipDocPKMain;
    private IpDocPK ipDocPKMainAnother;

    private IpDocPK ipDocPKRepresentative;
    private IpDocPK ipDocPKRepresentativeAnother;

    private IpDocPK ipDocPKCaptureUser;

    private IpDocPK ipDocPKCourtData;
    private IpDocPK ipDocPKCourtDataAnother;

    private IpUserdoc originalIpUserdoc;

    private IpUserdoc originalIpUserdocAnother;

    private CUserdoc cUserdoc;

    private IpUserdoc revertedIpUserdoc;

    @Before
    public void init() {
        ipDocPKMain = new IpDocPK("BG", "E", 2013, 983063);
        ipDocPKMainAnother = new IpDocPK("BG", "E", 2015, 1118911);

        ipDocPKCaptureUser = new IpDocPK("BG", "E", 2017, 1172712);

        ipDocPKCourtData = new IpDocPK("BG", "E", 2007, 1314243);
        ipDocPKCourtDataAnother = new IpDocPK("BG", "E", 2009, 1314240);

        ipDocPKRepresentative = new IpDocPK("BG", "E", 2013, 983130);
        ipDocPKRepresentativeAnother = new IpDocPK("BG", "E", 2012, 982689);
    }

    private void initCoreAndOriginalEntityObject(IpDocPK pk) {
        originalIpUserdoc = ipUserdocRepository.findById(pk).orElse(null);
        cUserdoc = userdocMapper.toCore(originalIpUserdoc,false);
        revertedIpUserdoc = userdocMapper.toEntity(cUserdoc,false);
    }

    private void initAnotherOriginalEntityObject(IpDocPK pk) {
        originalIpUserdocAnother = ipUserdocRepository.findById(pk).orElse(null);
    }


    @Test
    @Transactional
    public void transformIpUserdocToCUserdocMainData() {
        initCoreAndOriginalEntityObject(ipDocPKMain);

        assertNotNull(originalIpUserdoc.getRowVersion());
        assertEquals(cUserdoc.getRowVersion(), originalIpUserdoc.getRowVersion());

        assertNotNull(originalIpUserdoc.getCaptureDate());
        assertEquals(cUserdoc.getCaptureDate(), originalIpUserdoc.getCaptureDate());

        assertNotNull(originalIpUserdoc.getNotes());
        assertEquals(cUserdoc.getNotes(), originalIpUserdoc.getNotes());
    }

    @Test
    @Transactional
    public void transformIpUserdocCaptureUserToCUserdocCaptureUser() {
        initCoreAndOriginalEntityObject(ipDocPKCaptureUser);

        assertNotNull(originalIpUserdoc.getCaptureUser());
        assertNotNull(cUserdoc.getCaptureUser());
        assertEquals(cUserdoc.getCaptureUser().getUserId(), originalIpUserdoc.getCaptureUser().getUserId());
        assertEquals(cUserdoc.getCaptureUser().getUserId(), originalIpUserdoc.getCaptureUser().getUserId());
        assertEquals(cUserdoc.getCaptureUser().getUserName(), originalIpUserdoc.getCaptureUser().getUserName());
        assertEquals(cUserdoc.getCaptureUser().getIndInactive(), MapperHelper.getTextAsBoolean(originalIpUserdoc.getCaptureUser().getIndInactive()));
    }

    @Test
    @Transactional
    public void transformIpUserdocDocumentIdToCUserdocDocumentId() {
        initCoreAndOriginalEntityObject(ipDocPKMain);

        assertNotNull(originalIpUserdoc.getIpDoc());
        assertNotNull(originalIpUserdoc.getIpDoc().getPk());
        assertNotNull(cUserdoc.getDocumentId());
        assertEquals(cUserdoc.getDocumentId().getDocOrigin(), originalIpUserdoc.getIpDoc().getPk().getDocOri());
        assertEquals(cUserdoc.getDocumentId().getDocLog(), originalIpUserdoc.getIpDoc().getPk().getDocLog());
        assertEquals(cUserdoc.getDocumentId().getDocSeries(), originalIpUserdoc.getIpDoc().getPk().getDocSer());
        assertEquals(cUserdoc.getDocumentId().getDocNbr(), originalIpUserdoc.getIpDoc().getPk().getDocNbr());
    }

    @Test
    @Transactional
    public void transformIpUserdocApplicantToCUserdocApplicant() {
        initCoreAndOriginalEntityObject(ipDocPKMain);

        assertNotNull(originalIpUserdoc.getApplicantNotes());
        assertNotNull(cUserdoc.getApplicantNotes());
        assertEquals(cUserdoc.getApplicantNotes(), originalIpUserdoc.getApplicantNotes());

        assertNotNull(originalIpUserdoc.getServicePerson());
        assertNotNull(cUserdoc.getServicePerson());
        compareAddressAndPersonDataCore(cUserdoc.getServicePerson(), originalIpUserdoc.getServicePerson(), originalIpUserdoc.getServicePerson().getIpPerson());
    }

    @Test
    @Transactional
    public void transformIpUserdocCourtDataToCUserdocCourtData() {
        initCoreAndOriginalEntityObject(ipDocPKCourtData);

        testCourtData(cUserdoc, originalIpUserdoc);
    }

    @Test
    @Transactional
    public void testChangeCourtData() {
        initCoreAndOriginalEntityObject(ipDocPKCourtData);
        initAnotherOriginalEntityObject(ipDocPKCourtDataAnother);
        userdocMapper.fillUserdocFields(cUserdoc, originalIpUserdocAnother);

        testCourtData(cUserdoc, originalIpUserdocAnother);
    }

    private void testCourtData(CUserdoc coreUserdoc, IpUserdoc ipUserdoc) {
        assertNotNull(ipUserdoc.getCourtDocNbr());
        assertNotNull(coreUserdoc.getCourtDocNbr());
        assertEquals(coreUserdoc.getCourtDocNbr(), ipUserdoc.getCourtDocNbr());

        assertNotNull(ipUserdoc.getCourtDocDate());
        assertNotNull(coreUserdoc.getCourtDocDate());
        assertEquals(coreUserdoc.getCourtDocDate(), ipUserdoc.getCourtDocDate());

        assertNotNull(ipUserdoc.getDecreeDate());
        assertNotNull(coreUserdoc.getDecreeDate());
        assertEquals(coreUserdoc.getDecreeDate(), ipUserdoc.getDecreeDate());
    }

    @Test
    @Transactional
    public void transformIpUserdocPersonDataToCUserdocPersonData() {
        initCoreAndOriginalEntityObject(ipDocPKMain);

        testUserdocPersonData(cUserdoc, originalIpUserdoc);
    }

    @Test
    @Transactional
    public void testChangePersonData() {
        initCoreAndOriginalEntityObject(ipDocPKMain);
        initAnotherOriginalEntityObject(ipDocPKMainAnother);
        userdocMapper.fillUserdocFields(cUserdoc, originalIpUserdocAnother);

        testUserdocPersonData(cUserdoc, originalIpUserdocAnother);
    }

    private void testUserdocPersonData(CUserdoc coreUserdoc, IpUserdoc ipUserdoc) {
        assertFalse(CollectionUtils.isEmpty(ipUserdoc.getPersons()));
        assertNotNull(coreUserdoc.getUserdocPersonData());
        assertFalse(CollectionUtils.isEmpty(coreUserdoc.getUserdocPersonData().getPersonList()));
        assertEquals(ipUserdoc.getPersons().size(), coreUserdoc.getUserdocPersonData().getPersonList().size());

        List<IpUserdocPerson> originalIpUserdocPersons = ipUserdoc.getPersons();
        List<CUserdocPerson> cUserdocPersonList = coreUserdoc.getUserdocPersonData().getPersonList();
        for (int i = 0; i < originalIpUserdocPersons.size(); i++) {
            CUserdocPerson cUserdocPerson = cUserdocPersonList.get(i);
            IpUserdocPerson originalIpUserdocPerson = originalIpUserdocPersons.get(i);


            assertEquals(cUserdocPerson.getRole(), originalIpUserdocPerson.getPk().getRole());
            assertEquals(cUserdocPerson.getNotes(), originalIpUserdocPerson.getNotes());

            assertEquals(cUserdocPerson.getPerson().getPersonNbr(), originalIpUserdocPerson.getPk().getPersonNbr());
            assertEquals(cUserdocPerson.getPerson().getAddressNbr(), originalIpUserdocPerson.getPk().getAddrNbr());

            compareAddressAndPersonDataCore(cUserdocPerson.getPerson(), originalIpUserdocPerson.getIpPersonAddresses(), originalIpUserdocPerson.getIpPersonAddresses().getIpPerson());
        }
    }

    @Test
    @Transactional
    public void transformIpUserdoUserdocTypeToCUserdocUserdocType() {
        initCoreAndOriginalEntityObject(ipDocPKMain);
        assertNotNull(originalIpUserdoc.getIpUserdocTypes());
        assertNotNull(cUserdoc.getUserdocType());

        CUserdocType cUserdocType = cUserdoc.getUserdocType();
        IpUserdocTypes originalUserdocType = originalIpUserdoc.getIpUserdocTypes().get(DefaultValue.FIRST_RESULT);
        assertEquals(cUserdocType.getUserdocType(), originalUserdocType.getPk().getUserdocTyp());
        assertEquals(cUserdocType.getUserdocName(), originalUserdocType.getUserdocType().getUserdocName());

        if (!CollectionUtils.isEmpty(cUserdocType.getRoles()) || !CollectionUtils.isEmpty(originalUserdocType.getUserdocType().getPersonRoles()))
            assertEquals(cUserdocType.getRoles().size(), originalUserdocType.getUserdocType().getPersonRoles().size());
    }

}
