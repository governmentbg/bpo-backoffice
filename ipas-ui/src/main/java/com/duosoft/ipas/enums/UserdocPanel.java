package com.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum UserdocPanel {
    Bankruptcy("userdoc-bankruptcy-data"),
    Change("userdoc-change-data"),
    Licenses("userdoc-license-data"),
    Persons(GeneralPanel.Persons.code()),
    Pledge("userdoc-pledge-data"),
    Process(GeneralPanel.Process.code()),
    Renewal("userdoc-renewal-data"),
    Security_measure("userdoc-security-measure-data"),
    Transfer("userdoc-transfer-data"),
    UserdocMainData("userdoc-main-data"),
    UserdocTypeData("userdoc-type-data"),
    Withdrawal("userdoc-withdrawal-data"),
    GenericRecordal("userdoc-generic-recordal-data"),
    Objection("userdoc-objection-data"),
    PatentAnnulmentRequest("userdoc-patent-annulment-request-data"),
    SpcAnnulmentRequest("userdoc-spc-annulment-request-data"),
    UtilityModelAnnulmentRequest("userdoc-utility-model-annulment-request-data"),
    DesignAnnulmentRequest("userdoc-design-annulment-request-data"),
    Revocation("userdoc-revocation-data"),
    CourtAppeals("userdoc-court-appeals-data"),
    ReviewerSquad("userdoc-reviewer-squad-data"),
    Entry_changes("userdoc-entry-changes-data"),
    ServiceScope("userdoc-service-scope-data"),
    InternationalNiceClassesRestrictions("userdoc-international-nice-data"),
    Approved("userdoc-approved-data"),
    Invalidity("userdoc-invalidity-data"),
    Opposition("userdoc-opposition-data"),
    Claim("userdoc-claim-data"),
    UserdocPatentData("userdoc-patent-data"),
    Change_representative("userdoc-change-representative-data"),
    Change_correspondence_address("userdoc-change-correspondence-address-data"),
    InternationalRegistration("userdoc-international-registration"),
    Payments(GeneralPanel.Payments.code()),
    History(GeneralPanel.History.code()),;

    UserdocPanel(String code) {
        this.code = code;
    }

    private String code;

    /**
     * Method that returns the code
     *
     * @return the code
     */
    public String code() {
        return code;
    }


    public static UserdocPanel selectByCode(String code) {
        UserdocPanel panel = Arrays.stream(UserdocPanel.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(panel))
            throw new RuntimeException("Canno find userdoc panel with code: " + code);

        return panel;
    }

}
