package com.duosoft.ipas.util.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User: Georgi
 * Date: 13.12.2019 г.
 * Time: 12:26
 */
@Getter
@Setter
@NoArgsConstructor
public class UtilityModelTransformationData extends PatentTransformationData {
    protected boolean relationshipChanged;
    private String fileSeq;
    private String fileType;
    private Integer fileSeries;
    private Integer fileNbr;


    public RelationshipData getNationalPatentTransformationData() {
        if (fileNbr == null) {
            return null;
        }
        RelationshipData res = new RelationshipData(fileSeq, fileType, fileSeries, fileNbr);
        res.setRelationshipChanged(relationshipChanged);
        return res;
    }
}
