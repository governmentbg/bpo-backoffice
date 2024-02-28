package bg.duosoft.ipas.test;

import bg.duosoft.ipas.core.model.CPublicationInfo;
import bg.duosoft.ipas.persistence.model.entity.person.IpPerson;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.core.mapper.structure.UserMapper;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkOwners;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkOwnersPK;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFilesPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocFilesRepository;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonRepository;
import bg.duosoft.ipas.persistence.repository.entity.user.IpUserRepository;
import bg.duosoft.ipas.integration.payments.service.PaymentsIntegrationService;
import bg.duosoft.ipas.core.service.PublicationService;
import bg.duosoft.ipas.core.service.impl.structure.UserServiceImpl;
import bg.duosoft.ipas.core.service.impl.structure.OfficeDepartmentServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.List;


public class TestApplicationSearchService extends TestBase {

    @Autowired
    private IpDocFilesRepository ipDocFilesRepository;

    @Autowired
    private IpFileRepository ipFileRepository;
    @Autowired
    private IpMarkRepository ipMarkRepository;

    @Autowired
    private IpUserRepository ipUserRepository;

    @Autowired
    private UserServiceImpl ipUserServiceImpl;

    @Autowired
    private OfficeDepartmentServiceImpl officeDepartmentService;

    @Autowired
    private PaymentsIntegrationService paymentsIntegrationService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private IpPersonRepository  ipPersonRepository;

    @Test
    public void testIpDocFiles() {
        IpDocFilesPK ipDocFilesPK = new IpDocFilesPK("BG", "E", 1998, 968873, "BG", "N", 1998, 42159);
        IpDocFiles ipDocFiles = ipDocFilesRepository.findById(ipDocFilesPK).orElse(null);
        Assert.assertNotNull(ipDocFiles);
    }

    @Test
    @Transactional
    public void testIpFile() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 1998, 42159);
        IpFile ipFile = ipFileRepository.findById(ipFilePK).orElse(null);
        Assert.assertNotNull(ipFile);
    }

    @Test
    @Transactional
    public void testIpMark() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 2013, 130109);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);

        Assert.assertNotNull(ipMark);
    }


    @Test
    @Transactional
    public void testIpMarkOwners() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 2013, 130109);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);

        IpMarkOwnersPK ipMarkOwnersPK = new IpMarkOwnersPK("BG", "N", 2013, 130109, 398224, 1);
        IpMarkOwners newOwner = new IpMarkOwners();
        newOwner.setPk(ipMarkOwnersPK);
        newOwner.setRowVersion(1);
//        ipMark.getIpMarkOwners().add(newOwner);
//        ipMark.getIpMarkOwners().remove(1);
//
//        IpMark save = ipMarkRepository.save(ipMark);
        Assert.assertNotNull(ipMark);
    }

    @Test
    public void updateUser(){
        IpUser ipUser = ipUserRepository.findById(4140).orElse(null);
        ipUser.setOfficeSectionCode("012");
       // ipUserService.updateUser(ipUser);
//        ipUserRepository.save(ipUser);

    }

    private Validator validator;

//    @Before
//    public void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validation = factory.getValidator();
//    }
//
//    @Test
//    public void insertUser(){
//        IpUser ipUser = new IpUser();
//        ipUser.setUserName("sasdad");
//        ipUser.setInitials("dadad");
//        ipUser.setEmail("adsdas@dasd");
//        ipUser.setTelephone("123123");
//        ipUser.setLogin("log2322");
//        ipUser.setLoginPassword("2323");
//        ipUser.setOfficeDivisionCode("01");
//        ipUser.setIsAdmin("YES");
//        ipUser.setIsExaminer("YES");
//        ipUser.setIsExternal("YES");
//        Set<ConstraintViolation<IpUser>> violations = validation.validate(ipUser);
//       // ipUserService.saveNewUser(ipUser);
//    }

    @Test
    public void validationTest(){
        IpUser ipUser = ipUserRepository.findByLogin("asdasdasdasd");
        System.out.println();
    }

    @Autowired
    UserMapper userMapper;


    @Test
    public void paymentsTest(){
        List<CPublicationInfo> publications = publicationService.selectPublications("BG/N/2013/00130109");
        System.out.println(1);
    }

    @Test
    @Transactional
    public void personRepository(){
        Pageable of = PageRequest.of(0, 6);
        List<IpPerson> ipPeople = ipPersonRepository.selectPersonsByNameLike("%Иван%",of);
        for (IpPerson ipPerson : ipPeople) {
            List<IpPersonAddresses> addresses = ipPerson.getAddresses();
            System.out.println(1);
        }


        List<IpPerson> ipPeople1 = ipPersonRepository.selectRepresentativesByNameLike("%Пето%", of);
        System.out.println(1);
        System.out.println(1);
    }


}
