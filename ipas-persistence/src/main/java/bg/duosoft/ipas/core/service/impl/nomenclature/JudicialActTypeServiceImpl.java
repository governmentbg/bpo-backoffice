package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.userdoc.court_appeal.JudicialActTypeMapper;
import bg.duosoft.ipas.core.model.design.CImageViewType;
import bg.duosoft.ipas.core.model.userdoc.court_appeal.CJudicialActType;
import bg.duosoft.ipas.core.service.nomenclature.JudicialActTypeService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.JudicialActTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JudicialActTypeServiceImpl implements JudicialActTypeService {
    @Autowired
    private JudicialActTypeRepository judicialActTypeRepository;
    @Autowired
    private JudicialActTypeMapper judicialActTypeMapper;

    @Override
    public List<CJudicialActType> findAll() {
        List<CJudicialActType> juducialActType =judicialActTypeRepository.findAll().stream().map(r->judicialActTypeMapper.toCore(r)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(juducialActType)){
            juducialActType.sort(Comparator.comparing(CJudicialActType::getActTypeName));
        }
        return juducialActType;
    }
    @Override
    public CJudicialActType findById(Integer id) {
        return judicialActTypeMapper.toCore(judicialActTypeRepository.findById(id).orElse(null));
    }
}
