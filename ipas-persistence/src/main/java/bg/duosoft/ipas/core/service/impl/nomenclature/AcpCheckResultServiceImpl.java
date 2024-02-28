package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.acp.AcpCheckResultMapper;
import bg.duosoft.ipas.core.model.acp.CAcpCheckResult;
import bg.duosoft.ipas.core.service.nomenclature.AcpCheckResultService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfAcpCheckResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AcpCheckResultServiceImpl implements AcpCheckResultService {

    @Autowired
    private CfAcpCheckResultRepository repository;

    @Autowired
    private AcpCheckResultMapper mapper;

    @Override
    public CAcpCheckResult findById(Integer id) {
        return mapper.toCore(repository.findById(id).orElse(null));
    }

    @Override
    public List<CAcpCheckResult> findAll() {
        List<CAcpCheckResult> acpCheckResult = mapper.toCoreList(repository.findAll());
        acpCheckResult.sort(Comparator.comparing(CAcpCheckResult::getDescription));

        return acpCheckResult;
    }
}
