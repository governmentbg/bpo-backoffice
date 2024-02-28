package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.acp.AcpCheckReasonNomenclatureMapper;
import bg.duosoft.ipas.core.model.acp.CAcpCheckReasonNomenclature;
import bg.duosoft.ipas.core.service.nomenclature.AcpCheckReasonService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfAcpCheckReasonRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfAppTypeAcpCheckReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AcpCheckReasonServiceImpl implements AcpCheckReasonService {

    @Autowired
    private CfAcpCheckReasonRepository repository;

    @Autowired
    private AcpCheckReasonNomenclatureMapper mapper;

    @Autowired
    private CfAppTypeAcpCheckReasonRepository appTypeAcpCheckReasonRepository;

    @Override
    public CAcpCheckReasonNomenclature findById(Integer id) {
        return mapper.toCore(repository.findById(id).orElse(null));
    }

    @Override
    public List<CAcpCheckReasonNomenclature> findAll() {
        return mapper.toCoreList(repository.findAll());
    }

    @Override
    public List<CAcpCheckReasonNomenclature> findAllByApplicationType(String applicationType) {
        List<CAcpCheckReasonNomenclature> acpCheckReasons = mapper.toCoreList(appTypeAcpCheckReasonRepository.selectAcpCheckReasonsByAppType(applicationType));
        acpCheckReasons.sort(Comparator.comparing(CAcpCheckReasonNomenclature::getDescription));

        return acpCheckReasons;
    }
}
