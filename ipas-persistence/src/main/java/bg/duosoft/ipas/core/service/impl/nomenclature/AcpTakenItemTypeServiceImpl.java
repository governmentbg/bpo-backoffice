package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.acp.AcpTakenItemTypeMapper;
import bg.duosoft.ipas.core.model.acp.CAcpTakenItemType;
import bg.duosoft.ipas.core.service.nomenclature.AcpTakenItemTypeService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpTakenItemType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfAcpTakenItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcpTakenItemTypeServiceImpl implements AcpTakenItemTypeService {

    @Autowired
    private CfAcpTakenItemTypeRepository repository;

    @Autowired
    private AcpTakenItemTypeMapper mapper;

    @Override
    public CAcpTakenItemType findById(Integer id) {
        return mapper.toCore(repository.findById(id).orElse(null));
    }

    @Override
    public List<CAcpTakenItemType> findAll() {
        List<CfAcpTakenItemType> all = repository.findAll();
        return mapper.toCoreList(all);
    }
}
