package com.duosoft.ipas.util.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Raya
 * 14.03.2019
 */
@Getter
@Setter
@NoArgsConstructor
public class MarkClaimData extends RightsData{
    private MarkTransformationData transformationData;
    private boolean hasPriority;
}
