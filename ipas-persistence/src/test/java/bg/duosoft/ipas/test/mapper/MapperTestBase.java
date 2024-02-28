package bg.duosoft.ipas.test.mapper;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.model.file.*;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.persistence.model.entity.*;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.persistence.model.entity.ext.agent.ExtendedPartnership;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.model.entity.person.IpAgent;
import bg.duosoft.ipas.persistence.model.entity.person.IpPerson;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 1.3.2019 г.
 * Time: 17:31
 */
public abstract class MapperTestBase extends TestBase {

    protected void checkIpFile(IpFile originalFile, IpFile transformedFile) {
        _assertEquals(originalFile, transformedFile, IpFile::getApplTyp);
        _assertEquals(originalFile, transformedFile, IpFile::getApplSubtyp);
        _assertEquals(originalFile, transformedFile, IpFile::getLawCode);
        _assertEquals(originalFile, transformedFile, IpFile::getFilingDate);
//        _assertEquals(originalFile, transformedFile, IpFile::getIntFilingDate);//not mapped in C objects, so not exist in the transformed ip object
        _assertEquals(originalFile, transformedFile, IpFile::getCaptureDate);
        _assertEquals(originalFile, transformedFile, IpFile::getValidationDate);
        _assertEquals(originalFile, transformedFile, IpFile::getValidationUserId);
        _assertEquals(originalFile, transformedFile, IpFile::getRegistrationNbr);
        _assertEquals(originalFile, transformedFile, IpFile::getRegistrationSer);
        _assertEquals(originalFile, transformedFile, IpFile::getRegistrationDate);
        _assertEquals(originalFile, transformedFile, IpFile::getRegistrationDup);
        _assertEquals(originalFile, transformedFile, IpFile::getRegistrationTyp);
        _assertEquals(originalFile, transformedFile, IpFile::getIndRegistered);
        _assertEquals(originalFile, transformedFile, IpFile::getEntitlementDate);
        _assertEquals(originalFile, transformedFile, IpFile::getExpirationDate);
        _assertEquals(originalFile, transformedFile, IpFile::getFirstPriorityDate);
        _assertEquals(originalFile, transformedFile, IpFile::getPublicationDate);
        _assertEquals(originalFile, transformedFile, IpFile::getJournalCode);
        _assertEquals(originalFile, transformedFile, IpFile::getSpecialPublDate);
        _assertEquals(originalFile, transformedFile, IpFile::getTitle);
        _assertEquals(originalFile, transformedFile, IpFile::getTitleLang2);
        _assertEquals(originalFile, transformedFile, IpFile::getRowVersion);
//        _assertEquals(originalFile, transformedFile, IpFile::getIndFaxOnly);//not mapped in C objects, so not exist in the transformed ip object
        _assertEquals(originalFile, transformedFile, IpFile::getFirstPriorityDate);
        _assertEquals(originalFile, transformedFile, IpFile::getCaptureUserId);
        _assertEquals(originalFile, transformedFile, IpFile::getSpecialPublDate);
//        _assertEquals(originalFile, transformedFile, IpFile::getRnewFileSeq);//not mapped in C objects, so not exist in the transformed ip object
//        _assertEquals(originalFile, transformedFile, IpFile::getRnewFileTyp);//not mapped in C objects, so not exist in the transformed ip object
//        _assertEquals(originalFile, transformedFile, IpFile::getRnewFileSer);//not mapped in C objects, so not exist in the transformed ip object
//        _assertEquals(originalFile, transformedFile, IpFile::getRnewFileNbr);//not mapped in C objects, so not exist in the transformed ip object
        _assertEquals(originalFile, transformedFile, IpFile::getIndForeign);
        _assertEquals(originalFile, transformedFile, IpFile::getPublicationNbr);
        _assertEquals(originalFile, transformedFile, IpFile::getPublicationSer);
        _assertEquals(originalFile, transformedFile, IpFile::getPublicationTyp);
        _assertEquals(originalFile, transformedFile, IpFile::getIndIncorrRecpDeleted);
        _assertEquals(originalFile, transformedFile, IpFile::getCorrFileSeq);
        _assertEquals(originalFile, transformedFile, IpFile::getCorrFileTyp);
        _assertEquals(originalFile, transformedFile, IpFile::getCorrFileSer);
        _assertEquals(originalFile, transformedFile, IpFile::getCorrFileNbr);
        _assertEquals(originalFile, transformedFile, IpFile::getProcNbr);
        _assertEquals(originalFile, transformedFile, IpFile::getProcTyp);
        IpFilePK originalPk = originalFile.getPk();
        IpFilePK transformedPk = transformedFile.getPk();
        checkFileSeqTypSerNbrPk(originalPk, transformedPk);
        assertNotNull(originalFile.getCfFileType());
        assertEquals(originalFile.getCfFileType().getFileTyp(), transformedFile.getCfFileType().getFileTyp());
    }
    protected void checkFileSeqTypSerNbrPk(FileSeqTypSerNbrPK originalPk, FileSeqTypSerNbrPK transformedPk) {
        _assertEquals(originalPk, transformedPk, FileSeqTypSerNbrPK::getFileNbr);
        _assertEquals(originalPk, transformedPk, FileSeqTypSerNbrPK::getFileSeq);
        _assertEquals(originalPk, transformedPk, FileSeqTypSerNbrPK::getFileSer);
        _assertEquals(originalPk, transformedPk, FileSeqTypSerNbrPK::getFileTyp);
    }

