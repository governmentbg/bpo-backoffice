package com.duosoft.ipas.util.session.offidoc;

public interface OffidocSessionObjects {

    String SESSION_OFFIDOC = "sessionOffidoc";

    static String[] getAllSessionObjectNames() {
        return new String[]{
                SESSION_OFFIDOC
        };
    }
}
