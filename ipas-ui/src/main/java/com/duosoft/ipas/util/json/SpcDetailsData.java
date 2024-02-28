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
public class SpcDetailsData {

    private String mainAbstract;
    private String englishAbstract;
    private String bgPermitNumber;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date bgPermitDate;
    private String euPermitNumber;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date euPermitDate;
    private String productClaims;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date bgNotificationDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date euNotificationDate;
}
