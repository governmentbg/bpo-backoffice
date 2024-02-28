package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.plant.CPlantTaxonNomenclature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlantTaxonNomenclatureData {

    private Long id;
    private String taxonCode;
    private String commonClassifyBul;
    private String commonClassifyEng;
    private String latinClassify;

    public PlantTaxonNomenclatureData(CPlantTaxonNomenclature cPlantTaxonNomenclature) {
        this.id = cPlantTaxonNomenclature.getId();
        this.taxonCode = cPlantTaxonNomenclature.getTaxonCode();
        this.commonClassifyBul = cPlantTaxonNomenclature.getCommonClassifyBul();
        this.commonClassifyEng = cPlantTaxonNomenclature.getCommonClassifyEng();
        this.latinClassify = cPlantTaxonNomenclature.getLatinClassify();
    }
}
