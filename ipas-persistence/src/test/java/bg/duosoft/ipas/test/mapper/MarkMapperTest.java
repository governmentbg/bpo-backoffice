package bg.duosoft.ipas.test.mapper;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.mark.MarkMapper;
import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.core.model.mark.*;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipExtended;
import bg.duosoft.ipas.persistence.model.entity.mark.*;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 1.3.2019 г.
 * Time: 16:18
 */
//@Slf4j
public class MarkMapperTest extends MapperTestBase {
    @Autowired
    private MarkMapper markMapper;
    @Autowired
    private IpMarkRepository ipMarkRepository;

    private IpFilePK ipFilePK;
    //    private IpFilePK ipFileWithTransform;
    private IpMark originalIpMark;
    private CMark transformedCMark;
    private IpMark transformedIpMark;


    @Before
    public void init() {
        ipFilePK = new IpFilePK("BG", "N", 2007, 99998);
//        ipFileWithTransform = new IpFilePK("BG", "D", 2012, 33736);
    }

    protected void initOriginalAndTransformedIpMarks(IpFilePK pk) {
        originalIpMark = ipMarkRepository.getOne(pk);
        transformedCMark = markMapper.toCore(originalIpMark, true);
        transformedIpMark = markMapper.toEntity(transformedCMark);
    }


