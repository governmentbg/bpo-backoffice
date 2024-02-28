package bg.duosoft.ipas.test.mapper;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.mapper.patent.PatentMapper;
import bg.duosoft.ipas.core.mapper.plant.PlantMapper;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.plant.CPlant;
import bg.duosoft.ipas.core.model.plant.CPlantTaxonNomenclature;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.persistence.model.entity.ext.plant.Plant;
import bg.duosoft.ipas.persistence.model.entity.ext.plant.PlantTaxonNomenclature;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.persistence.repository.entity.plant.PlantRepository;
import bg.duosoft.ipas.test.TestBase;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

public class PlantMapperTest extends TestBase {
    @Autowired
    private IpPatentRepository ipPatentRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private PatentMapper patentMapper;

    @Autowired
    private PlantMapper plantMapper;

    private IpFilePK ipFilePK;

    private IpFilePK ipFilePK1;


    @Before
    public void init() {
        ipFilePK  = new IpFilePK("BG", "С", 2018, 1071);
        ipFilePK1 = new IpFilePK("BG", "С", 2013, 913);
    }

    // Test conversion form Plant to CPlant.
    @Test
    @Rollback
    public void transformPlantToCorePlantTest(){
        Plant originalPlant = plantRepository.findById(ipFilePK).orElse(null);
        CPlant cPlant = plantMapper.toCore(originalPlant);

        Gson gson = new Gson();
        System.out.println(gson.toJson(cPlant));

        assertNotNull(cPlant);
        assertEquals(cPlant.getProposedDenomination(), originalPlant.getProposedDenomination());
        assertEquals(cPlant.getProposedDenominationEng(), originalPlant.getProposedDenominationEng());
        assertEquals(cPlant.getPublDenomination(), originalPlant.getPublDenomination());
        assertEquals(cPlant.getPublDenominationEng(), originalPlant.getPublDenominationEng());
        assertEquals(cPlant.getApprDenomination(), originalPlant.getApprDenomination());
        assertEquals(cPlant.getApprDenominationEng(), originalPlant.getApprDenominationEng());
        assertEquals(cPlant.getRejDenomination(), originalPlant.getRejDenomination());
        assertEquals(cPlant.getRejDenominationEng(), originalPlant.getRejDenominationEng());
        assertEquals(cPlant.getFeatures(), originalPlant.getFeatures());
        assertEquals(cPlant.getStability(), originalPlant.getStability());
        assertEquals(cPlant.getTesting(), originalPlant.getTesting());

        PlantTaxonNomenclature originalPlantTaxonNomenclature = originalPlant.getPlantNumenclature();
        CPlantTaxonNomenclature cPlantTaxonNomenclature = cPlant.getPlantNumenclature();

        assertNotNull(cPlantTaxonNomenclature);
        assertEquals(cPlantTaxonNomenclature.getId().longValue(), originalPlantTaxonNomenclature.getId().longValue());
        assertEquals(cPlantTaxonNomenclature.getTaxonCode(), originalPlantTaxonNomenclature.getTaxonCode());
        assertEquals(cPlantTaxonNomenclature.getCommonClassifyBul(), originalPlantTaxonNomenclature.getCommonClassifyBul());
        assertEquals(cPlantTaxonNomenclature.getCommonClassifyEng(), originalPlantTaxonNomenclature.getCommonClassifyEng());
        assertEquals(cPlantTaxonNomenclature.getLatinClassify(), originalPlantTaxonNomenclature.getLatinClassify());
    }

    @Test
    @Rollback
    public void transformPlantToCorePlantTest1(){
        Plant originalPlant = plantRepository.findById(ipFilePK1).orElse(null);

        CPlant cPlant = plantMapper.toCore(originalPlant);

        Gson gson = new Gson();
        System.out.println(gson.toJson(cPlant));

        assertNotNull(cPlant);
        assertEquals(cPlant.getProposedDenomination(), originalPlant.getProposedDenomination());
        assertEquals(cPlant.getProposedDenominationEng(), originalPlant.getProposedDenominationEng());
        assertEquals(cPlant.getPublDenomination(), originalPlant.getPublDenomination());
        assertEquals(cPlant.getPublDenominationEng(), originalPlant.getPublDenominationEng());
        assertEquals(cPlant.getApprDenomination(), originalPlant.getApprDenomination());
        assertEquals(cPlant.getApprDenominationEng(), originalPlant.getApprDenominationEng());
        assertEquals(cPlant.getRejDenomination(), originalPlant.getRejDenomination());
        assertEquals(cPlant.getRejDenominationEng(), originalPlant.getRejDenominationEng());
        assertEquals(cPlant.getFeatures(), originalPlant.getFeatures());
        assertEquals(cPlant.getStability(), originalPlant.getStability());
        assertEquals(cPlant.getTesting(), originalPlant.getTesting());

        PlantTaxonNomenclature originalPlantTaxonNomenclature = originalPlant.getPlantNumenclature();
        CPlantTaxonNomenclature cPlantTaxonNomenclature = cPlant.getPlantNumenclature();

        assertNotNull(cPlantTaxonNomenclature);
        assertEquals(cPlantTaxonNomenclature.getId().longValue(), originalPlantTaxonNomenclature.getId().longValue());
        assertEquals(cPlantTaxonNomenclature.getTaxonCode(), originalPlantTaxonNomenclature.getTaxonCode());
        assertEquals(cPlantTaxonNomenclature.getCommonClassifyBul(), originalPlantTaxonNomenclature.getCommonClassifyBul());
        assertEquals(cPlantTaxonNomenclature.getCommonClassifyEng(), originalPlantTaxonNomenclature.getCommonClassifyEng());
        assertEquals(cPlantTaxonNomenclature.getLatinClassify(), originalPlantTaxonNomenclature.getLatinClassify());
    }

