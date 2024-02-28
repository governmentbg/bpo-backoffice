package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.enums.RelationshipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UtilityModelParallelData extends RelationshipExtendedData {
    private boolean hasParallelData;
    protected boolean relationshipChanged;
    private String fileSeq;
    private String fileType;
    private Integer fileSeries;
    private Integer fileNbr;

    public RelationshipData getNationalParallelData() {
        if (fileNbr == null) {
            return null;
        }
        RelationshipData res = new RelationshipData(fileSeq, fileType, fileSeries, fileNbr);
        res.setRelationshipChanged(relationshipChanged);
        return res;
    }
    public CRelationshipExtended toParallelUtilityModelRelationship(){
        if (this.hasParallelData){
            CRelationshipExtended transformationRelationship = new CRelationshipExtended();
            transformationRelationship.setApplicationType(this.getApplicationType());
            transformationRelationship.setFilingNumber(this.getFilingNumber());
            transformationRelationship.setFilingDate(this.getFilingDate());
            transformationRelationship.setRegistrationNumber(this.getRegistrationNumber());
            transformationRelationship.setRegistrationDate(this.getRegistrationDate());
            transformationRelationship.setRegistrationCountry(this.getRegistrationCountry());
            transformationRelationship.setRelationshipType(RelationshipType.PARALLEL_PATENT_TYPE);
            return transformationRelationship;
        }
        return null;
    }
}
