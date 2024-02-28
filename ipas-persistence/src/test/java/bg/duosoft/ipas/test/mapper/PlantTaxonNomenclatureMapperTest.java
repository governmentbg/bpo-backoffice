package bg.duosoft.ipas.test.mapper;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.mapper.plant.PlantTaxonNomenclatureMapper;
import bg.duosoft.ipas.core.model.plant.CPlantTaxonNomenclature;
import bg.duosoft.ipas.persistence.model.entity.ext.plant.PlantTaxonNomenclature;
import bg.duosoft.ipas.persistence.repository.entity.plant.PlantTaxonNomenclatureRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

public class PlantTaxonNomenclatureMapperTest extends TestBase {
    @Autowired
    private PlantTaxonNomenclatureRepository plantTaxonNomenclatureRepository;

    @Autowired
    private PlantTaxonNomenclatureMapper plantTaxonNomenclatureMapper;

    private Long id;

    @Before
    public void init() {
        id = 100L;
    }

    // Test conversion form PlantTaxonNomenclature to CPlantTaxonNomenclature.
    @Test
    @Rollback
    public void transformPlantTaxonNomenclatureToCorePlantTaxonNomenclatureTest(){

        PlantTaxonNomenclature originalPlantTaxonNomenclature = plantTaxonNomenclatureRepository.findById(id).orElse(null);
        CPlantTaxonNomenclature cPlantTaxonNomenclature = plantTaxonNomenclatureMapper.toCore(originalPlantTaxonNomenclature);

        assertNotNull(cPlantTaxonNomenclature);
        assertEquals(cPlantTaxonNomenclature.getId().longValue(), originalPlantTaxonNomenclature.getId().longValue());
        assertEquals(cPlantTaxonNomenclature.getTaxonCode(), originalPlantTaxonNomenclature.getTaxonCode());
        assertEquals(cPlantTaxonNomenclature.getCommonClassifyBul(), originalPlantTaxonNomenclature.getCommonClassifyBul());
        assertEquals(cPlantTaxonNomenclature.getCommonClassifyEng(), originalPlantTaxonNomenclature.getCommonClassifyEng());
        assertEquals(cPlantTaxonNomenclature.getLatinClassify(), originalPlantTaxonNomenclature.getLatinClassify());
    }
}
