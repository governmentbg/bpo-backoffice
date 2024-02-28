package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RelationshipExtendedData {

    private String applicationType;
    private String filingNumber;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date filingDate;
    private String registrationNumber;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date registrationDate;
    private String registrationCountry;
}
