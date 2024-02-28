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
public class IdentityData {
    private String applicationType;

    private String applicationSubtype;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date filingDate;

    private Long registrationNbr;

    private String registrationDup;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date registrationDate;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date entitlementDate;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date expirationDate;

    private Boolean indFaxReception;

    private String notes;

    private String publicationTyp;

    private String efilingDataLoginName;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date notInForceDate;
}