    protected void checkFileRelations(List<IpFileRelationship> originalRels, List<IpFileRelationship> transformedRels) {
        assertNotNull(originalRels);
        assertNotNull(transformedRels);
        assertEquals(originalRels.size(), transformedRels.size());
        for (int i = 0; i < originalRels.size(); i++) {
            IpFileRelationship originalRel = originalRels.get(i);
            IpFileRelationship transformedRel = transformedRels.get(i);
            _assertEquals(originalRel, transformedRel, IpFileRelationship::getPk);
        }
    }
    protected void checkApplicationTypeSubtype(IntellectualPropertyEntity original, IntellectualPropertyEntity transformed) {
        assertNotNull(original.getCfApplicationSubtype());
        assertNotNull(original.getCfApplicationSubtype().getCfApplicationType());
        assertNotNull(original.getCfApplicationSubtype().getCfApplicationType().getApplTyp());
        assertNotNull(original.getCfApplicationSubtype().getPk());
        assertNotNull(original.getCfApplicationSubtype().getPk().getApplTyp());
        assertNotNull(original.getCfApplicationSubtype().getPk().getApplSubtyp());
        assertEquals(original.getCfApplicationSubtype().getCfApplicationType().getApplTyp(), transformed.getCfApplicationSubtype().getCfApplicationType().getApplTyp());
        assertEquals(original.getCfApplicationSubtype().getPk().getApplTyp(), transformed.getCfApplicationSubtype().getPk().getApplTyp());
        assertEquals(original.getCfApplicationSubtype().getPk().getApplSubtyp(), transformed.getCfApplicationSubtype().getPk().getApplSubtyp());
    }
    protected void checkCfLaw(IntellectualPropertyEntity original, IntellectualPropertyEntity transformed) {
        assertNotNull(transformed.getCfLaw());
        assertEquals(original.getCfLaw().getLawCode(), transformed.getCfLaw().getLawCode());
    }

