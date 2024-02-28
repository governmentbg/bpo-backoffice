package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.service.nomenclature.ClassNiceService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassNice;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfClassNiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Raya
 * 28.03.2019
 */
@Service
@Transactional
public class ClassNiceServiceImpl implements ClassNiceService {

    @Autowired
    private CfClassNiceRepository cfClassNiceRepository;

    @Override
    public String getNiceClassDescription(Long code, Long edition, String version) {
        CfClassNice cf = cfClassNiceRepository.selectByPK(code, edition, version);
        if(cf != null) {
            return cf.getClassDetail();
        } else {
            return null;
        }
    }
}
