package com.duosoft.ipas.util.session.userdoc;

import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.mark.CUserdocSingleDesign;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserdocInitPanelSessionObjectsUtils {

    public static void initMainDataPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_PROCESS_PARENT_DATA, sessionIdentifier, userdoc.getUserdocParentData(), request);
    }

    public static void initGroundTypesPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_ROOT_GROUNDS, sessionIdentifier, userdoc.getUserdocRootGrounds(), request);
    }
    public static void initCourtAppealsSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_COURT_APPEALS, sessionIdentifier, userdoc.getUserdocCourtAppeals(), request);
    }
    public static void initReviewerSquadSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REVIEWER_SQUAD, sessionIdentifier, userdoc.getReviewers(), request);
    }
    public static void initPersonPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_SERVICE_PERSON, sessionIdentifier, userdoc.getServicePerson(), request);

        List<CUserdocPersonRole> roles = userdoc.getUserdocType().getRoles();
        for (CUserdocPersonRole role : roles) {
            UserdocPersonRole userdocPersonRole = role.getRole();
            switch (userdocPersonRole){
                case APPLICANT:
                    List<CUserdocPerson> applicants = UserdocPersonUtils.selectApplicants(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_APPLICANTS, sessionIdentifier, applicants, request);
                    break;
                case OLD_OWNER:
                    List<CUserdocPerson> oldOwners = UserdocPersonUtils.selectOldOwners(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_OLD_OWNERS, sessionIdentifier, oldOwners, request);
                    break;
                case NEW_OWNER:
                    List<CUserdocPerson> newOwners = UserdocPersonUtils.selectNewOwners(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NEW_OWNERS, sessionIdentifier, newOwners, request);
                    break;
                case REPRESENTATIVE:
                    List<CUserdocPerson> representatives = UserdocPersonUtils.selectRepresentatives(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REPRESENTATIVES, sessionIdentifier, representatives, request);
                    break;
                case OLD_REPRESENTATIVE:
                    List<CUserdocPerson> oldRepresentatives = UserdocPersonUtils.selectOldRepresentatives(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_OLD_REPRESENTATIVES, sessionIdentifier, oldRepresentatives, request);
                    break;
                case NEW_REPRESENTATIVE:
                    List<CUserdocPerson> newRepresentatives = UserdocPersonUtils.selectNewRepresentatives(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NEW_REPRESENTATIVES, sessionIdentifier, newRepresentatives, request);
                    break;
                case REPRESENTATIVE_OF_THE_OWNER:
                    List<CUserdocPerson> representativesOfTheOwner = UserdocPersonUtils.selectRepresentativesOfTheOwner(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_REPRESENTATIVES_OF_THE_OWNER, sessionIdentifier, representativesOfTheOwner, request);
                    break;
                case GRANTOR:
                    List<CUserdocPerson> grantors = UserdocPersonUtils.selectGrantors(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_GRANTORS, sessionIdentifier, grantors, request);
                    break;
                case GRANTEE:
                    List<CUserdocPerson> grantees = UserdocPersonUtils.selectGrantees(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_GRANTEES, sessionIdentifier, grantees, request);
                    break;
                case PAYEE:
                    List<CUserdocPerson> payees = UserdocPersonUtils.selectPayees(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_PAYEE, sessionIdentifier, payees, request);
                    break;
                case PAYER:
                    List<CUserdocPerson> payers = UserdocPersonUtils.selectPayers(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_PAYER, sessionIdentifier, payers, request);
                    break;
                case PLEDGER:
                    List<CUserdocPerson> pledgers = UserdocPersonUtils.selectPledgers(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_PLEDGER, sessionIdentifier, pledgers, request);
                    break;
                case CREDITOR:
                    List<CUserdocPerson> creditors = UserdocPersonUtils.selectCreditors(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_CREDITOR, sessionIdentifier, creditors, request);
                    break;
                case OLD_CORRESPONDENCE_ADDRESS:
                    List<CUserdocPerson> oldCorrespondenceAddress = UserdocPersonUtils.selectOldCorrespondenceAddress(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_OLD_CORRESPONDENCE_ADDRESS, sessionIdentifier, oldCorrespondenceAddress, request);
                    break;
                case NEW_CORRESPONDENCE_ADDRESS:
                    List<CUserdocPerson> newCorrespondenceAddress = UserdocPersonUtils.selectNewCorrespondenceAddress(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NEW_CORRESPONDENCE_ADDRESS, sessionIdentifier, newCorrespondenceAddress, request);
                    break;
                case AFFECTED_INVENTOR:
                    List<CUserdocPerson> affectedInventors = UserdocPersonUtils.selectAffectedInventors(userdoc.getUserdocPersonData());
                    HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_AFFECTED_INVENTOR, sessionIdentifier, affectedInventors, request);
                    break;
            }
        }
    }

    public static void initTransferPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        setServiceScopeSessionObjects(request, sessionIdentifier, userdoc);
    }

    public static void initServiceScopePanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        setServiceScopeSessionObjects(request, sessionIdentifier, userdoc);
    }

    public static void initApprovedPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        setSessionApprovedNiceClasses(request, sessionIdentifier, userdoc);
    }

    public static void initWithdrawalPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        setServiceScopeSessionObjects(request, sessionIdentifier, userdoc);
    }

    public static void initLicensePanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        setServiceScopeSessionObjects(request, sessionIdentifier, userdoc);
    }

    public static void initRenewalPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        setServiceScopeSessionObjects(request, sessionIdentifier, userdoc);
    }

    private static void setSessionNiceClasses(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        List<CNiceClass> niceClasses;
        if (userdoc.getProtectionData() != null && userdoc.getProtectionData().getNiceClassList() != null) {
            niceClasses = userdoc.getProtectionData().getNiceClassList();
        } else {
            niceClasses = new ArrayList<>();
        }

        HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_NICE_CLASSES, sessionIdentifier, niceClasses, request);
    }

    private static void setSessionSingleDesigns(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        List<CUserdocSingleDesign> singleDesigns;
        if (Objects.nonNull(userdoc.getSingleDesigns())) {
            singleDesigns = userdoc.getSingleDesigns();
        } else {
            singleDesigns = new ArrayList<>();
        }

        HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_SINGLE_DESIGNS, sessionIdentifier, singleDesigns, request);
    }

    private static void setServiceScopeSessionObjects(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        CProcessParentData userdocParentData = userdoc.getUserdocParentData();
        if (UserdocUtils.isUserdocMainObjectMark(userdocParentData)) {
            setSessionNiceClasses(request, sessionIdentifier, userdoc);
        }
        if (UserdocUtils.isUserdocMainObjectDesign(userdocParentData)) {
            setSessionSingleDesigns(request, sessionIdentifier, userdoc);
        }
    }

    private static void setSessionApprovedNiceClasses(HttpServletRequest request, String sessionIdentifier, CUserdoc userdoc) {
        List<CNiceClass> niceClasses;
        if (userdoc.getApprovedNiceClassList() != null) {
            niceClasses = userdoc.getApprovedNiceClassList();
        } else {
            niceClasses = new ArrayList<>();
        }

        HttpSessionUtils.setAndCloneSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_APPROVED_NICE_CLASSES, sessionIdentifier, niceClasses, request);
    }

}
