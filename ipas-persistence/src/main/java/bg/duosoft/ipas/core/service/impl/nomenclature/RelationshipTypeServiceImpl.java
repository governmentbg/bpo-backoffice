package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.file.RelationshipTypeMapper;
import bg.duosoft.ipas.core.model.file.CRelationshipType;
import bg.duosoft.ipas.core.service.nomenclature.RelationshipTypeService;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfRelationshipType;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfRelationshipTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RelationshipTypeServiceImpl implements RelationshipTypeService {

    @Autowired
    private CfRelationshipTypeRepository relationshipTypeRepository;

    @Autowired
    private RelationshipTypeMapper relationshipTypeMapper;

    @Override
    @Cacheable("relationshipTypesMap")
    public Map<String, CRelationshipType> selectAll() {
        List<CfRelationshipType> all = relationshipTypeRepository.findAll();
        if(CollectionUtils.isEmpty(all))
            return null;

        List<CRelationshipType> cRelationshipTypes = relationshipTypeMapper.toCoreList(all);
        return cRelationshipTypes.stream().collect(Collectors.toMap(CRelationshipType::getRelationshipType, o -> o));
    }
}