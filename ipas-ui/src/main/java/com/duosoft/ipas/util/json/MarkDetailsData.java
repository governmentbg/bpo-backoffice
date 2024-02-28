package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarkDetailsData {

    private String signType;
    private String markName;
    private String markTransliteration;
    private String markTranslation;
    private String colourDescription;
    private String disclaimer;
//    private String notes;
//    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
//    private Date lastRenewalDate;
    private String description;
    private String usageRule;
}
