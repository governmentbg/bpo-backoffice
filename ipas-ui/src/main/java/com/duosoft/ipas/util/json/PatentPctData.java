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
public class PatentPctData {

    
    protected boolean hasPctData;
    private String pctApplicationId;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date pctApplicationDate;

    private String pctPublicationCountryCode;

    private String pctPublicationId;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date pctPublicationDate;
}
