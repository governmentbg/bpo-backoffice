package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateReceptionData {

    private String receptionName;

    private Integer receptionType;

    private Boolean originalExpected;

    private Integer submissionType;

    private String userdocRelatedObjectGroupType;

    private String userdocRelatedObjectNumber;

    private String receptionUserdocType;

    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date entryDate;

    private String euPatentType;

    private Integer euPatentNumber;

    private Boolean figurativeMark;

    private String receptionNotes;

    private List<String> additionalUserdocs;

}

