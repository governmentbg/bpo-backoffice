package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.userdoc.grounds.LegalGroundTypesMapper;
import bg.duosoft.ipas.core.model.userdoc.grounds.CLegalGroundTypes;
import bg.duosoft.ipas.core.service.nomenclature.LegalGroundTypesService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLegalGroundTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LegalGroundTypesServiceImpl implements LegalGroundTypesService {

    @Autowired
    private CfLegalGroundTypesRepository cfLegalGroundTypesRepository;

    @Autowired
    private LegalGroundTypesMapper legalGroundTypesMapper;

    @Override
    public List<CLegalGroundTypes> findAllLegalGroundTypesForSpecificPanel(String version, String panel) {
       return cfLegalGroundTypesRepository.findAllLegalGroundsForSpecificPanel(version,panel).stream()
               .map(r->legalGroundTypesMapper.toCore(r)).collect(Collectors.toList());
    }

    @Override
    public List<CLegalGroundTypes> findAllLegalGroundsForSpecificPanelAndEarlierRight(String version, String panel, Integer earlierRightTypeId) {
        return cfLegalGroundTypesRepository.findAllLegalGroundsForSpecificPanelAndEarlierRight(version,panel,earlierRightTypeId).stream()
                .map(r->legalGroundTypesMapper.toCore(r)).collect(Collectors.toList());
    }

    @Override
    public CLegalGroundTypes findById(Integer id) {
        return legalGroundTypesMapper.toCore(cfLegalGroundTypesRepository.findById(id).orElse(null));
    }

    @Override
    public List<CLegalGroundTypes> findAllLegalGroundsByVersionAndCode(String version, String code) {
        return cfLegalGroundTypesRepository.findAllLegalGroundsByVersionAndCode(version,code).stream()
                .map(r->legalGroundTypesMapper.toCore(r)).collect(Collectors.toList());
    }
}
