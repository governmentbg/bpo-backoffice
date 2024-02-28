package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessActionFormData {
    private String notes1;
    private String notes2;
    private String notes3;
    private String notes4;
    private String notes5;
    private String specialFinalStatus;
    private List<String> offidocTemplates;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date manualDueDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date actionDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date recordalDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date invalidationDate;
    private String executionConfirmationText;
    private Boolean transferCorrespondenceAddress;
}
