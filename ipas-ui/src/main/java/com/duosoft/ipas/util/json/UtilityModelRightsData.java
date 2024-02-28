package com.duosoft.ipas.util.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User: Georgi
 * Date: 6.12.2019 г.
 * Time: 18:25
 */
@Getter
@Setter
@NoArgsConstructor
public class UtilityModelRightsData extends PatentRightsData {
    private UtilityModelTransformationData transformationData;
    private UtilityModelParallelData utilityModelParallelData;
}
