package bg.duosoft.ipas.core.service.impl.nomenclature;


import bg.duosoft.ipas.core.mapper.miscellaneous.ClassCpcIpcConcordanceMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CClassCpcIpcConcordance;
import bg.duosoft.ipas.core.service.nomenclature.ClassCpcIpcConcordanceService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpcIpcConcordance;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpcIpcConcordancePK;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.ClassCpcIpcConcordanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ClassCpcIpcConcordanceServiceImpl implements ClassCpcIpcConcordanceService {

    @Autowired
    private ClassCpcIpcConcordanceRepository repository;

    @Autowired
    private ClassCpcIpcConcordanceMapper mapper;

    @Override
    public CClassCpcIpcConcordance findById(String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode) {
        CfClassCpcIpcConcordancePK pk = new CfClassCpcIpcConcordancePK(sectionCode,classCode,subclassCode,groupCode,subgroupCode);
        CfClassCpcIpcConcordance cfClassCpcIpcConcordance = repository.findById(pk).orElse(null);
        return mapper.toCore(cfClassCpcIpcConcordance);
    }
}