    @Test
    @Transactional
    public void transformPatentToCorePatentTest(){
        IpPatent originalPatent = ipPatentRepository.findById(ipFilePK).orElse(null);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);

        assertNotNull(cPatent);
        assertNotNull(cPatent.getPlantData());

        CPlant plantData = cPatent.getPlantData();
        Plant originalPlant = originalPatent.getPlantData();

        assertEquals(plantData.getProposedDenomination(), originalPlant.getProposedDenomination());
        assertEquals(plantData.getProposedDenominationEng(), originalPlant.getProposedDenominationEng());
        assertEquals(plantData.getPublDenomination(), originalPlant.getPublDenomination());
        assertEquals(plantData.getPublDenominationEng(), originalPlant.getPublDenominationEng());
        assertEquals(plantData.getApprDenomination(), originalPlant.getApprDenomination());
        assertEquals(plantData.getApprDenominationEng(), originalPlant.getApprDenominationEng());
        assertEquals(plantData.getRejDenomination(), originalPlant.getRejDenomination());
        assertEquals(plantData.getRejDenominationEng(), originalPlant.getRejDenominationEng());
        assertEquals(plantData.getFeatures(), originalPlant.getFeatures());
        assertEquals(plantData.getStability(), originalPlant.getStability());
        assertEquals(plantData.getTesting(), originalPlant.getTesting());

        PlantTaxonNomenclature originalPlantTaxonNomenclature = originalPlant.getPlantNumenclature();
        CPlantTaxonNomenclature cPlantTaxonNomenclature = plantData.getPlantNumenclature();

        assertNotNull(cPlantTaxonNomenclature);
        assertEquals(cPlantTaxonNomenclature.getId().longValue(), originalPlantTaxonNomenclature.getId().longValue());
        assertEquals(cPlantTaxonNomenclature.getTaxonCode(), originalPlantTaxonNomenclature.getTaxonCode());
        assertEquals(cPlantTaxonNomenclature.getCommonClassifyBul(), originalPlantTaxonNomenclature.getCommonClassifyBul());
        assertEquals(cPlantTaxonNomenclature.getCommonClassifyEng(), originalPlantTaxonNomenclature.getCommonClassifyEng());
        assertEquals(cPlantTaxonNomenclature.getLatinClassify(), originalPlantTaxonNomenclature.getLatinClassify());
    }

    @Test
    @Transactional
    public void transformIpPatentToCPatentRevertToIpPatentTestIpPatent() {

        IpPatent originalPatent = ipPatentRepository.findById(ipFilePK1).orElse(null);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);

        assertNotNull(ipPatent);
        assertNotNull(ipPatent.getPlantData());

        Plant plant = ipPatent.getPlantData();
        Plant originalPlant = originalPatent.getPlantData();

        assertEquals(ipFilePK1.getFileSeq(), plant.getPk().getFileSeq());
        assertEquals(ipFilePK1.getFileTyp(), plant.getPk().getFileTyp());
        assertEquals(ipFilePK1.getFileSer(), plant.getPk().getFileSer());
        assertEquals(ipFilePK1.getFileNbr(), plant.getPk().getFileNbr());

        assertEquals(originalPlant.getPk().getFileSeq(), plant.getPk().getFileSeq());
        assertEquals(originalPlant.getPk().getFileTyp(), plant.getPk().getFileTyp());
        assertEquals(originalPlant.getPk().getFileSer(), plant.getPk().getFileSer());
        assertEquals(originalPlant.getPk().getFileNbr(), plant.getPk().getFileNbr());

        assertEquals(originalPlant.getProposedDenomination(), plant.getProposedDenomination());
        assertEquals(originalPlant.getProposedDenominationEng(), plant.getProposedDenominationEng());
        assertEquals(originalPlant.getPublDenomination(), plant.getPublDenomination());
        assertEquals(originalPlant.getPublDenominationEng(), plant.getPublDenominationEng());
        assertEquals(originalPlant.getApprDenomination(), plant.getApprDenomination());
        assertEquals(originalPlant.getApprDenominationEng(), plant.getApprDenominationEng());
        assertEquals(originalPlant.getRejDenomination(), plant.getRejDenomination());
        assertEquals(originalPlant.getRejDenominationEng(), plant.getRejDenominationEng());
        assertEquals(originalPlant.getFeatures(), plant.getFeatures());
        assertEquals(originalPlant.getStability(), plant.getStability());
        assertEquals(originalPlant.getTesting(), plant.getTesting());

        PlantTaxonNomenclature originalPlantTaxonNomenclature = originalPlant.getPlantNumenclature();
        PlantTaxonNomenclature plantTaxonNomenclature = plant.getPlantNumenclature();

        assertNotNull(plantTaxonNomenclature);
        assertEquals(plantTaxonNomenclature.getId().longValue(), originalPlantTaxonNomenclature.getId().longValue());
        assertEquals(plantTaxonNomenclature.getTaxonCode(), originalPlantTaxonNomenclature.getTaxonCode());
        assertEquals(plantTaxonNomenclature.getCommonClassifyBul(), originalPlantTaxonNomenclature.getCommonClassifyBul());
        assertEquals(plantTaxonNomenclature.getCommonClassifyEng(), originalPlantTaxonNomenclature.getCommonClassifyEng());
        assertEquals(plantTaxonNomenclature.getLatinClassify(), originalPlantTaxonNomenclature.getLatinClassify());
    }
}
