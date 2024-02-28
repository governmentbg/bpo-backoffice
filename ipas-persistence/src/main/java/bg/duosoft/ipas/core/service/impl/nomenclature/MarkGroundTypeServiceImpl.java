package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.userdoc.grounds.MarkGroundTypeMapper;
import bg.duosoft.ipas.core.model.userdoc.grounds.CMarkGroundType;
import bg.duosoft.ipas.core.service.nomenclature.MarkGroundTypeService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfMarkGroundTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MarkGroundTypeServiceImpl implements MarkGroundTypeService {
    @Autowired
    private CfMarkGroundTypeRepository cfMarkGroundTypeRepository;
    @Autowired
    private MarkGroundTypeMapper markGroundTypeMapper;
    @Override
    public List<CMarkGroundType> findAll() {
        return cfMarkGroundTypeRepository.findAll().stream().map(r->markGroundTypeMapper.toCore(r)).collect(Collectors.toList());
    }

    @Override
    public CMarkGroundType findById(Integer markGroupType) {
        return markGroundTypeMapper.toCore(cfMarkGroundTypeRepository.findById(markGroupType).orElse(null));
    }
}
