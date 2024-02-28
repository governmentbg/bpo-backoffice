package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.acp.AcpTakenItemStorageMapper;
import bg.duosoft.ipas.core.model.acp.CAcpTakenItemStorage;
import bg.duosoft.ipas.core.service.nomenclature.AcpTakenItemStorageService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAcpTakenItemStorage;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfAcpTakenItemStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcpTakenItemStorageServiceImpl implements AcpTakenItemStorageService {

    @Autowired
    private CfAcpTakenItemStorageRepository repository;

    @Autowired
    private AcpTakenItemStorageMapper mapper;

    @Override
    public CAcpTakenItemStorage findById(Integer id) {
        return mapper.toCore(repository.findById(id).orElse(null));
    }

    @Override
    public List<CAcpTakenItemStorage> findAll() {
        List<CfAcpTakenItemStorage> all = repository.findAll();
        return mapper.toCoreList(all);
    }
}
