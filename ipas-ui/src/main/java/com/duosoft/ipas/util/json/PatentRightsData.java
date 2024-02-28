package com.duosoft.ipas.util.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatentRightsData extends RightsData {
    private boolean hasPriority;
    private PatentPctData dataPct;
    private PatentTransformationData transformationData;
}
