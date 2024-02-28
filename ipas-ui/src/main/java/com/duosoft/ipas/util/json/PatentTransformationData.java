package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.enums.RelationshipType;
import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PatentTransformationData extends RelationshipExtendedData{
    private boolean hasTransformationData;

    public CRelationshipExtended toTransformationRelationship(){
        if (this.hasTransformationData){
            CRelationshipExtended transformationRelationship = new CRelationshipExtended();
            transformationRelationship.setApplicationType(this.getApplicationType());
            transformationRelationship.setFilingNumber(this.getFilingNumber());
            transformationRelationship.setFilingDate(this.getFilingDate());
            transformationRelationship.setRegistrationNumber(this.getRegistrationNumber());
            transformationRelationship.setRegistrationDate(this.getRegistrationDate());
            transformationRelationship.setRegistrationCountry(this.getRegistrationCountry());
            transformationRelationship.setRelationshipType(RelationshipType.TRANSFORMED_NATIONAL_PATENT_TYPE);
            return transformationRelationship;
        }
        return null;
    }
}
