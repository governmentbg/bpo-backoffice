package bg.duosoft.ipas.core.service.impl.person;

import bg.duosoft.ipas.core.mapper.person.PersonIdTypeMapper;
import bg.duosoft.ipas.core.model.person.CPersonIdType;
import bg.duosoft.ipas.core.service.person.PersonIdTypeService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfPersonIdTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PersonIdTypeServiceImpl implements PersonIdTypeService {

    private final CfPersonIdTypeRepository repository;
    private final PersonIdTypeMapper mapper;

    @Override
    public CPersonIdType selectById(String type) {
        return mapper.toCore(
                repository.findById(type).orElse(null)
        );
    }
}
