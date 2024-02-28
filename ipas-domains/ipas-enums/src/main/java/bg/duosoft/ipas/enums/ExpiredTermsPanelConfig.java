package bg.duosoft.ipas.enums;

public enum ExpiredTermsPanelConfig {
    ALL("ALL"),
    ZMR("ZMR");

    ExpiredTermsPanelConfig(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }
}
