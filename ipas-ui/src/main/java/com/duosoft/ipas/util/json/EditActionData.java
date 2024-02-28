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
public class EditActionData {
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date actionDate;
    private boolean actionDateExist;

    private String journalCode;
    private boolean journalCodeExist;

    private String notes;
    private boolean notesExist;

    private String notes1;
    private boolean notes1Exist;

    private String notes2;
    private boolean notes2Exist;

    private String notes3;
    private boolean notes3Exist;

    private String notes4;
    private boolean notes4Exist;

    private String notes5;
    private boolean notes5Exist;
}