    //tests the CPublication object of the transformed CMark
    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckFilePublicationData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkTransformedPublicationData(originalIpMark, transformedCMark.getFile().getPublicationData());
    }


    //tests the registration data of the transformed CMark
    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckFileRegistrationData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkTransformedRegistrationData(originalIpMark, transformedCMark.getFile().getRegistrationData());
    }


    //tests the CMark's basic fields(rowVersion/markContainsLogo/markContainsSound), CFile's basic fields(rowVersion/notes) and CFilingData's fields
    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckFileMainDataAndFileFilingData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        assertEquals(originalIpMark.getFile().getIpDoc().getNotes(), transformedCMark.getFile().getNotes());//notes do not exists in patents, so it's checked only in transformIpMarkToCMarkCheckFileMainDataAndFileFilingData, but not in MapperTestBase.checkTransformedBasicFileAndFilingData
        assertEquals(originalIpMark.getRowVersion(), transformedCMark.getRowVersion());//didn't know in which test to check the rowVersion, so it's checked here!
        checkTransformedBasicFileAndFilingData(originalIpMark, transformedCMark.getFile());

    }

    //test's the transformed LimitationData's fields
    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckLimitationData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        CLimitationData limitationData = transformedCMark.getLimitationData();
        assertNotNull(limitationData);
        assertNotNull(originalIpMark.getIpMarkRegulation());
        assertEquals(originalIpMark.getDisclaimer(), limitationData.getDisclaimer());
        assertEquals(originalIpMark.getDisclaimerLang2(), limitationData.getDisclaimerInOtherLang());
        assertEquals(originalIpMark.getByConsentDescription(), limitationData.getByConsent());
        assertEquals(originalIpMark.getIpMarkRegulation().getRegulationsDescription(), limitationData.getRegulations());
    }

    //tests the transformed CRenewalData
    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckRenewalData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        assertNotNull(originalIpMark.getLastRenewalDate());
        assertNotNull(transformedCMark.getRenewalData());
        CRenewalData renewalData = transformedCMark.getRenewalData();
        assertEquals(originalIpMark.getLastRenewalDate(), renewalData.getLastRenewalDate());
    }

    //tests the transformed CMadridApplicationData
    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckMadridApplicationData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        CMadridApplicationData ma = transformedCMark.getMadridApplicationData();
        assertNotNull(ma);
        assertNotNull(ma.getBasicFileRef());
        assertNotNull(ma.getInternationalFileNumber());
        assertEquals(originalIpMark.getIntregn(), ma.getInternationalFileNumber());
        assertEquals(originalIpMark.getBasicFileRef(), ma.getBasicFileRef());
    }

    //tests the transformed signData's basic fields
    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckSignData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        CSignData signData = transformedCMark.getSignData();
        assertEquals(originalIpMark.getName().getMarkName(), signData.getMarkName());
        assertEquals(originalIpMark.getName().getMarkNameLang2(), signData.getMarkNameInOtherLang());
        assertEquals(originalIpMark.getTranslation(), signData.getMarkTranslation());
        assertEquals(originalIpMark.getTranslationLang2(), signData.getMarkTranslationInOtherLang());
        assertEquals(originalIpMark.getMarkTransliteration(), signData.getMarkTransliteration());
        assertEquals(originalIpMark.getMarkTransliterationLang2(), signData.getMarkTransliterationInOtherLang());
        assertEquals(originalIpMark.getMarkTransliterationLang2(), signData.getMarkTransliterationInOtherLang());
        assertEquals(originalIpMark.getSignWcode(), signData.getSignType().code());
        assertEquals(originalIpMark.getMarkSeriesDescription(), signData.getSeriesDescription());

    }

    //tests the transformed CSignData.CLogo's field basic fields
    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckSignDataLogoData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        CSignData signData = transformedCMark.getSignData();
        assertNotNull(signData);
        assertEquals(signData.getSignType(), MarkSignType.COMBINED);
        CMarkAttachment logo = signData.getAttachments().get(0);
        IpLogo ipLogo = originalIpMark.getLogo();
        assertNotNull(logo);
        assertArrayEquals(ipLogo.getLogoData(), logo.getData());
        assertEquals(ipLogo.getImageFormatWcode(), MapperHelper.logoTypeToFormatWCode(logo.getMimeType()));
        assertEquals(ipLogo.getColourDescription(), logo.getColourDescription());
        assertEquals(ipLogo.getColourdDescrLang2(), logo.getColourDescriptionInOtherLang());
    }
    //test the transformed CSignData.CLogo's viennaClasses
    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckSignDataLogoDataViennaClasses() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        CSignData signData = transformedCMark.getSignData();
        assertNotNull(signData);
        assertEquals(signData.getSignType(), MarkSignType.COMBINED);
        CMarkAttachment logo = signData.getAttachments().get(0);
        assertNotNull(logo);
        IpLogo ipLogo = originalIpMark.getLogo();
        assertNotNull(logo.getViennaClassList());
        List<CViennaClass> viennaClasses = logo.getViennaClassList();
        List<IpLogoViennaClasses> ipViennaClasses = ipLogo.getIpLogoViennaClassesCollection();
        assertNotNull(viennaClasses);
        assertTrue(viennaClasses.size() > 0);
        assertEquals(ipViennaClasses.size(), viennaClasses.size());
        for (int i = 0; i < ipViennaClasses.size(); i++) {
            IpLogoViennaClasses ipViennaClass = ipViennaClasses.get(i);
            CViennaClass viennaClass = viennaClasses.get(i);
            assertEquals(ipViennaClass.getPk().getViennaClassCode(), viennaClass.getViennaCategory());
            assertEquals(ipViennaClass.getPk().getViennaGroupCode(), viennaClass.getViennaDivision());
            assertEquals(ipViennaClass.getPk().getViennaElemCode(), viennaClass.getViennaSection());
            assertEquals(ipViennaClass.getViennaEditionCode(), viennaClass.getViennaVersion());
            assertEquals(ipViennaClass.getVclWpublishValidate(), viennaClass.getVclWpublishValidated());

        }
    }


    //tests the original IpFile and the transformed IpFile
    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestIpFile() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkIpFile(originalIpMark.getFile(), transformedIpMark.getFile());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkTestFileRelations1() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkTransformedRelationsData(originalIpMark, transformedCMark.getFile());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestFileRelations1() {

        initOriginalAndTransformedIpMarks(ipFilePK);
        checkFileRelations(originalIpMark.getFile().getIpFileRelationships1(), transformedIpMark.getFile().getIpFileRelationships1());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkTestFileRelations2() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkTransformedRelationsData(originalIpMark, transformedCMark.getFile());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestFileRelations2() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkFileRelations(originalIpMark.getFile().getIpFileRelationships2(), transformedIpMark.getFile().getIpFileRelationships2());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkTestCFileCOwnershipDataCOwner() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkTransformedCOwnershipData(originalIpMark, transformedCMark.getFile());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestOwners() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        compareIntellectualPropertyEntityOwners(originalIpMark, transformedIpMark);
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkTestCFileServicePerson() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkTransformedServicePerson(originalIpMark, transformedCMark.getFile());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestServicePerson() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        compareAddressAndPersonDataOrigin(originalIpMark.getServicePerson(), transformedIpMark.getServicePerson());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestMainOwner() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        compareAddressAndPersonDataOrigin(originalIpMark.getMainOwner(), transformedIpMark.getMainOwner());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkTestCFileCPriorityData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkTransformedPriorityData(originalIpMark, transformedCMark.getFile());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkTestCFileCSimpleProcessData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkTransformedSimpleProcessData(originalIpMark, transformedCMark.getFile());
    }


    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestPriorities() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        assertTrue(originalIpMark.getPriorities().size() > 0);//there should be at least one priority to compare!!!
        checkPriorities(originalIpMark.getPriorities(), transformedIpMark.getPriorities());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestIpMark() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getPk);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getRowVersion);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getBasicFileRef);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getByConsentDescription);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getDisclaimer);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getDisclaimerLang2);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getExhibitionDate);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getExhibitionNotes);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getFirstPriorityDate);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getIndManualInterpretation);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getIntregn);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getLastRenewalDate);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getMarkSeriesDescription);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getMarkTransliteration);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getMarkTransliterationLang2);
