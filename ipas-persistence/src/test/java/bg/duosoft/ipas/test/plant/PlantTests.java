package bg.duosoft.ipas.test.plant;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.persistence.model.entity.ext.plant.Plant;
import bg.duosoft.ipas.persistence.model.entity.ext.plant.PlantTaxonNomenclature;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.persistence.repository.entity.plant.PlantRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


public class PlantTests extends TestBase {
    @Autowired
    private IpPatentRepository ipPatentRepository;

    @Autowired
    private PlantRepository plantRepository;

    private IpFilePK ipFilePK;

    private IpFilePK ipFilePK1;

    @Before
    public void init() {
        ipFilePK = new IpFilePK();

        ipFilePK.setFileSeq("BG");
        ipFilePK.setFileTyp("С");
        ipFilePK.setFileSer(2018);
        ipFilePK.setFileNbr(1071);

        ipFilePK1 = new IpFilePK();

        ipFilePK1.setFileSeq("BG");
        ipFilePK1.setFileTyp("С");
        ipFilePK1.setFileSer(2013);
        ipFilePK1.setFileNbr(913);
    }

    @Test
    @Rollback
    public void readPlant(){

        Plant plant = plantRepository.findById(ipFilePK).orElse(null);

        System.out.println(plant);
        assertNotNull(plant);
        assertEquals(plant.getPk().getFileSeq(), "BG");
        assertEquals(plant.getPk().getFileTyp(), "С");
        assertEquals(plant.getPk().getFileSer().longValue(), 2018);
        assertEquals(plant.getPk().getFileNbr().longValue(), 1071);

        assertEquals(plant.getProposedDenomination(), "МИСКЕТ ВИКИНГ");
        assertEquals(plant.getProposedDenominationEng(), "MISKET VIKING");
        assertNull(plant.getPublDenomination());
        assertNull(plant.getPublDenominationEng());
        assertNull(plant.getApprDenomination());
        assertNull(plant.getApprDenominationEng());
        assertNull(plant.getRejDenomination());
        assertNull(plant.getRejDenominationEng());
        assertNull(plant.getFeatures());
        assertEquals(plant.getStability(), "Сортът е  с повишена зимоустойчивост и средно устойчив на криптогамни болести");
        assertEquals(plant.getTesting(), "Сортът е подходящ за отглеждане при всички видове формировки");


        PlantTaxonNomenclature plantTaxonNomenclature = plant.getPlantNumenclature();

        assertNotNull(plantTaxonNomenclature);
        assertEquals(plantTaxonNomenclature.getId().longValue(), 1587L);
        assertEquals(plantTaxonNomenclature.getTaxonCode(), "VITIS-VIN");
        assertEquals(plantTaxonNomenclature.getCommonClassifyBul(), "Лоза");
        assertEquals(plantTaxonNomenclature.getCommonClassifyEng(), "losa");
        assertEquals(plantTaxonNomenclature.getLatinClassify(), "Vitis vinifera L.");

    }

