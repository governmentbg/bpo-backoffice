package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.enums.RelationshipType;
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
public class MarkTransformationData {

    private boolean hasTransformationData;
    private String applicationType;
    private String filingNumber;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date filingDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date registrationDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date cancellationDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date priorityDate;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date serveMessageDate;

    public CRelationshipExtended toTransformationRelationship(){
        if (this.hasTransformationData){
            CRelationshipExtended transformationRelationship = new CRelationshipExtended();
            transformationRelationship.setCancellationDate(this.getCancellationDate());
            transformationRelationship.setFilingDate(this.getFilingDate());
            transformationRelationship.setPriorityDate(this.getPriorityDate());
            transformationRelationship.setServeMessageDate(this.getServeMessageDate());
            transformationRelationship.setRegistrationDate(this.getRegistrationDate());
            transformationRelationship.setFilingNumber(this.getFilingNumber());
            transformationRelationship.setApplicationType(this.getApplicationType());
            transformationRelationship.setRelationshipType(RelationshipType.TRANSFORMED_MARK_TYPE);
            return transformationRelationship;
        }
        return null;
    }
}
