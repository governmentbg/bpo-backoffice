package bg.duosoft.ipas.enums.security;

import java.util.Arrays;

import static bg.duosoft.ipas.enums.security.SecurityRolePrefix.*;

/**
 * User: ggeorgiev
 * Date: 18.6.2019 г.
 * Time: 12:17
 */
public enum SecurityRole {

    HomePage("ipas.home.page"),
    AdminModule("ipas.admin.module"),
    StructureEdit("structure.edit"),
    StructureView("structure.view"),
    StructureGroupEdit("structure.group.edit"),
    StructureGroupView("structure.group.view"),
    UsersAndRolesView("users.roles.view"),
    CourtDecisionsView("court.decision.view"),
    DisputesView("dispute.view"),
    ReceptionCreator("reception.creator"),
    ReceptionCreatorFromExistingDocument("reception.creator.from.existing.document"),
    IpasWorkingDateChange("ipas.change.working.date"),
    ReceptionEntryDateChange("reception.entry.date.change"),
    ProcessAutomaticActionExecute("process.automatic.action.execute"),
    ProcessExecuteActionsForForeignObjects("process.execute-actions-for-foreign-object"),
    ProcessChangeResponsibleUser("process.change-responsible-user"),
    ProcessChangeManualDueDate("process.change-manual-due-date"),
    ProcessRegisterOldUserdocs("process.register-old-userdocs"),
    IpObjectsViewReceptionList("ipobjects.reception-list"),
    IpObjectsViewWaitingTermList("ipobjects.waiting-term-list"),
    IpObjectsViewWaitingTermZmrList("ipobjects.waiting-term-zmr-list"),
    IpObjectsViewInternationalMarksList("ipobjects.international-marks-list"),
    HomePanelsZmAndZmr(HOME_PANELS+".zm-and-zmr"),
    HomePanelExactCorresp(HOME_PANELS+".corresp-exact"),
    IpObjectsViewInternationalMarkReceptionsList("ipobjects.international-mark-receptions-list"),
    IpObjectsViewLastActionsList("ipobjects.last-actions-list"),
    NewlyAllocatedUserdocs(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "view-newly-allocated-userdocs"),
    IpObjectsMyObjectsList("ipobjects.my-objects"),
    IpObjectsMyUserdocsList("ipobjects.my-userdocs"),
    IpObjectsUserdocWaitingTermsList("ipobjects.userdoc-waiting-terms"),
    IpObjectsUserdocExpiredTermsList("ipobjects.userdoc-expired-terms"),
    IpObjectsViewExpiredTermList("ipobjects.expired-term-list"),
    IpObjectsViewExpiredTermZmrList("ipobjects.expired-term-zmr-list"),
    IpObjectsSearchForeignObjectsData("ipobjects.search-foreign-objects-home-page"),
    IpObjectsHistory("ipobjects.history"),
    PatentSecretData("patent.secret.data"),
    AdminEditPerson("ipas.admin.edit.person"),
    ExpiringMarksNotifications("ipobjects.expiring-marks-notifications"),
    CertificatesForPaidPatentFees("ipobjects.certificates-for-paid-patent-fees"),
    LastPaymentsList("ipobjects.last-payments-list"),
    NotLinkedPaymentsList("ipobjects.not-linked-payments-list"),
    PaymentsEdit(PAYMENTS_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX),
    PaymentsExtendedRights(PAYMENTS_SECURITY_ROLE_PREFIX + "." + EXTENDED_RIGHTS),
    PaymentsAdministration(PAYMENTS_SECURITY_ROLE_PREFIX + "." + "administration"),
    PaymentsModule("ipas.payments.module"),
    MarkViewAll(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + ALL_PREFIX),
    MarkViewOwn(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + OWN_PREFIX),
    AcpViewAll(ACP_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + ALL_PREFIX),
    AcpViewOwn(ACP_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + OWN_PREFIX),
    AcpIdentityData(ACP_SECURITY_ROLE_PREFIX + "." + "object-identity-data"),
    AcpProcess(ACP_SECURITY_ROLE_PREFIX + "." + "object-process-data"),
    AcpPersons(ACP_SECURITY_ROLE_PREFIX + "." + "object-person-data"),
    AcpEditOwn(ACP_SECURITY_ROLE_PREFIX +"."+ EDIT_PREFIX +"."+ OWN_PREFIX),
    AcpEditAll(ACP_SECURITY_ROLE_PREFIX +"."+ EDIT_PREFIX +"."+ ALL_PREFIX),
    MarkEditAll(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + ALL_PREFIX),
    MarkEditOwn(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + OWN_PREFIX),
    MarkIdentityData(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "object-identity-data"),
    MarkMainData(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "mark-main-data"),
    MarkInternationalData(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "mark-international-data"),
    AcpAffectedObjectsData(ACP_SECURITY_ROLE_PREFIX + "." + "acp-affected-objects-data"),
    AcpTakenItemsData(ACP_SECURITY_ROLE_PREFIX + "." + "acp-taken-items-data"),
    AcpCheckData(ACP_SECURITY_ROLE_PREFIX + "." + "acp-check-data"),
    AcpAdministrativePenaltyData(ACP_SECURITY_ROLE_PREFIX + "." + "acp-administrative-penalty-data"),
    AcpViolationPlacesData(ACP_SECURITY_ROLE_PREFIX + "." + "acp-violation-places-data"),
    MarkNiceClasses(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "mark-nice-data"),
    MarkInternationalNiceClasses(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX+ "." + "mark-international-nice-data"),
    MarkClaims(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "mark-claims-data"),
    MarkPublication(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "mark-publication-data"),
    MarkPayments(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "object-payments-data"),
    MarkPersons(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "object-person-data"),
    MarkProcess(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "object-process-data"),
    MarkRecordals(IP_OJBECT_MARK_SECURITY_ROLE_PREFIX + "." + "object-recordals-data"),