    protected void compareAddressAndPersonDataOrigin(IpPersonAddresses originalAddress, IpPersonAddresses compareAddress){

        IpPerson originalIpPerson = originalAddress.getIpPerson();
        IpPerson compareIpPerson = compareAddress.getIpPerson();


        assertEquals(originalIpPerson.getPersonName(),compareIpPerson.getPersonName());
        assertEquals(originalIpPerson.getNationalityCountryCode(),compareIpPerson.getNationalityCountryCode());
        assertEquals(originalIpPerson.getLegalNature(),compareIpPerson.getLegalNature());

        assertEquals(originalAddress.getAddrStreet(),compareAddress.getAddrStreet());
        assertEquals(originalAddress.getZipcode(),compareAddress.getZipcode());
        assertEquals(originalAddress.getStateName(),compareAddress.getStateName());
        assertEquals(originalAddress.getStateCode(),compareAddress.getStateCode());
        assertEquals(originalAddress.getAddrZone(),compareAddress.getAddrZone());

        assertEquals(originalIpPerson.getEmail(),compareIpPerson.getEmail());
        assertEquals(originalIpPerson.getTelephone(),compareIpPerson.getTelephone());
        assertEquals(originalIpPerson.getCompanyRegisterDate(),compareIpPerson.getCompanyRegisterDate());

        assertEquals(originalAddress.getResidenceCountry().getCountryCode(), compareAddress.getResidenceCountry().getCountryCode());
        assertEquals(originalAddress.getCityCode(),compareAddress.getCityCode());
        assertEquals(originalAddress.getCityName(),compareAddress.getCityName());

        assertEquals(originalIpPerson.getPersonNameLang2(),compareIpPerson.getPersonNameLang2());
        assertEquals(originalIpPerson.getLegalNatureLang2(),compareIpPerson.getLegalNatureLang2());

        assertEquals(originalAddress.getPk().getPersonNbr(),compareAddress.getPk().getPersonNbr());
        assertEquals(originalAddress.getPk().getAddrNbr(),compareAddress.getPk().getAddrNbr());
        if (originalIpPerson.getIpAgent() != null) {
            assertNotNull(compareIpPerson.getIpAgent());
            _assertEquals(originalIpPerson.getIpAgent(), compareIpPerson.getIpAgent(), IpAgent::getAgentCode);
        } else {
            assertNull(compareIpPerson.getIpAgent());
        }
        if (originalIpPerson.getExtendedPartnership() == null) {
            assertNull(compareIpPerson.getExtendedPartnership());
        } else {
            assertNotNull(compareIpPerson.getExtendedPartnership());
            _assertEquals(originalIpPerson.getExtendedPartnership(), compareIpPerson.getExtendedPartnership(), ExtendedPartnership::getPersonNbr);
            _assertEquals(originalIpPerson.getExtendedPartnership(), compareIpPerson.getExtendedPartnership(), ExtendedPartnership::getPartnershipCode);
        }
    }

    protected void compareIntellectualPropertyEntityOwners(IntellectualPropertyEntity original, IntellectualPropertyEntity transformed) {
        List<IntellectualPropertyOwner> originalOwners = original.getOwners();
        List<IntellectualPropertyOwner> transformedOwners= transformed.getOwners();
        assertTrue(originalOwners.size() > 0);
        assertEquals(originalOwners.size(), transformedOwners.size());
        for (int i = 0; i < originalOwners.size(); i++ ) {
            IntellectualPropertyOwner originalOwner = originalOwners.get(i);
            IntellectualPropertyOwner transformedOwner = transformedOwners.get(i);
            _assertEquals(originalOwner, transformedOwner, IntellectualPropertyOwner::getOrderNbr);
            _assertEquals(originalOwner.getPk(), transformedOwner.getPk(), IntellectualPropertyRelatedPersonPK::getAddrNbr);
            _assertEquals(originalOwner.getPk(), transformedOwner.getPk(), IntellectualPropertyRelatedPersonPK::getPersonNbr);
            checkFileSeqTypSerNbrPk(originalOwner.getPk(), transformedOwner.getPk());
            _assertEquals(originalOwner, transformedOwner, IntellectualPropertyOwner::getNotes);
            compareAddressAndPersonDataOrigin(originalOwner.getIpPersonAddresses(), transformedOwner.getIpPersonAddresses());
        }
    }
    protected void compareIntellectualPropertyEntityRepresentatives(IntellectualPropertyEntity original, IntellectualPropertyEntity transformed) {
        List<IntellectualPropertyRepresentative> originalReprs = original.getRepresentatives();
        List<IntellectualPropertyRepresentative> transformedReprs = transformed.getRepresentatives();
        assertTrue(originalReprs.size() > 0);
        assertEquals(originalReprs.size(), transformedReprs.size());
        for (int i = 0; i < originalReprs.size(); i++ ) {
            IntellectualPropertyRepresentative originalRepresentative = originalReprs.get(i);
            IntellectualPropertyRepresentative transformedRepresentative = transformedReprs.get(i);
            checkFileSeqTypSerNbrPk(originalRepresentative.getPk(), transformedRepresentative.getPk());
            _assertEquals(originalRepresentative.getPk(), transformedRepresentative.getPk(), IntellectualPropertyRepresentativePK::getRepresentativeTyp);
            compareAddressAndPersonDataOrigin(originalRepresentative.getIpPersonAddresses(), transformedRepresentative.getIpPersonAddresses());
        }
    }

