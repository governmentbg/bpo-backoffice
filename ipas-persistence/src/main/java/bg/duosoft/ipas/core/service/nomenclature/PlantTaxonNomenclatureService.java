package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.plant.CPlantTaxonNomenclature;

import java.util.List;

public interface PlantTaxonNomenclatureService {

    List<CPlantTaxonNomenclature> findCPlantTaxonNomenclatureByCommonClassifyBulAndCommonClassifyEngAndLatinClassify(String taxonNomData,int maxResults);

    CPlantTaxonNomenclature findById(Long id);
}
