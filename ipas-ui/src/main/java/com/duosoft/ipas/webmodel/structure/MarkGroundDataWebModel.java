package com.duosoft.ipas.webmodel.structure;


import bg.duosoft.ipas.util.date.DateUtils;
import com.duosoft.ipas.util.json.GroundNiceClasses;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkGroundDataWebModel {
    private Integer markGroundType;
    private String registrationNbr;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date registrationDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date filingDate;
    private String countryCode;
    private List<GroundNiceClasses> niceClasses;
    private Boolean niceClassesInd;
    private Boolean markImportedInd;
    private String filingNumber;
    private String nameText;
    private String legalGroundCategory;
    private String geographicalIndTyp;
    private String markSignTyp;

}
