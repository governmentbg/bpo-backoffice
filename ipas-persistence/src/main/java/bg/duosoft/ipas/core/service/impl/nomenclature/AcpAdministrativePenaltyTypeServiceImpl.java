package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.acp.AcpAdministrativePenaltyTypeMapper;
import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenaltyType;
import bg.duosoft.ipas.core.service.nomenclature.AcpAdministrativePenaltyTypeService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfAcpAdministrativePenaltyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcpAdministrativePenaltyTypeServiceImpl implements AcpAdministrativePenaltyTypeService {

    @Autowired
    private CfAcpAdministrativePenaltyTypeRepository repository;

    @Autowired
    private AcpAdministrativePenaltyTypeMapper mapper;

    @Override
    public CAcpAdministrativePenaltyType findById(Integer id) {
        return mapper.toCore(repository.findById(id).orElse(null));
    }

    @Override
    public List<CAcpAdministrativePenaltyType> findAll() {
        return mapper.toCoreList(repository.findAll());
    }

}
