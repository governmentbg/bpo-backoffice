package com.duosoft.ipas.util.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserdocPatentData {
    private String titleBg;
    private Integer descriptionPagesCount;
    private Integer claimsCount;
    private Integer drawingsCount;
}
