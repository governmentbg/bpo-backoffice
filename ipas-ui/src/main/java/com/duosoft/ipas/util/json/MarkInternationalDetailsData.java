package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarkInternationalDetailsData {
    private String internationalFileNumber;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date intFilingDate;
    private String registrationNumber;
    private String replacementFilingNumber;
    private Boolean isAllServices;
}
