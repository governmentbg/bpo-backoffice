var GeneralPanel = {
    Persons: "object-person-data",
    IdentityData: "object-identity-data",
    Process: "object-process-data",
    Payments: "object-payments-data",
    Recordals: "object-recordal-data",
    History: "object-history-data",
};

var MarkPanel = {
    IdentityData: GeneralPanel.IdentityData,
    MainData: "mark-main-data",
    InternationalData: "mark-international-data",
    AcpAffectedObjectsData: "acp-affected-objects-data",
    AcpCheckData: "acp-check-data",
    AcpAdministrativePenaltyData: "acp-administrative-penalty-data",
    AcpTakenItemsData: "acp-taken-items-data",
    AcpViolationPlacesData: "acp-violation-places-data",
    NiceClasses: "mark-nice-data",
    InternationalNiceClassesRestrictions:"mark-international-nice-data",
    Persons: GeneralPanel.Persons,
    Claims: "mark-claims-data",
    Publication: "mark-publication-data",
    Payments: GeneralPanel.Payments,
    Process: GeneralPanel.Process,
    Recordals: GeneralPanel.Recordals,
    History: GeneralPanel.History,
};

var PanelContainer = {
    Patent: "panel-container-patent",
    Mark: "panel-container-mark",
    Userdoc: "panel-container-userdoc",
    Offidoc: "panel-container-offidoc",
    ManualSubProcess: "panel-container-msprocess",
};

var PatentPanel = {
    Payments: GeneralPanel.Payments,
    IpcData: "patent-ipc-data",
    CpcData: "patent-cpc-data",
    IdentityData: GeneralPanel.IdentityData,
    MainData: "patent-main-data",
    PlantMainData: "plant-main-data",
    SpcMainData: "spc-main-data",
    SpcIdentityData: "spc-"+ GeneralPanel.IdentityData,
    PublishedDrawingsData: "published-drawings-data",
    DesignDrawingsData: "design-drawings-data",
    ClaimsData: "claims-data",
    CitationsData: "patent-citations-data",
    RightsData: "patent-rights-data",
    Publication: "patent-publication-data",
    Persons: GeneralPanel.Persons,
    Recordals: GeneralPanel.Recordals,
    History: GeneralPanel.History,
};

var UserdocPanel = {
    Bankruptcy: "userdoc-bankruptcy-data",
    Change: "userdoc-change-data",
    Licenses: "userdoc-license-data",
    Persons: GeneralPanel.Persons,
    Pledge: "userdoc-pledge-data",
    Process: GeneralPanel.Process,
    Renewal: "userdoc-renewal-data",
    Security_measure: "userdoc-security-measure-data",
    Claim :"userdoc-claim-data",
    UserdocPatentData :"userdoc-patent-data",
    Objection:"userdoc-objection-data",
    PatentAnnulmentRequest:"userdoc-patent-annulment-request-data",
    SpcAnnulmentRequest:"userdoc-spc-annulment-request-data",
    UtilityModelAnnulmentRequest:"userdoc-utility-model-annulment-request-data",
    DesignAnnulmentRequest:"userdoc-design-annulment-request-data",
    Revocation:"userdoc-revocation-data",
    CourtAppeals:"userdoc-court-appeals-data",
    ReviewerSquad:"userdoc-reviewer-squad-data",
    Entry_changes:"userdoc-entry-changes-data",
    Opposition:"userdoc-opposition-data",
    Invalidity:"userdoc-invalidity-data",
    ServiceScope:"userdoc-service-scope-data",
    InternationalNiceClassesRestrictions:"userdoc-international-nice-data",
    Approved:"userdoc-approved-data",
    Transfer: "userdoc-transfer-data",
    UserdocMainData: "userdoc-main-data",
    UserdocTypeData: "userdoc-type-data",
    Withdrawal: "userdoc-withdrawal-data",
    GenericRecordal: "userdoc-generic-recordal-data",
    InternationalRegistration: "userdoc-international-registration"
};

var OffidocPanel = {
    Decisions: "panel-offidoc-decisions-data"
}