    protected void checkPriorities(List<? extends IntellectualPropertyPriority> original, List<? extends IntellectualPropertyPriority> transformed) {
        assertNotNull(original);
        assertNotNull(transformed);
        assertEquals(original.size(), transformed.size());
        for (int i = 0; i < original.size(); i++) {
            IntellectualPropertyPriority o = original.get(i);
            IntellectualPropertyPriority t = transformed.get(i);

            _assertEquals(o, t, IntellectualPropertyPriority::getPriorityDate);
            _assertEquals(o, t, IntellectualPropertyPriority::getIndAccepted);
            _assertEquals(o, t, IntellectualPropertyPriority::getNotes);
            _assertEquals(o, t, IntellectualPropertyPriority::getPriorityApplIdAlt);
            assertTrue(o.getPriorityApplIdAlt() == null || o.getPriorityApplIdAlt().toString().length() <= 14);
            _assertEquals(o.getCountry(), t.getCountry(), CfGeoCountry::getCountryCode);
            _assertEquals(o.getPk(), t.getPk(), IntellectualPropertyPriorityPK::getCountryCode);
            _assertEquals(o.getPk(), t.getPk(), IntellectualPropertyPriorityPK::getPriorityApplId);
            _assertEquals(o.getPk(), t.getPk(), IntellectualPropertyPriorityPK::getFileSeq);
            _assertEquals(o.getPk(), t.getPk(), IntellectualPropertyPriorityPK::getFileTyp);
            _assertEquals(o.getPk(), t.getPk(), IntellectualPropertyPriorityPK::getFileSer);
            _assertEquals(o.getPk(), t.getPk(), IntellectualPropertyPriorityPK::getFileNbr);
        }
    }





    /**
     * compare entity to core transformation helper methods
     * @param cPerson
     * @param originalAddress
     * @param originalIpPerson
     */


    protected void compareAddressAndPersonDataCore(CPerson cPerson, IpPersonAddresses originalAddress, IpPerson originalIpPerson){
        assertEquals(originalIpPerson.getPersonName(),cPerson.getPersonName());
        assertEquals(originalIpPerson.getNationalityCountryCode(),cPerson.getNationalityCountryCode());
        assertEquals(originalIpPerson.getLegalNature(),cPerson.getLegalNature());
        assertEquals(originalAddress.getAddrStreet(),cPerson.getAddressStreet());
        assertEquals(originalAddress.getZipcode(),cPerson.getZipCode());
        assertEquals(originalAddress.getStateName(),cPerson.getStateName());
        assertEquals(originalAddress.getStateCode(),cPerson.getStateCode());
        assertEquals(originalAddress.getAddrZone(),cPerson.getAddressZone());
        assertEquals(originalIpPerson.getEmail(),cPerson.getEmail());
        assertEquals(originalIpPerson.getTelephone(),cPerson.getTelephone());
        assertEquals(originalIpPerson.getCompanyRegisterDate(),cPerson.getCompanyRegisterRegistrationDate());
        assertEquals(originalAddress.getResidenceCountry().getCountryCode(),cPerson.getResidenceCountryCode());
        assertEquals(originalAddress.getCityCode(),cPerson.getCityCode());
        assertEquals(originalAddress.getCityName(),cPerson.getCityName());
        assertEquals(originalIpPerson.getPersonNameLang2(),cPerson.getPersonNameInOtherLang());
        assertEquals(originalIpPerson.getLegalNatureLang2(),cPerson.getLegalNatureInOtherLang());
        assertEquals(originalAddress.getPk().getPersonNbr(),cPerson.getPersonNbr());
        assertEquals(originalAddress.getPk().getAddrNbr(),cPerson.getAddressNbr());
        assertEquals(originalAddress.getAddrStreetLang2(),cPerson.getAddressStreetInOtherLang());
        if (originalAddress.getIpPerson().getIpAgent() != null) {
            assertEquals(originalAddress.getIpPerson().getIpAgent().getAgentCode(), Integer.valueOf(cPerson.getAgentCode()));
        } else if (originalAddress.getIpPerson().getExtendedPartnership() != null) {
            assertEquals( originalAddress.getIpPerson().getExtendedPartnership().getPartnershipCode(), cPerson.getAgentCode());
            assertEquals(originalAddress.getIpPerson().getExtendedPartnership().getPersonNbr(), cPerson.getPersonNbr());
        }


    }


