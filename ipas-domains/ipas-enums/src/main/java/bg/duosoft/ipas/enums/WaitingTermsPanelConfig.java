package bg.duosoft.ipas.enums;

public enum WaitingTermsPanelConfig {
    ALL("ALL"),
    ZMR("ZMR");

    WaitingTermsPanelConfig(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }
}
