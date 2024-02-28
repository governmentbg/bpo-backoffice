package bg.duosoft.ipas.util.reception;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.CReceptionCorrespondent;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdocRequest;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.enums.ReceptionCorrespondentType;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.*;
import bg.duosoft.ipas.persistence.model.entity.person.IpPerson;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.CorrespondentTypeRepository;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ReceptionCorrespondentUtils {

    public static void mergeOrInsertCorrespondents(CReception receptionForm, PersonService personService) {
        List<CPerson> cPeople = extractPersons(receptionForm.getOwnershipData(), receptionForm.getRepresentationData());
        if (receptionForm.isUserdocRequest() && !CollectionUtils.isEmpty(receptionForm.getUserdoc().getUserdocPersons())) {
            cPeople.addAll(receptionForm.getUserdoc().getUserdocPersons().stream().map(r -> r.getPerson()).collect(Collectors.toList()));
        }
        cPeople.forEach(cPerson -> {
            CPerson result = personService.mergeOrInsertPersonAddress(cPerson);
            if (Objects.isNull(cPerson.getPersonNbr()) || 0 > cPerson.getPersonNbr())
                cPerson.setPersonNbr(result.getPersonNbr());
            if (Objects.isNull(cPerson.getAddressNbr()) || 0 > cPerson.getAddressNbr())
                cPerson.setAddressNbr(result.getAddressNbr());
        });
    }

    public static List<ReceptionCorrespondent> getReceptionCorrespondents(ReceptionRequest save, CReception receptionForm, IpPersonRepository ipPersonRepository, CorrespondentTypeRepository correspondentTypeRepository) {
        List<ReceptionCorrespondent> receptionCorrespondents = new ArrayList<>();

        CorrespondentType representativeCorrespondentType = selectCorrespondentType(ReceptionCorrespondentType.REPRESENTATIVE, correspondentTypeRepository);
        addReceptionRepresentatives(save, receptionForm, receptionCorrespondents, representativeCorrespondentType, ipPersonRepository);

        CorrespondentType applicantCorrespondentType = selectCorrespondentType(ReceptionCorrespondentType.APPLICANT, correspondentTypeRepository);
        addReceptionOwners(save, receptionForm, receptionCorrespondents, applicantCorrespondentType);
        return receptionCorrespondents;
    }

    public static List<ReceptionUserdocCorrespondent> getReceptionUserdocCorrespondents(ReceptionUserdocRequest userdocRequest, CReception receptionForm, IpPersonRepository ipPersonRepository, CorrespondentTypeRepository correspondentTypeRepository) {
        List<ReceptionUserdocCorrespondent> receptionCorrespondents = new ArrayList<>();

        CorrespondentType representativeCorrespondentType = selectCorrespondentType(ReceptionCorrespondentType.REPRESENTATIVE, correspondentTypeRepository);
        addReceptionUserdocRepresentatives(userdocRequest, receptionForm, receptionCorrespondents, representativeCorrespondentType, ipPersonRepository);

        CorrespondentType applicantCorrespondentType = selectCorrespondentType(ReceptionCorrespondentType.APPLICANT, correspondentTypeRepository);
        addReceptionUserdocOwners(userdocRequest, receptionForm, receptionCorrespondents, applicantCorrespondentType);
        return receptionCorrespondents;
    }

    private static void addReceptionRepresentatives(ReceptionRequest receptionRequest, CReception receptionForm,
                                                    List<ReceptionCorrespondent> receptionCorrespondents,
                                                    CorrespondentType representativeCorrespondentType, IpPersonRepository ipPersonRepository) {
        CRepresentationData representationData = receptionForm.getRepresentationData();
        if (Objects.nonNull(representationData) && !CollectionUtils.isEmpty(representationData.getRepresentativeList())) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            List<ReceptionCorrespondent> representatives = representativeList.stream()
                    .map(cRepresentative -> {
                        ReceptionCorrespondent correspondent = new ReceptionCorrespondent();
                        correspondent.setPk(new ReceptionCorrespondentPK());
                        correspondent.getPk().setReceptionRequestId(receptionRequest.getId());
                        correspondent.setCorrespondentType(representativeCorrespondentType);
                        correspondent.setRepresentativeType(cRepresentative.getRepresentativeType());
                        correspondent.getPk().setPersonNbr(cRepresentative.getPerson().getPersonNbr());
                        correspondent.getPk().setAddressNbr(cRepresentative.getPerson().getAddressNbr());
                        return correspondent;
                    })
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(representatives))
                receptionCorrespondents.addAll(representatives);
        }
    }

    private static void addReceptionUserdocRepresentatives(ReceptionUserdocRequest receptionRequest, CReception receptionForm,
                                                           List<ReceptionUserdocCorrespondent> correspondents,
                                                           CorrespondentType representativeCorrespondentType, IpPersonRepository ipPersonRepositor) {
        CRepresentationData representationData = receptionForm.getRepresentationData();
        if (Objects.nonNull(representationData) && !CollectionUtils.isEmpty(representationData.getRepresentativeList())) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            List<ReceptionUserdocCorrespondent> representatives = representativeList.stream()
                    .map(cRepresentative -> {
                        ReceptionUserdocCorrespondent correspondent = new ReceptionUserdocCorrespondent();
                        correspondent.setPk(new ReceptionUserdocCorrespondentPK());
                        correspondent.getPk().setReceptionUserdocRequestId(receptionRequest.getId());
                        correspondent.setCorrespondentType(representativeCorrespondentType);
                        correspondent.getPk().setPersonNbr(cRepresentative.getPerson().getPersonNbr());
                        correspondent.getPk().setAddressNbr(cRepresentative.getPerson().getAddressNbr());
                        correspondent.setRepresentativeType(cRepresentative.getRepresentativeType());
                        return correspondent;
                    })
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(representatives))
                correspondents.addAll(representatives);
        }
    }

    private static void addReceptionOwners(ReceptionRequest save, CReception receptionForm, List<ReceptionCorrespondent> receptionCorrespondents, CorrespondentType applicantCorrespondentType) {
        COwnershipData ownershipData = receptionForm.getOwnershipData();
        if (Objects.nonNull(ownershipData) && !CollectionUtils.isEmpty(ownershipData.getOwnerList())) {
            List<COwner> ownerList = ownershipData.getOwnerList();
            List<ReceptionCorrespondent> applicants = ownerList.stream()
                    .map(cOwner -> {
                        ReceptionCorrespondent receptionCorrespondent = new ReceptionCorrespondent();
                        receptionCorrespondent.setPk(new ReceptionCorrespondentPK());
                        receptionCorrespondent.getPk().setReceptionRequestId(save.getId());
                        receptionCorrespondent.setCorrespondentType(applicantCorrespondentType);
                        receptionCorrespondent.getPk().setPersonNbr(cOwner.getPerson().getPersonNbr());
                        receptionCorrespondent.getPk().setAddressNbr(cOwner.getPerson().getAddressNbr());
                        return receptionCorrespondent;
                    })
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(applicants))
                receptionCorrespondents.addAll(applicants);
        }
    }

    private static void addReceptionUserdocOwners(ReceptionUserdocRequest save, CReception receptionForm,
                                                  List<ReceptionUserdocCorrespondent> receptionUserdocCorrespondents,
                                                  CorrespondentType applicantCorrespondentType) {
        COwnershipData ownershipData = receptionForm.getOwnershipData();
        if (Objects.nonNull(ownershipData) && !CollectionUtils.isEmpty(ownershipData.getOwnerList())) {
            List<COwner> ownerList = ownershipData.getOwnerList();
            List<ReceptionUserdocCorrespondent> applicants = ownerList.stream()
                    .map(cOwner -> {
                        ReceptionUserdocCorrespondent receptionCorrespondent = new ReceptionUserdocCorrespondent();
                        receptionCorrespondent.setPk(new ReceptionUserdocCorrespondentPK());
                        receptionCorrespondent.getPk().setReceptionUserdocRequestId(save.getId());
                        receptionCorrespondent.setCorrespondentType(applicantCorrespondentType);
                        receptionCorrespondent.getPk().setPersonNbr(cOwner.getPerson().getPersonNbr());
                        receptionCorrespondent.getPk().setAddressNbr(cOwner.getPerson().getAddressNbr());
                        return receptionCorrespondent;
                    })
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(applicants))
                receptionUserdocCorrespondents.addAll(applicants);
        }
    }

    private static CorrespondentType selectCorrespondentType(ReceptionCorrespondentType type, CorrespondentTypeRepository correspondentTypeRepository) {
        CorrespondentType correspondentType = correspondentTypeRepository.findById(type.code()).orElse(null);
        if (Objects.isNull(correspondentType))
            throw new RuntimeException("Correspondent type is empty ! ");
        return correspondentType;
    }


    public static List<CReceptionCorrespondent> selectApplicantCorrespondents(List<CReceptionCorrespondent> correspondents) {
        if (CollectionUtils.isEmpty(correspondents))
            return null;

        return correspondents.stream()
                .filter(cReceptionCorrespondent -> cReceptionCorrespondent.getCorrespondentType().getId().equals(ReceptionCorrespondentType.APPLICANT.code()))
                .collect(Collectors.toList());
    }

    public static List<CReceptionCorrespondent> selectRepresentativeCorrespondents(List<CReceptionCorrespondent> correspondents) {
        if (CollectionUtils.isEmpty(correspondents))
            return null;

        return correspondents.stream()
                .filter(cReceptionCorrespondent -> cReceptionCorrespondent.getCorrespondentType().getId().equals(ReceptionCorrespondentType.REPRESENTATIVE.code()))
                .collect(Collectors.toList());
    }

    public static CReceptionRequest selectReceptionRequestWithMatchedApplicants(List<CPerson> applicants, List<CReceptionRequest> receptionRequests) {
        AtomicReference<CReceptionRequest> matchedReceptionRequest = new AtomicReference<>(null);
        for (CReceptionRequest receptionRequest : receptionRequests) {
            List<CReceptionCorrespondent> correspondents = selectApplicantCorrespondents(receptionRequest.getCorrespondents());
            if (Objects.isNull(correspondents))
                continue;

            if (isAllCorrespondentsMatch(applicants, correspondents))
                matchedReceptionRequest.set(receptionRequest);
        }
        return matchedReceptionRequest.get();
    }

    public static List<CReceptionUserdocRequest> selectUserdocReceptionRequestWithMatchedPersons(List<CPerson> applicants, List<CPerson> representatives, List<CReceptionUserdocRequest> receptionRequests) {
        List<CReceptionUserdocRequest> requestsWithMatchedApplicants = new ArrayList<>();
        for (CReceptionUserdocRequest receptionRequest : receptionRequests) {
            List<CReceptionCorrespondent> correspondents = selectApplicantCorrespondents(receptionRequest.getCorrespondents());
            if (Objects.isNull(correspondents))
                continue;

            if (isAllCorrespondentsMatch(applicants, correspondents))
                requestsWithMatchedApplicants.add(receptionRequest);
        }

        if (!CollectionUtils.isEmpty(requestsWithMatchedApplicants)) {
            List<CReceptionUserdocRequest> requestsWithMatchedRepresentatives = new ArrayList<>();
            for (CReceptionUserdocRequest receptionRequest : requestsWithMatchedApplicants) {
                List<CReceptionCorrespondent> correspondents = selectRepresentativeCorrespondents(receptionRequest.getCorrespondents());
                if (Objects.isNull(correspondents))
                    continue;

                if (isAllCorrespondentsMatch(representatives, correspondents))
                    requestsWithMatchedRepresentatives.add(receptionRequest);
            }

            if (!CollectionUtils.isEmpty(requestsWithMatchedRepresentatives)) {
                return requestsWithMatchedRepresentatives;
            }

        }

        return null;
    }

    private static boolean isAllCorrespondentsMatch(List<CPerson> persons, List<CReceptionCorrespondent> correspondents) {
        AtomicBoolean isAllMatch = new AtomicBoolean(true);
        for (CPerson person : persons) {
            AtomicBoolean isExist = new AtomicBoolean(false);
            Integer personNbr = person.getPersonNbr();
            Integer addressNbr = person.getAddressNbr();

            for (CReceptionCorrespondent correspondent : correspondents) {
                if (correspondent.getPersonNbr().equals(personNbr) && correspondent.getAddressNbr().equals(addressNbr)) {
                    isExist.set(true);
                }
            }

            if (!isExist.get()) {
                isAllMatch.set(false);
            }
        }
        return isAllMatch.get();
    }

    private static List<CPerson> extractPersons(COwnershipData ownershipData, CRepresentationData representationData) {
        List<CPerson> personList = new ArrayList<>();
        addOwnersToPersonList(personList, ownershipData);
        addRepresentsToPersonList(personList, representationData);
        return personList;
    }

    private static void addOwnersToPersonList(List<CPerson> personList, COwnershipData ownershipData) {
        if (Objects.nonNull(ownershipData)) {
            List<COwner> ownerList = ownershipData.getOwnerList();
            if (!CollectionUtils.isEmpty(ownerList)) {
                List<CPerson> result = ownerList.stream()
                        .map(COwner::getPerson).collect(Collectors.toList());
                personList.addAll(result);
            }
        }
    }

    private static void addRepresentsToPersonList(List<CPerson> personList, CRepresentationData representationData) {
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            if (!CollectionUtils.isEmpty(representativeList)) {
                List<CPerson> result = representativeList.stream()
                        .map(CRepresentative::getPerson).collect(Collectors.toList());
                personList.addAll(result);
            }
        }
    }


}