    public void checkTransformedBasicFileAndFilingData(IntellectualPropertyEntity original, CFile tranformed) {
        assertNotNull(tranformed);

        assertNotNull(original.getFile());
        assertNotNull(original.getFile().getIpDoc());
        assertEquals(original.getFile().getRowVersion(), tranformed.getRowVersion());

        CFilingData filingData = tranformed.getFilingData();
        assertNotNull(filingData);
        assertEquals(original.getCfApplicationSubtype().getPk().getApplTyp(), filingData.getApplicationType());
        assertEquals(original.getCfApplicationSubtype().getPk().getApplSubtyp(), filingData.getApplicationSubtype());
        assertEquals(original.getFilingDate(), filingData.getFilingDate());
        assertEquals(original.getCaptureDate(), filingData.getCaptureDate());
        assertNotNull(original.getCaptureUser());
        assertEquals(original.getCaptureUser().getUserId().longValue(), (Object) (filingData.getCaptureUserId()));



        assertEquals(original.getNovelty1Date(), filingData.getNovelty1Date());
        assertEquals(original.getNovelty2Date(), filingData.getNovelty2Date());
        assertEquals(original.getFile().getValidationDate(), filingData.getValidationDate());
        assertEquals(original.getFile().getValidationUserId() == null ? null : original.getFile().getValidationUserId().longValue(), filingData.getValidationUserId());
        assertEquals(original.getFile().getIpDoc().getReceptionDate(), filingData.getReceptionDate());
        assertEquals(original.getFile().getIpDoc().getReceptionUserId() == null ? null : original.getFile().getIpDoc().getReceptionUserId() .longValue(), filingData.getReceptionUserId());
        assertEquals(original.getFile().getIpDoc().getExternalOfficeCode(), filingData.getExternalOfficeCode());
        assertEquals(original.getFile().getIpDoc().getExternalOfficeFilingDate(), filingData.getExternalOfficeFilingDate());
        assertEquals(original.getFile().getIpDoc().getExternalSystemId(), filingData.getExternalSystemId());
        assertEquals(original.getFile().getIpDoc().getReceptionUserId() == null ? null : original.getFile().getIpDoc().getReceptionUserId().longValue(), filingData.getReceptionUserId());
        assertEquals(original.getFile().getIpDoc().getNotes(), tranformed.getNotes());
        assertEquals(original.getIndManualInterpretation(), MapperHelper.getBooleanAsText(filingData.getIndManualInterpretationRequired()));

    }
    public void checkTransformedPublicationData(IntellectualPropertyEntity original, CPublicationData publicationData) {
        assertNotNull(publicationData);
        assertEquals(original.getPublicationDate(), publicationData.getPublicationDate());
        assertEquals(original.getPublicationNotes(), publicationData.getPublicationNotes());
        assertEquals(original.getJournalCode(), publicationData.getJournalCode());
    }

    public void checkTransformedRegistrationData(IntellectualPropertyEntity original, CRegistrationData registrationData) {
        assertNotNull(registrationData);
        assertNotNull(registrationData.getRegistrationId());
        assertEquals(original.getRegistrationDate(), registrationData.getRegistrationDate());
        assertEquals(original.getEntitlementDate(), registrationData.getEntitlementDate());
        assertEquals(original.getExpirationDate(), registrationData.getExpirationDate());
        assertEquals(original.getRegistrationDup(), registrationData.getRegistrationId().getRegistrationDup());
        assertEquals(original.getRegistrationNbr() == null ? null : original.getRegistrationNbr().longValue(), registrationData.getRegistrationId().getRegistrationNbr());
        assertEquals(original.getRegistrationSer() == null ? null : original.getRegistrationSer().longValue(), registrationData.getRegistrationId().getRegistrationSeries());
        assertEquals(original.getRegistrationTyp(), registrationData.getRegistrationId().getRegistrationType());
        assertEquals(original.getIndRegistered(), MapperHelper.getBooleanAsText(registrationData.getIndRegistered()));
    }

