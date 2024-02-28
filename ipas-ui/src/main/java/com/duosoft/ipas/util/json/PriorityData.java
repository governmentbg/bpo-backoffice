package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.file.CParisPriority;
import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by Raya
 * 14.03.2019
 */
@Getter
@Setter
@NoArgsConstructor
public class PriorityData {

    private String countryCode;
    private String applicationId;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date priorityDate;
    private String notes;
    private boolean accepted;

    public CParisPriority convertToCParisPriority(){
        CParisPriority cParisPriority = new CParisPriority();
        cParisPriority.setApplicationId(applicationId);
        cParisPriority.setCountryCode(countryCode);
        cParisPriority.setPriorityDate(priorityDate);
        cParisPriority.setNotes(notes);

        if(accepted){
            cParisPriority.setPriorityStatus(1);
        } else {
            cParisPriority.setPriorityStatus(2);
        }
        return cParisPriority;
    }
}
