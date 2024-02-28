package bg.duosoft.ipas.test.plant;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
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


public class PlantTaxonNomenclatureTests extends TestBase {

    @Autowired
    private PlantTaxonNomenclatureRepository plantTaxonNomenclatureRepository;

    private Long id;

    @Before
    public void init() {
        id = 100L;
    }

    @Test
    @Rollback
    public void readPlantNomencaltur(){

        PlantTaxonNomenclature plantNomenclature = plantTaxonNomenclatureRepository.findById(id).orElse(null);

        assertNotNull(plantNomenclature);
        assertEquals(plantNomenclature.getId().longValue(), 100L);
        assertEquals(plantNomenclature.getTaxonCode(), "SDAFA");
        assertEquals(plantNomenclature.getCommonClassifyBul(), "неизвесно5");
        assertEquals(plantNomenclature.getCommonClassifyEng(), "asjkdfas");
        assertEquals(plantNomenclature.getLatinClassify(), "sdafa");
    }
}