    public void checkTransformedRelationsData(IntellectualPropertyEntity original, CFile transformed) {
        List<CRelationship> transformedRelations = transformed.getRelationshipList();
        List<IpFileRelationship> originalRels1 = original.getFile().getIpFileRelationships1();
        List<IpFileRelationship> originalRels2 = original.getFile().getIpFileRelationships2();
        assertNotNull(transformedRelations);
        assertEquals(transformedRelations.size(), (originalRels1 == null ? 0 : originalRels1.size()) + (originalRels2 == null ? 0 : originalRels2.size()));

        int rel1idx = 0;
        int rel2idx = 0;
        for (int i = 0; i < transformedRelations.size(); i++) {
            CRelationship rel = transformedRelations.get(i);
            IpFileRelationship ipFileRelationship;
            if ("1".equals(rel.getRelationshipRole())) {
                ipFileRelationship = originalRels1.get(rel1idx++);
                assertEquals(ipFileRelationship.getPk().getRelationshipTyp(), rel.getRelationshipType());
                assertEquals(ipFileRelationship.getPk().getFileSeq2(), rel.getFileId().getFileSeq());
                assertEquals(ipFileRelationship.getPk().getFileTyp2(), rel.getFileId().getFileType());
                assertEquals(ipFileRelationship.getPk().getFileSer2(), rel.getFileId().getFileSeries());
                assertEquals(ipFileRelationship.getPk().getFileNbr2(), rel.getFileId().getFileNbr());
            } else if ("2".equals(rel.getRelationshipRole())){
                ipFileRelationship = originalRels2.get(rel2idx++);
                assertEquals(ipFileRelationship.getPk().getRelationshipTyp(), rel.getRelationshipType());
                assertEquals(ipFileRelationship.getPk().getFileSeq1(), rel.getFileId().getFileSeq());
                assertEquals(ipFileRelationship.getPk().getFileTyp1(), rel.getFileId().getFileType());
                assertEquals(ipFileRelationship.getPk().getFileSer1(), rel.getFileId().getFileSeries());
                assertEquals(ipFileRelationship.getPk().getFileNbr1(), rel.getFileId().getFileNbr());
            } else {
                throw new RuntimeException("Unknown relationship type : " + rel.getRelationshipRole());
            }
        }
    }
    public void checkTransformedCOwnershipData(IntellectualPropertyEntity original, CFile transformed) {
        assertNotNull(original);
        assertNotNull(original.getOwners());
        assertNotNull(transformed);
        assertNotNull(transformed.getOwnershipData());
        assertNotNull(transformed.getOwnershipData().getOwnerList());
        assertTrue(original.getOwners().size() > 0);
        assertEquals(original.getOwners().size(), transformed.getOwnershipData().getOwnerList().size());
        for (int i = 0; i < original.getOwners().size(); i++) {
            IntellectualPropertyOwner originalOwner = original.getOwners().get(i);
            IpPersonAddresses originalAddress = originalOwner.getIpPersonAddresses();
            IpPerson originalIpPerson = originalAddress.getIpPerson();
            COwner cOwner = transformed.getOwnershipData().getOwnerList().get(i);
            CPerson cPerson = cOwner.getPerson();
            assertEquals(cOwner.getOwnershipNotes(), originalOwner.getNotes());
            assertEquals(cOwner.getOrderNbr(), originalOwner.getOrderNbr());
            compareAddressAndPersonDataCore(cPerson, originalAddress, originalIpPerson);
        }
    }

