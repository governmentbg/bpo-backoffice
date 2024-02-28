package bg.duosoft.ipas.enums;

public enum EarlierRightTypes {
    EARLIER_MARK_LAW_12_2(1),
    WELL_KNOWN_MARK_LAW_12_2(2),
    FAMOUS_MARK_LAW_12_3(3),
    UNREGISTERED_MARK_USED_IN_TRADE_LAW_12_4(4),
    BAD_REQUEST_LAW_12_5(5),
    MARK_REQUESTED_BY_AGENT_12_6(6),
    TRADING_COMPANY_12_7(7),
    EARLIER_REGISTERED_GEOGRAPH_12_8(8),
    COPYRIGHT(9),
    NAME_AND_PORTRAIT_RIGHT(10),
    PLANT_AND_BREEDS_RIGHT(11),
    INDUSTRIAL_PROPERTY_RIGHT(12);

    EarlierRightTypes(Integer code) { this.code = code;}
    private Integer code;
    public Integer code() {
        return code;
    }
}
