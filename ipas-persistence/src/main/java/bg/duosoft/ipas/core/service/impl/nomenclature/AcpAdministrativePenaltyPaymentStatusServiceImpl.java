package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.acp.AcpAdministrativePenaltyPaymentStatusMapper;
import bg.duosoft.ipas.core.mapper.acp.AcpAdministrativePenaltyTypeMapper;
import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenaltyPaymentStatus;
import bg.duosoft.ipas.core.model.acp.CAcpAdministrativePenaltyType;
import bg.duosoft.ipas.core.service.nomenclature.AcpAdministrativePenaltyPaymentStatusService;
import bg.duosoft.ipas.core.service.nomenclature.AcpAdministrativePenaltyTypeService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfAcpAdministrativePenaltyPaymentStatusRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfAcpAdministrativePenaltyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcpAdministrativePenaltyPaymentStatusServiceImpl implements AcpAdministrativePenaltyPaymentStatusService {

    @Autowired
    private CfAcpAdministrativePenaltyPaymentStatusRepository repository;

    @Autowired
    private AcpAdministrativePenaltyPaymentStatusMapper mapper;

    @Override
    public CAcpAdministrativePenaltyPaymentStatus findById(Integer id) {
        return mapper.toCore(repository.findById(id).orElse(null));
    }

    @Override
    public List<CAcpAdministrativePenaltyPaymentStatus> findAll() {
        return mapper.toCoreList(repository.findAll());
    }
}