    public void checkTransformedRepresentationData(IntellectualPropertyEntity original, CFile transformed) {

        assertNotNull(original);
        List<IntellectualPropertyRepresentative> originalRepresentatives = original.getRepresentatives();

        assertNotNull(originalRepresentatives);
        assertTrue(originalRepresentatives.size() > 0);
        assertNotNull(transformed);
        CRepresentationData representationData = transformed.getRepresentationData();
        assertNotNull(representationData);

        assertNotNull(representationData.getRepresentativeList());
        assertEquals(originalRepresentatives.size(), representationData.getRepresentativeList().size());

        for (int i = 0; i < originalRepresentatives.size(); i++) {
            IntellectualPropertyRepresentative representative = originalRepresentatives.get(i);
            IpPersonAddresses originalAddress = representative.getIpPersonAddresses();
            IpPerson originalIpPerson= originalAddress.getIpPerson();

            CRepresentative cRepresentative = representationData.getRepresentativeList().get(i);
            CPerson cPerson= cRepresentative.getPerson();


            assertEquals(cRepresentative.getRepresentativeType(), representative.getPk().getRepresentativeTyp());

            compareAddressAndPersonDataCore(cPerson,originalAddress,originalIpPerson);

        }
    }
    protected void checkTransformedServicePerson(IntellectualPropertyEntity original, CFile transformed) {
        assertNotNull(original);
        assertNotNull(transformed);
        assertNotNull(transformed);
        assertNotNull(transformed.getServicePerson());

        CPerson cPerson = transformed.getServicePerson();
        IpPersonAddresses originalAddress = original.getServicePerson();
        IpPerson originalIpPerson = originalAddress.getIpPerson();

        compareAddressAndPersonDataCore(cPerson,originalAddress,originalIpPerson);
    }
    protected void checkTransformedPriorityData(IntellectualPropertyEntity original, CFile transformed) {
        assertNotNull(transformed);
        assertNotNull(original.getPriorities());

        assertNotNull(transformed.getPriorityData());
        assertNotNull(transformed.getPriorityData().getParisPriorityList());

        assertEquals(original.getPriorities().size(), transformed.getPriorityData().getParisPriorityList().size());
        assertEquals(original.getExhibitionDate(), transformed.getPriorityData().getExhibitionDate());
        assertEquals(original.getExhibitionNotes(), transformed.getPriorityData().getExhibitionNotes());
        assertEquals(original.getFirstPriorityDate(), transformed.getPriorityData().getEarliestAcceptedParisPriorityDate());

        if (!CollectionUtils.isEmpty(original.getPriorities())) {
            assertTrue(transformed.getPriorityData().isHasParisPriorityData());
        } else {
            assertTrue(!transformed.getPriorityData().isHasParisPriorityData());
        }

        for (int i = 0; i < original.getPriorities().size(); i++) {

            IntellectualPropertyPriority ipPriority = original.getPriorities().get(i);
            CParisPriority cParisPriority = transformed.getPriorityData().getParisPriorityList().get(i);

            assertEquals(cParisPriority.getCountryCode(),ipPriority.getCountry().getCountryCode());
            assertEquals(cParisPriority.getApplicationId(),ipPriority.getPk().getPriorityApplId());
            assertEquals(cParisPriority.getPriorityDate(),ipPriority.getPriorityDate());
            assertEquals(cParisPriority.getNotes(),ipPriority.getNotes());
            assertEquals(cParisPriority.getCountryCode(),ipPriority.getPk().getCountryCode());
            assertEquals(1,ipPriority.getRowVersion().intValue());

            if (cParisPriority.getPriorityStatus()==0){
                assertNull(ipPriority.getIndAccepted());
            } else if (cParisPriority.getPriorityStatus()==1) {
                assertEquals(ipPriority.getIndAccepted(),"S");
            } else{
                assertEquals(cParisPriority.getPriorityStatus().intValue(),2);
                assertEquals(ipPriority.getIndAccepted(),"N");
            }

        }
    }
    protected void checkTransformedSimpleProcessData(IntellectualPropertyEntity e, CFile transformed) {
        IpProcSimple ipProcSimple = e.getFile().getIpProcSimple();
        CProcessSimpleData cProcessSimpleData = transformed.getProcessSimpleData();
        assertNotNull(ipProcSimple);
        assertNotNull(cProcessSimpleData);
        assertEquals(ipProcSimple.getCreationDate(), cProcessSimpleData.getCreationDate());
        assertEquals(ipProcSimple.getStatusDate(), cProcessSimpleData.getStatusDate());
        assertEquals(ipProcSimple.getResponsibleUser().getUserId(), cProcessSimpleData.getResponsibleUser().getUserId());
        assertEquals(ipProcSimple.getResponsibleUser().getUserName(), cProcessSimpleData.getResponsibleUser().getUserName());
        assertEquals(ipProcSimple.getPk().getProcNbr(), cProcessSimpleData.getProcessId().getProcessNbr());
        assertEquals(ipProcSimple.getPk().getProcTyp(), cProcessSimpleData.getProcessId().getProcessType());
        assertEquals(ipProcSimple.getStatusCode(), cProcessSimpleData.getStatusCode());
    }
    protected Long integerToLong(Integer i) {
        return i == null ? null : i.longValue();
    }
}