//        _assertEquals(originalIpMark, transformedIpMark, IpMark::getNotes);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getNovelty1Date);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getNovelty2Date);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getSignWcode);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getTranslation);
//        _assertEquals(originalIpMark, transformedIpMark, IpMark::getAssociationNbr); not mapped in C objects!
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getCaptureDate);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getEntitlementDate);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getExpirationDate);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getFilingDate);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getIndRegistered);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getJournalCode);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getNiceClassTxt);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getPublicationDate);
//        _assertEquals(originalIpMark, transformedIpMark, IpMark::getPublicationNbr);//not mapped in C objects!
//        _assertEquals(originalIpMark, transformedIpMark, IpMark::getPublicationSer);//not mapped in C objects!
//        _assertEquals(originalIpMark, transformedIpMark, IpMark::getPublicationTyp);//not mapped in C objects!
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getPublicationNotes);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getRegistrationDate);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getRegistrationDup);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getRegistrationNbr);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getRegistrationSer);
        _assertEquals(originalIpMark, transformedIpMark, IpMark::getRegistrationTyp);
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestIpName() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        IpName originalName = originalIpMark.getName();
        IpName transformedName = transformedIpMark.getName();
        assertNotNull(originalName);
        assertNotNull(transformedName);
        assertNotNull(originalName.getMarkName());
        assertNotNull(originalName.getMarkNameLang2());
        _assertEquals(originalName, transformedName, IpName::getMarkName);
        _assertEquals(originalName, transformedName, IpName::getMarkNameLang2);
