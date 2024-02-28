package com.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum ManualSubProcessPanel {
    MainData("msprocess-main-data"),
    Process(GeneralPanel.Process.code());

    ManualSubProcessPanel(String code) {
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


    public static ManualSubProcessPanel selectByCode(String code) {
        ManualSubProcessPanel panel = Arrays.stream(ManualSubProcessPanel.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(panel))
            throw new RuntimeException("Canno find manual sub process panel with code: " + code);

        return panel;
    }

}
