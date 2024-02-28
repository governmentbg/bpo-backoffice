package bg.duosoft.ipas.enums;

public enum RecordalType {
    Licenses("license"),
    Pledge("pledge"),
    Transfer("transfer"),
    Renewal("renewal"),
    Change("change"),
    Withdrawal("withdrawal"),
    GenericRecordal("genericRecordal"),
    Bankruptcy("bankruptcy"),
    Change_representative("changeRepresentative"),
    Change_correspondence_address("changeCorrespondenceAddress"),
    Entry_changes("entryChanges"),
    Security_measure("securityMeasure");

    RecordalType(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }
}
