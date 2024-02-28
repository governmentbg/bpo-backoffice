package bg.duosoft.ipas.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum UserdocGroup {

    CORRESP("CORRESP"),
    CORRESP_EXACT("CORRESP_EXACT"),
    ZMR("ZMR"),
    MAIN_ZMR("MAIN_ZMR"),
    CORRESP_ZMR("CORRESP_ZMR"),
    MAIN_MR("MAIN_MR"),
    CORRESP_MR("CORRESP_MR"),
    MAIN("MAIN"),
    CORRESP_MR_WIPO("CORRESP_MR_WIPO"),
    CORRESP_ZMR_WIPO("CORRESP_ZMR_WIPO");

    UserdocGroup(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static UserdocGroup selectByCode(String code) {
        return Arrays.stream(UserdocGroup.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);
    }

    public static List<String>  getMainAndCorrespGroup(){
        List<String>list=new ArrayList<>();
        list.add(CORRESP.code());
        list.add(MAIN.code());
        return list;
    }
    public static List<String>  getAllWithoutMainAndCorrespGroup(){
        List<String>list=new ArrayList<>();
        list.add(ZMR.code());
        list.add(MAIN_ZMR.code());
        list.add(CORRESP_ZMR.code());
        list.add(MAIN_MR.code());
        list.add(CORRESP_MR.code());
        list.add(CORRESP_ZMR_WIPO.code());
        return list;
    }

}
