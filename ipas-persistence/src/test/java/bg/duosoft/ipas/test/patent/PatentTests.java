package bg.duosoft.ipas.test.patent;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;



public class PatentTests extends TestBase {

    @Autowired
    private IpPatentRepository ipPatentRepository;

    @Autowired
    private PatentService patentService;


    @Test
    @Transactional
    public void readPatent(){
        IpFilePK filePk = new IpFilePK();
        filePk.setFileSeq("BG");
        filePk.setFileTyp("P");
        filePk.setFileSer(2018);
        filePk.setFileNbr(112748);

        IpPatent ipPatent=ipPatentRepository.findById(filePk).orElse(null);

        System.out.println();

    }

    @Test
    @Transactional
    public void readCPatent(){
        IpFilePK filePk = new IpFilePK();
        filePk.setFileSeq("BG");
        filePk.setFileTyp("P");
        filePk.setFileSer(2018);
        filePk.setFileNbr(112748);

        CPatent cPatent=patentService.findPatent("BG", "P", 2018, 112748, false);

        System.out.println(cPatent.getRowVersion());

    }

}