    PatentViewAll(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + ALL_PREFIX),
    PatentViewOwn(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + OWN_PREFIX),
    PatentEditAll(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + ALL_PREFIX),
    PatentEditOwn(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + OWN_PREFIX),
    PatentPersons(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "object-person-data"),
    PatentIdentityData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "object-identity-data"),
    SpcIdentityData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "spc-object-identity-data"),
    PatentMainData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "patent-main-data"),
    PlantMainData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "plant-main-data"),
    SpcMainData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "spc-main-data"),
    PatentPublishedDrawingsData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "published-drawings-data"),
    PatentCitationsData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "citations-data"),
    ClaimsData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "claims-data"),
    PatentRightsData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "patent-rights-data"),
    PatentPayments(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "object-payments-data"),
    PatentPublication(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "patent-publication-data"),
    PatentIPCData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "patent-ipc-data"),
    PatentCPCData(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "patent-cpc-data"),
    PatentProcess(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "object-process-data"),
    PatentRecordals(IP_OBJECT_PATENT_SECURITY_ROLE_PREFIX + "." + "object-recordals-data"),


    UserdocViewAll(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + ALL_PREFIX),
    UserdocViewOwn(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + OWN_PREFIX),
    UserdocEditAll(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + ALL_PREFIX),
    UserdocEditOwn(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + OWN_PREFIX),
    UserdocInternationalMarkReceptionsListZmAndZmr("userdoc.international-mark-receptions-list-zm-zmr"),
    UserdocBankruptcy(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-bankruptcy-data"),
    UserdocChange(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-change-data"),
    UserdocLicense(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-license-data"),
    UserdocPersons(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "object-person-data"),
    UserdocPledge(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-pledge-data"),
    UserdocProcess(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "object-process-data"),
    UserdocRenewal(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-renewal-data"),
    UserdocSecurityMeasure(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-security-measure-data"),
    UserdocChangeRepresentative(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-change-representative-data"),
    UserdocChangeCorrespondenceAddress(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-change-correspondence-address-data"),
    UserdocClaim(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-claim-data"),
    UserdocObjection(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-objection-data"),
    UserdocPatentAnnulmentRequest(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-patent-annulment-request-data"),
    UserdocSpcAnnulmentRequest(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-spc-annulment-request-data"),
    UserdocUtilityModelAnnulmentRequest(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-utility-model-annulment-request-data"),
    UserdocDesignAnnulmentRequest(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-design-annulment-request-data"),
    UserdocRevocation(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-revocation-data"),
    UserdocCourtAppeals(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-court-appeals-data"),
    UserdocReviewerSquad(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-reviewer-squad-data"),
    UserdocEntryChanges(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-entry-changes-data"),
    UserdocOpposition(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-opposition-data"),
    UserdocInvalidity(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-invalidity-data"),
    UserdocServiceScope(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-service-scope-data"),
    UserdocInternationalNiceClasses(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX+ "." + "userdoc-international-nice-data"),
    UserdocApproved(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-approved-data"),
    UserdocPatentData(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-patent-data"),
    UserdocTransfer(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-transfer-data"),
    UserdocMainData(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-main-data"),
    UserdocTypeData(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-type-data"),
    UserdocWithdrawal(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-withdrawal-data"),
    UserdocGenericRecordal(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-generic-recordal"),
    UserdocViewReceptionList(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "view-last-added"),
    UserdocPayments(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX + "." + "userdoc-payments"),
    UserdocInternationalRegistration(IP_OBJECT_USERDOC_SECURITY_ROLE_PREFIX+ "." + "userdoc-international-registration"),

    OffidocViewAll(IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + ALL_PREFIX),
    OffidocViewOwn(IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + VIEW_PREFIX + "." + OWN_PREFIX),
    OffidocEditAll(IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + ALL_PREFIX),
    OffidocEditOwn(IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + OWN_PREFIX),
    OffidocMainData(IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + "offidoc-main-data"),
    OffidocProcess(IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + "object-process-data"),
    OffidocGenerateFiles(IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + "offidoc-generate-files-data"),
    OffidocPublishedDecision(IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "." + "offidoc-published-decision"),
    OffidocNotifications(IP_OBJECT_OFFIDOC_SECURITY_ROLE_PREFIX + "."+"offidoc-notifications"),
    ManualSubProcessEditAll(MANUAL_SUB_PROCESS_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + ALL_PREFIX),
    ManualSubProcessEditOwn(MANUAL_SUB_PROCESS_SECURITY_ROLE_PREFIX + "." + EDIT_PREFIX + "." + OWN_PREFIX),
    ManualSubProcessMainData(MANUAL_SUB_PROCESS_SECURITY_ROLE_PREFIX + "." + "msprocess-main-data"),
    ManualSubProcessProcess(MANUAL_SUB_PROCESS_SECURITY_ROLE_PREFIX + "." + "object-process-data"),

    ;

    SecurityRole(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static SecurityRole getSecurityRole(String code) {
        return Arrays.stream(SecurityRole.values()).filter(r -> r.code.equals(code)).findAny().orElse(null);
    }


    public static void main(String[] args) {
        Arrays.stream(SecurityRole.values()).forEach(r -> System.out.println(String.format("INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES('%s','%s');", r.code, r.code)));
//        System.out.println("test");
    }
}