//        _assertEquals(originalName, transformedName, IpName::getMapDenomination); //not mapped in CMark

    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestCaptureUserId() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        assertNotNull(originalIpMark.getCaptureUser().getUserId());
        assertEquals(originalIpMark.getCaptureUser().getUserId(), transformedIpMark.getCaptureUser().getUserId());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestIpRegulation() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        IpMarkRegulation originalRegulation = originalIpMark.getIpMarkRegulation();
        IpMarkRegulation transformedRegulation = transformedIpMark.getIpMarkRegulation();
        assertNotNull(originalRegulation);
        assertNotNull(transformedRegulation);
        assertNotNull(originalRegulation.getRegulationsDescription());
        checkFileSeqTypSerNbrPk(originalRegulation.getPk(), transformedRegulation.getPk());
        _assertEquals(originalRegulation, transformedRegulation, IpMarkRegulation::getRegulationsDescription);
        _assertEquals(originalRegulation, transformedRegulation, IpMarkRegulation::getRowVersion);

    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkCheckProtectionData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        CProtectionData protectionData = transformedCMark.getProtectionData();
        assertNotNull(protectionData.getNiceClassList());
        assertNotNull(originalIpMark.getIpMarkNiceClasses());
        assertTrue(protectionData.getNiceClassList().size() > 0);
        assertEquals(originalIpMark.getIpMarkNiceClasses().size(), protectionData.getNiceClassList().size());
        for (int i = 0; i < protectionData.getNiceClassList().size(); i++) {
            IpMarkNiceClasses originalNiceClass = originalIpMark.getIpMarkNiceClasses().get(i);
            CNiceClass transformedNiceClass = protectionData.getNiceClassList().get(i);
            assertEquals(originalNiceClass.getPk().getNiceClassCode().intValue(), (Object) transformedNiceClass.getNiceClassNbr());
            assertEquals(originalNiceClass.getPk().getNiceClassStatusWcode(), transformedNiceClass.getNiceClassDetailedStatus());
            if (originalNiceClass.getIpMarkNiceClassesExt() != null) {
                assertEquals(originalNiceClass.getIpMarkNiceClassesExt().getAllTermsDeclaration(), MapperHelper.getBooleanAsText(transformedNiceClass.getAllTermsDeclaration()));
            }
            assertEquals(originalNiceClass.getNiceClassEdition(), integerToLong(transformedNiceClass.getNiceClassEdition()));
            assertEquals(originalNiceClass.getNiceClassDescription(), transformedNiceClass.getNiceClassDescription());
            assertEquals(originalNiceClass.getNiceClassDescrLang2(), transformedNiceClass.getNiceClassDescriptionInOtherLang());
            assertEquals(originalNiceClass.getNiceClassVersion(), transformedNiceClass.getNiceClassVersion());


        }

    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestNiceClasses() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        List<IpMarkNiceClasses> originalNiceClasses = originalIpMark.getIpMarkNiceClasses();
        List<IpMarkNiceClasses> transformedNiceClasses = transformedIpMark.getIpMarkNiceClasses();
        assertNotNull(originalNiceClasses);
        assertNotNull(transformedNiceClasses);
        assertTrue(originalNiceClasses.size() > 0);
        assertEquals(originalNiceClasses.size(), transformedNiceClasses.size());
        for (int i = 0; i < originalNiceClasses.size(); i++) {
            IpMarkNiceClasses originalNiceClass = originalNiceClasses.get(i);
            IpMarkNiceClasses transformedNiceClass = transformedNiceClasses.get(i);
            _assertEquals(originalNiceClass, transformedNiceClass, IpMarkNiceClasses::getNiceClassDescription);
            _assertEquals(originalNiceClass, transformedNiceClass, IpMarkNiceClasses::getNiceClassDescrLang2);
            _assertEquals(originalNiceClass, transformedNiceClass, IpMarkNiceClasses::getNiceClassEdition);
            _assertEquals(originalNiceClass, transformedNiceClass, IpMarkNiceClasses::getNiceClassVersion);
            checkFileSeqTypSerNbrPk(originalNiceClass.getPk(), transformedNiceClass.getPk());
            _assertEquals(originalNiceClass.getPk(), transformedNiceClass.getPk(), IpMarkNiceClassesPK::getNiceClassStatusWcode);
            _assertEquals(originalNiceClass.getPk(), transformedNiceClass.getPk(), IpMarkNiceClassesPK::getNiceClassCode);
        }
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestIpLogo() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        IpLogo originalLogo = originalIpMark.getLogo();
        IpLogo transformedLogo = transformedIpMark.getLogo();
        assertNotNull(originalLogo);
        assertNotNull(transformedLogo);
        assertArrayEquals(originalLogo.getLogoData(), transformedLogo.getLogoData());
        _assertEquals(originalLogo, transformedLogo, IpLogo::getIndBase64);
        _assertEquals(originalLogo, transformedLogo, IpLogo::getColourDescription);
        _assertEquals(originalLogo, transformedLogo, IpLogo::getColourdDescrLang2);
        _assertEquals(originalLogo, transformedLogo, IpLogo::getImageFormatWcode);
        _assertEquals(originalLogo, transformedLogo, IpLogo::getRowVersion);
        assertNotNull(originalLogo.getIpLogoViennaClassesCollection());
        assertNotNull(transformedLogo.getIpLogoViennaClassesCollection());
        assertTrue(originalLogo.getIpLogoViennaClassesCollection().size() > 0);
        assertEquals(originalLogo.getIpLogoViennaClassesCollection().size(), transformedLogo.getIpLogoViennaClassesCollection().size());
        for (int i = 0; i < originalLogo.getIpLogoViennaClassesCollection().size(); i++) {
            IpLogoViennaClasses originalViennaClass = originalLogo.getIpLogoViennaClassesCollection().get(i);
            IpLogoViennaClasses transformedViennaClass = transformedLogo.getIpLogoViennaClassesCollection().get(i);
            _assertEquals(originalViennaClass, transformedViennaClass, IpLogoViennaClasses::getVclWpublishValidate);
            _assertEquals(originalViennaClass, transformedViennaClass, IpLogoViennaClasses::getViennaEditionCode);
            _assertEquals(originalViennaClass, transformedViennaClass, IpLogoViennaClasses::getRowVersion);
            checkFileSeqTypSerNbrPk(originalViennaClass.getPk(), transformedViennaClass.getPk());
            _assertEquals(originalViennaClass.getPk(), transformedViennaClass.getPk(), IpLogoViennaClassesPK::getViennaClassCode);
            _assertEquals(originalViennaClass.getPk(), transformedViennaClass.getPk(), IpLogoViennaClassesPK::getViennaElemCode);
            _assertEquals(originalViennaClass.getPk(), transformedViennaClass.getPk(), IpLogoViennaClassesPK::getViennaGroupCode);
        }


    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestApplicationTypeSubType() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkApplicationTypeSubtype(originalIpMark, transformedIpMark);
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestCfLaw() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkCfLaw(originalIpMark, transformedIpMark);
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkTestCFileCRepresentationData() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        checkTransformedRepresentationData(originalIpMark, transformedCMark.getFile());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestRepresentatives() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        compareIntellectualPropertyEntityRepresentatives(originalIpMark, transformedIpMark);
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkTestTransformation() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        IpFileRelationshipExtended originalTransformation = originalIpMark.getRelationshipExtended();
        CRelationshipExtended transformedTransformation = transformedCMark.getRelationshipExtended();
        assertEquals(originalTransformation.getCancellationDate(), transformedTransformation.getCancellationDate());
        assertEquals(originalTransformation.getFilingDate(), transformedTransformation.getFilingDate());
        assertEquals(originalTransformation.getFilingNumber(), transformedTransformation.getFilingNumber());
        assertEquals(originalTransformation.getPriorityDate(), transformedTransformation.getPriorityDate());
        assertEquals(originalTransformation.getRegistrationDate(), transformedTransformation.getRegistrationDate());
        assertEquals(originalTransformation.getApplicationType(), transformedTransformation.getApplicationType());
    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkRevertToIpMarkTestTransformations() {
        initOriginalAndTransformedIpMarks(ipFilePK);
        IpFileRelationshipExtended transformation = originalIpMark.getRelationshipExtended();
        IpFileRelationshipExtended transTransformation = transformedIpMark.getRelationshipExtended();
        assertNotNull(transTransformation);
        _assertEquals(transformation, transTransformation, IpFileRelationshipExtended::getCancellationDate);
        _assertEquals(transformation, transTransformation, IpFileRelationshipExtended::getFilingDate);
        _assertEquals(transformation, transTransformation, IpFileRelationshipExtended::getFilingNumber);
        _assertEquals(transformation, transTransformation, IpFileRelationshipExtended::getPriorityDate);
        _assertEquals(transformation, transTransformation, IpFileRelationshipExtended::getRegistrationDate);
        _assertEquals(transformation, transTransformation, IpFileRelationshipExtended::getFilingDate);
        checkFileSeqTypSerNbrPk(transformation.getPk(), transTransformation.getPk());
    }


}
