package com.duosoft.ipas.util.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class OriginalReceptionResult {
    private Boolean isOriginal;
    private Integer originalRequestId;
    private String message;
    private boolean removeUpdateOriginalButton;

    public static OriginalReceptionResult notOriginalReceptionRequest() {
        return new OriginalReceptionResult(false, null,null,false);
    }
}

