package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.core.model.acp.CAcpPersonsData;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.mark.CLimitationData;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CSignData;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CTechnicalData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.CReceptionCorrespondent;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.core.service.reception.ReceptionInitializationService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonAdressesRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Transactional
public class ReceptionInitializationServiceImpl implements ReceptionInitializationService {
    private static final Integer MARK_LAW = 1;
    private static final Integer PATENT_LAW = 2;
    private static final Integer PLANT_LAW = 4;
    private static final Integer DESIGN_LAW = 3;
    public static final Integer CORRESPONDENCE_APPLICANT = 2;
    public static final Integer CORRESPONDENCE_REPRESENTATIVE = 3;

    @Autowired
    private IpPersonAdressesRepository ipPersonAdressesRepository;

    @Autowired
    private PersonAddressMapper personAddressMapper;


    @Override
    public CMark initMark(CFile cFile, CReceptionRequest receptionRequest) {
        CMark mark = new CMark();
        mark.setReception(true);
        mark.setFile(cFile);
        mark.setSignData(new CSignData());
        mark.setLimitationData(new CLimitationData());
        mark.getFile().setOwnershipData(new COwnershipData());
        mark.getFile().setRepresentationData(new CRepresentationData());
        mark.getFile().getFilingData().setLawCode(MARK_LAW);

        boolean isFigurativeMark = receptionRequest.getName().equalsIgnoreCase(DefaultValue.EMPTY_OBJECT_NAME);
        if (isFigurativeMark) {
            mark.getSignData().setSignType(MarkSignType.FIGURATIVE);
        } else {
            mark.getSignData().setMarkName(receptionRequest.getName());
            mark.getSignData().setSignType(MarkSignType.WORD);
        }

        mark.getFile().getFilingData().getReceptionDocument().setIndFaxReception(receptionRequest.getOriginalExpected());
        mark.setNovelty1Date(cFile.getFilingData().getFilingDate());
        mark.setNovelty2Date(cFile.getFilingData().getFilingDate());
        mark.getFile().setAcpPersonsData(new CAcpPersonsData());
        mark.getFile().getAcpPersonsData().setRepresentationData(new CRepresentationData());
        mark.getFile().getAcpPersonsData().getRepresentationData().setRepresentativeList(new ArrayList<>());
        initPersons(cFile, receptionRequest);
        return mark;
    }

    @Override
    public CPatent initPatent(CFile cFile, CReceptionRequest receptionRequest) {
        CPatent cPatent = new CPatent();
        cPatent.setReception(true);
        cPatent.setPctApplicationData(null);
        cPatent.setTechnicalData(new CTechnicalData());
        cPatent.getTechnicalData().setDrawingList(new ArrayList<>());
        cPatent.getTechnicalData().setClaimList(new ArrayList<>());
        cPatent.getTechnicalData().setIpcClassList(new ArrayList<>());
        cPatent.setAuthorshipData(new CAuthorshipData());
        cPatent.getAuthorshipData().setAuthorList(new ArrayList<>());
        cPatent.setFile(cFile);
        cPatent.getFile().setOwnershipData(new COwnershipData());
        cPatent.getFile().setRepresentationData(new CRepresentationData());
        initPersons(cFile, receptionRequest);

        FileType fileType = FileType.selectByCode(cFile.getFileId().getFileType());

        if (fileType == FileType.PATENT || fileType == FileType.UTILITY_MODEL || fileType == FileType.SPC || fileType == FileType.EU_PATENT) {
            cPatent.getFile().getFilingData().setLawCode(PATENT_LAW);
        }
        if (fileType == FileType.DESIGN) {
            cPatent.getFile().getFilingData().setLawCode(DESIGN_LAW);
        }

        if (fileType == FileType.EU_PATENT) {
            cPatent.getTechnicalData().setEnglishTitle(receptionRequest.getName());
        }

        if (fileType == FileType.SPC) {
            cPatent.getTechnicalData().setMainAbstract(receptionRequest.getName());
        } else {
            cPatent.getTechnicalData().setTitle(receptionRequest.getName());
        }

        if (fileType == FileType.PLANTS_AND_BREEDS) {
            cPatent.getFile().getFilingData().setLawCode(PLANT_LAW);
        }

        return cPatent;
    }

    private void initPersons(CFile cFile, CReceptionRequest receptionRequest) {
        List<CReceptionCorrespondent> receptionCorrespondents = receptionRequest.getCorrespondents();
        if (!CollectionUtils.isEmpty(receptionCorrespondents)) {
            AtomicInteger orderNbr = new AtomicInteger();
            receptionCorrespondents.forEach(cCorrespondent -> {
                IpPersonAddressesPK pk = new IpPersonAddressesPK(cCorrespondent.getPersonNbr(), cCorrespondent.getAddressNbr());
                IpPersonAddresses person = ipPersonAdressesRepository.findById(pk).orElse(null);
                if (Objects.nonNull(person)) {
                    CPerson cPerson = personAddressMapper.toCore(person);
                    if (CORRESPONDENCE_APPLICANT.equals(cCorrespondent.getCorrespondentType().getId())) {
                        List<COwner> ownerList = cFile.getOwnershipData().getOwnerList();
                        if (Objects.isNull(ownerList)) {
                            ownerList = new ArrayList<>();
                            cFile.getOwnershipData().setOwnerList(ownerList);
                        }
                        COwner cOwner = new COwner();
                        cOwner.setPerson(cPerson);
                        cOwner.setOrderNbr(orderNbr.get());
                        orderNbr.getAndIncrement();
                        ownerList.add(cOwner);
                    } else if (CORRESPONDENCE_REPRESENTATIVE.equals(cCorrespondent.getCorrespondentType().getId())) {
                        List<CRepresentative> representativeList = cFile.getRepresentationData().getRepresentativeList();
                        if (Objects.isNull(representativeList)) {
                            representativeList = new ArrayList<>();
                            cFile.getRepresentationData().setRepresentativeList(representativeList);
                        }

                        RepresentativeType representativeType = RepresentativeUtils.convertRepresentativeTypeValueToEnum(cCorrespondent.getRepresentativeType());
                        CRepresentative cRepresentative = RepresentativeUtils.convertToRepresentative(cPerson,representativeType);
                        representativeList.add(cRepresentative);
                    }
                }
            });
        }
    }

}
