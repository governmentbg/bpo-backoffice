package com.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum OffidocPanel {
    MainData("offidoc-main-data"),
    GenerateFiles("offidoc-generate-files-data"),
    PublishedDecision("offidoc-published-decision"),
    Decisions("offidoc-decisions-data"),
    Process(GeneralPanel.Process.code());

    OffidocPanel(String code) {
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


    public static OffidocPanel selectByCode(String code) {
        OffidocPanel panel = Arrays.stream(OffidocPanel.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(panel))
            throw new RuntimeException("Canno find offidoc panel with code: " + code);

        return panel;
    }

}
