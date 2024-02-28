package bg.duosoft.ipas.test.repository.nomenclature;


import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassViennaSect;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassViennaSectPK;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfClassViennaSectionRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class BaseNomenclatureTest extends TestBase {
    @Autowired
    private CfClassViennaSectionRepository cfClassViennaSectionRepository;

    @Test
    @Transactional
    public void readCfViennaSecttion() {
        CfClassViennaSectPK id = new CfClassViennaSectPK(1l, 1l, 1l, "0");
        CfClassViennaSect res = cfClassViennaSectionRepository.getOne(id);
        assertEquals("Stars", res.getViennaSectionName());
        System.out.println(res);
    }

    @Test
    @Transactional
    public void readnonExistingCfViennaSecttion() {
        CfClassViennaSectPK id = new CfClassViennaSectPK(1l, 1l, null, null);
        Optional<CfClassViennaSect> res = cfClassViennaSectionRepository.findById(id);
        assertEquals(true, res.isEmpty());
        System.out.println(res);
    }
}