    @Test
    @Rollback
    public void readPlant1(){

        Plant plant = plantRepository.findById(ipFilePK1).orElse(null);

        System.out.println(plant);
        assertNotNull(plant);
        assertEquals(plant.getPk().getFileSeq(), "BG");
        assertEquals(plant.getPk().getFileTyp(), "С");
        assertEquals(plant.getPk().getFileSer().longValue(), 2013);
        assertEquals(plant.getPk().getFileNbr().longValue(), 913);

        assertEquals(plant.getProposedDenomination(), "\"ДУНАВИЯ\"");
        assertEquals(plant.getProposedDenominationEng(), "\"DUNAVIYA\"");
        assertEquals(plant.getPublDenomination(), "\"ДУНАВИЯ\"");
        assertEquals(plant.getPublDenominationEng(), "\"DUNAVIYA\"");
        assertEquals(plant.getApprDenomination(), "\"ДУНАВИЯ\"");
        assertEquals(plant.getApprDenominationEng(), "\"DUNAVIYA\"");
        assertEquals(plant.getRejDenomination(), "\"ДУНАВИЯ\"");
        assertEquals(plant.getRejDenominationEng(), "\"DUNAVIYA\"");
        assertEquals(plant.getFeatures(), "1. Време на изкласяване\n" +
                "а) сорт Плиска - 05.05\n" +
                "б) сорт Дунавия - 07.05\n" +
                "2. Глума: форма на рамото\n" +
                "а) сорт Садово 1 - средна\n" +
                "б) сорт Дунавия - силна\n" +
                "3. Височина на растението\n" +
                "а) сорт Плиска - 80 см\n" +
                "б) сорт Дунавия - 100 см\n" +
                "4. Флагов лист - окосменост на влагалището\n" +
                "а) сорт Садово 1- силна\n" +
                "б) сорт Дунавия - средна");
        assertEquals(plant.getStability(), "Кафява ръжда - средно устойчива\n" +
                "Черна ръжда - средна устойчивост\n" +
                "Брашнеста мана - средна устойчивост");
        assertNull(plant.getTesting());

        PlantTaxonNomenclature plantTaxonNomenclature = plant.getPlantNumenclature();

        assertNotNull(plantTaxonNomenclature);
        assertEquals(plantTaxonNomenclature.getId().longValue(), 2045L);
        assertEquals(plantTaxonNomenclature.getTaxonCode(), "TRITI-AES");
        assertEquals(plantTaxonNomenclature.getCommonClassifyBul(), "Пшеница");
        assertEquals(plantTaxonNomenclature.getCommonClassifyEng(), "Wheat");
        assertEquals(plantTaxonNomenclature.getLatinClassify(), "Triticum aestivum L. emend. Fiori et Paol.");

    }

    @Test
    @Rollback
    public void readPatent(){
        IpPatent originalPatent = ipPatentRepository.findById(ipFilePK1).orElse(null);

        assertNotNull(originalPatent);
        assertNotNull(originalPatent.getPlantData());

        Plant plant = originalPatent.getPlantData();

        assertNotNull(plant);
        assertEquals(plant.getPk().getFileSeq(), "BG");
        assertEquals(plant.getPk().getFileTyp(), "С");
        assertEquals(plant.getPk().getFileSer().longValue(), 2013);
        assertEquals(plant.getPk().getFileNbr().longValue(), 913);

        assertEquals(plant.getProposedDenomination(), "\"ДУНАВИЯ\"");
        assertEquals(plant.getProposedDenominationEng(), "\"DUNAVIYA\"");
        assertEquals(plant.getPublDenomination(), "\"ДУНАВИЯ\"");
        assertEquals(plant.getPublDenominationEng(), "\"DUNAVIYA\"");
        assertEquals(plant.getApprDenomination(), "\"ДУНАВИЯ\"");
        assertEquals(plant.getApprDenominationEng(), "\"DUNAVIYA\"");
        assertEquals(plant.getRejDenomination(), "\"ДУНАВИЯ\"");
        assertEquals(plant.getRejDenominationEng(), "\"DUNAVIYA\"");
        assertEquals(plant.getFeatures(), "1. Време на изкласяване\n" +
                "а) сорт Плиска - 05.05\n" +
                "б) сорт Дунавия - 07.05\n" +
                "2. Глума: форма на рамото\n" +
                "а) сорт Садово 1 - средна\n" +
                "б) сорт Дунавия - силна\n" +
                "3. Височина на растението\n" +
                "а) сорт Плиска - 80 см\n" +
                "б) сорт Дунавия - 100 см\n" +
                "4. Флагов лист - окосменост на влагалището\n" +
                "а) сорт Садово 1- силна\n" +
                "б) сорт Дунавия - средна");
        assertEquals(plant.getStability(), "Кафява ръжда - средно устойчива\n" +
                "Черна ръжда - средна устойчивост\n" +
                "Брашнеста мана - средна устойчивост");
        assertNull(plant.getTesting());

        PlantTaxonNomenclature plantTaxonNomenclature = plant.getPlantNumenclature();

        assertNotNull(plantTaxonNomenclature);
        assertEquals(plantTaxonNomenclature.getId().longValue(), 2045L);
        assertEquals(plantTaxonNomenclature.getTaxonCode(), "TRITI-AES");
        assertEquals(plantTaxonNomenclature.getCommonClassifyBul(), "Пшеница");
        assertEquals(plantTaxonNomenclature.getCommonClassifyEng(), "Wheat");
        assertEquals(plantTaxonNomenclature.getLatinClassify(), "Triticum aestivum L. emend. Fiori et Paol.");

    }
}
