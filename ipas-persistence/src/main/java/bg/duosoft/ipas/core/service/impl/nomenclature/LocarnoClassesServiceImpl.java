package bg.duosoft.ipas.core.service.impl.nomenclature;

import bg.duosoft.ipas.core.mapper.miscellaneous.LocarnoClassesMapper;
import bg.duosoft.ipas.core.model.design.CLocarnoClasses;
import bg.duosoft.ipas.core.service.nomenclature.LocarnoClassesService;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLocarnoClassesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocarnoClassesServiceImpl implements LocarnoClassesService {

    @Autowired
    private CfLocarnoClassesRepository cfLocarnoClassesRepository;

    @Autowired
    private LocarnoClassesMapper locarnoClassesMapper;

    @Override
    public List<CLocarnoClasses> findByLocarnoClassCode(String locarnoClassCode, int maxResults) {
        Pageable pageable = PageRequest.of(0, maxResults);
        return cfLocarnoClassesRepository.findByLocarnoClassCode("%"+locarnoClassCode+"%",pageable)
                .stream().map(r->locarnoClassesMapper.toCore(r)).collect(Collectors.toList());
    }

    @Override
    public boolean isLocarnoClassExist(String locarnoClassCode) {
        Integer count = cfLocarnoClassesRepository.countByClassCode(locarnoClassCode);
        return !(Objects.isNull(count) || 0 == count);
    }
}
