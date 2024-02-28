package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.person.CReprsPowerOfAttorney;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import com.duosoft.ipas.webmodel.structure.AttorneyDataWebModel;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class ReprsPowerOfAttorneyUtils {

    public static CReprsPowerOfAttorney constructUserdocAttorneyData(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr, Integer personKind) {
        PersonKind personKindEnum = PersonKind.selectByCode(personKind);
        UserdocPersonRole role = PersonUtils.selectUserdocPersonRoleByPersonKind(personKindEnum);
        List<CUserdocPerson> sessionPersons = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
        if (CollectionUtils.isEmpty(sessionPersons)) {
            throw new RuntimeException("Empty userdoc representative list on open power of attorney data modal!");
        }
        CUserdocPerson cUserdocPerson = sessionPersons.stream().filter(r -> r.getPerson().getPersonNbr().equals(personNbr) && r.getPerson().getAddressNbr().equals(addressNbr)).findFirst().orElse(null);
        if (Objects.isNull(cUserdocPerson)) {
            throw new RuntimeException("Userdoc representative not found on open power of attorney data modal!");
        }
        return new CReprsPowerOfAttorney(personNbr, addressNbr, personKind, cUserdocPerson.getAttorneyPowerTerm(), cUserdocPerson.getReauthorizationRight(),cUserdocPerson.getPriorReprsRevocation(), cUserdocPerson.getAuthorizationCondition());
    }

    public static CReprsPowerOfAttorney constructObjectAttorneyData(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr) {
        CRepresentationData representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
        List<CRepresentative> representativeList = representationData.getRepresentativeList();
        if (CollectionUtils.isEmpty(representativeList)) {
            throw new RuntimeException("Empty representative list on open power of attorney data modal!");
        }

        CRepresentative representative = representativeList.stream().filter(r -> r.getPerson().getPersonNbr().equals(personNbr) && r.getPerson().getAddressNbr().equals(addressNbr)).findFirst().orElse(null);
        if (Objects.isNull(representative)) {
            throw new RuntimeException("Representative not found on open power of attorney data modal!");
        }
        return new CReprsPowerOfAttorney(personNbr, addressNbr, null, representative.getAttorneyPowerTerm(), representative.getReauthorizationRight(),representative.getPriorReprsRevocation(), representative.getAuthorizationCondition());
    }

    public static CReprsPowerOfAttorney convertWebModelToCore(AttorneyDataWebModel attorneyDataWebModel) {
        CReprsPowerOfAttorney resultData = new CReprsPowerOfAttorney();
        resultData.setAttorneyPowerTerm(attorneyDataWebModel.getAttorneyPowerTerm());
        resultData.setAuthorizationCondition(StringUtils.hasText(attorneyDataWebModel.getAuthorizationCondition()) ? attorneyDataWebModel.getAuthorizationCondition() : null);
        resultData.setReauthorizationRight(attorneyDataWebModel.getReauthorizationRight());
        resultData.setPriorReprsRevocation(attorneyDataWebModel.getPriorReprsRevocation());
        resultData.setPersonKind(attorneyDataWebModel.getPersonKind());
        resultData.setPersonNbr(attorneyDataWebModel.getPersonNbr());
        resultData.setAddressNbr(attorneyDataWebModel.getAddressNbr());

        return resultData;
    }

    public static AttorneyDataWebModel convertCoreToWebModel(CReprsPowerOfAttorney attorneyData) {
        AttorneyDataWebModel attorneyDataWebModel = new AttorneyDataWebModel();
        attorneyDataWebModel.setAttorneyPowerTerm(attorneyData.getAttorneyPowerTerm());
        if (Objects.isNull(attorneyDataWebModel.getAttorneyPowerTerm())){
            attorneyDataWebModel.setAttorneyPowerTermIndefinite(true);
        }
        attorneyDataWebModel.setReauthorizationRight(attorneyData.getReauthorizationRight());
        attorneyDataWebModel.setPriorReprsRevocation(attorneyData.getPriorReprsRevocation());
        attorneyDataWebModel.setAuthorizationCondition(attorneyData.getAuthorizationCondition());
        attorneyDataWebModel.setPersonKind(attorneyData.getPersonKind());
        attorneyDataWebModel.setPersonNbr(attorneyData.getPersonNbr());
        attorneyDataWebModel.setAddressNbr(attorneyData.getAddressNbr());
        return attorneyDataWebModel;
    }



    public static void editUserdocAttorneyData(HttpServletRequest request, String sessionIdentifier, CReprsPowerOfAttorney attorneyPowerData) {
        PersonKind personKindEnum = PersonKind.selectByCode(attorneyPowerData.getPersonKind());
        UserdocPersonRole role = PersonUtils.selectUserdocPersonRoleByPersonKind(personKindEnum);
        List<CUserdocPerson> sessionPersons = PersonSessionUtils.selectUserdocSessionPersons(request, sessionIdentifier, role);
        if (!CollectionUtils.isEmpty(sessionPersons)) {
            CUserdocPerson cUserdocPerson = sessionPersons.stream().filter(r -> r.getPerson().getPersonNbr().equals(attorneyPowerData.getPersonNbr()) && r.getPerson().getAddressNbr().equals(attorneyPowerData.getAddressNbr())).findFirst().orElse(null);
            if (Objects.nonNull(cUserdocPerson)) {
                cUserdocPerson.setAttorneyPowerTerm(attorneyPowerData.getAttorneyPowerTerm());
                if (StringUtils.hasText(attorneyPowerData.getAuthorizationCondition())) {
                    cUserdocPerson.setAuthorizationCondition(attorneyPowerData.getAuthorizationCondition());
                } else {
                    cUserdocPerson.setAuthorizationCondition(null);
                }
                cUserdocPerson.setReauthorizationRight(attorneyPowerData.getReauthorizationRight());
                cUserdocPerson.setPriorReprsRevocation(attorneyPowerData.getPriorReprsRevocation());
            }
        }
    }

    public static void editObjectAttorneyData(HttpServletRequest request, String sessionIdentifier, CReprsPowerOfAttorney attorneyPowerData) {
        CRepresentationData representationData = PersonSessionUtils.getSessionRepresentationData(request, sessionIdentifier);
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            CRepresentative representative = representativeList.stream().filter(r -> r.getPerson().getPersonNbr().equals(attorneyPowerData.getPersonNbr()) && r.getPerson().getAddressNbr().equals(attorneyPowerData.getAddressNbr())).findFirst().orElse(null);


            if (Objects.nonNull(representative)) {
                representative.setAttorneyPowerTerm(attorneyPowerData.getAttorneyPowerTerm());
                if (StringUtils.hasText(attorneyPowerData.getAuthorizationCondition())) {
                    representative.setAuthorizationCondition(attorneyPowerData.getAuthorizationCondition());
                } else {
                    representative.setAuthorizationCondition(null);
                }
                representative.setReauthorizationRight(attorneyPowerData.getReauthorizationRight());
                representative.setPriorReprsRevocation(attorneyPowerData.getPriorReprsRevocation());
            }


        }
    }
}
